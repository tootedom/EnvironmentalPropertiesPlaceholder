/*
 * Copyright 2012 dominictootell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.greencheek.utils.environment.propertyplaceholder.merger;

import org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesMergerBuilder;
import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentVariableReader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.exception.NoDefaultPropertiesFileException;
import org.greencheek.utils.environment.propertyplaceholder.resolver.exception.NoMatchingPropertyException;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.FileSystemResourceLoader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.Resource;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Thread safe implementation of the @{link #PropertiesMerger}.  The implementation is constructed via a
 * @{link #PropertiesMergerBuilder}.
 *
 * The properties are obtained first from either a location on the classpath or filesystem.  These set of
 * properties can vary depending upon a given set of environment or system variables: i.e:
 * <ol>
 *     <li>Read default.props</li>
 *     <li>Override the properties set in default.props with the contents
 *     of a .properties file in the environments/ folder that represents the current value of the ENV system property, i.e. dev.properties</li>
 * </ol>
 * The properties files that are read that override default can vary not only on one ENV variable but could vary on several:
 * <ol>
 *     <li>Read default.props</li>
 *     <li>Override the properties set in default.props with the contents
 *     of a .properties file in the environments/ folder that represents the current value of the ${ENV} system property, i.e. dev.properties</li>
 *     <li>Override dev.properties with a .properties file that represents the current value of ${ENV}.${OS.ARCH} system and env properties i.e. dev.x86_64.properties</li>
 * </ol>
 * The obtained properties' values are not modified in any way.  Variables contained within the values should not be
 * modified or resolved.
 *
 * The implementation is thread safe.
 *
 * User: dominictootell
 * Date: 09/06/2012
 * Time: 12:26
 */
public class EnvironmentSpecificPropertiesMerger implements PropertiesMerger {

    private static final Logger log = LoggerFactory.getLogger(EnvironmentSpecificPropertiesMerger.class);

    private final Pattern sensitivePropertyMasker;
    private final boolean outputtingPropertiesInDebugMode;

    private final boolean strictMergingOfProperties;
    private final String nameOfDefaultPropertiesFile;


    private final char extensionSeparatorCharForPropertiesFile;
    private final String extensionForPropertiesFile;

    private final char delimiterUsedForSeparatingSwitchingConfigurationVariables;

    private final String applicationName;
    private final ResourceLoader operationalOverridesResourceLoader;
    private final ResourceLoader resourceLoaderForLoadingConfigurationProperties;
    private final String relativeLocationOfFilesOverridingDefaultProperties;

    private final OperatingEnvironmentVariableReader operatingEnvironmentVariableReader;

    private final Properties mergedProperties;

    /**
     * Holds the list of files that are to be looked for
     * in order in which they are merged (override) the defaults and the files before them.
     */
    private final List<String> possibleOverrideFiles = new ArrayList<String>();

    public EnvironmentSpecificPropertiesMerger(PropertiesMergerBuilder builder) {

        this.applicationName = builder.getApplicationName();

        ResourceLoader opsOverride =   builder.getResourceLoaderForOperationalOverrides();
        if(opsOverride == PropertiesMergerBuilder.DEFAULT_RESOURCE_LOADER_FOR_OPERATION_OVERRIDES) {
            if(applicationName!=null && applicationName.trim().length()>0) {
                opsOverride = new FileSystemResourceLoader(PropertiesMergerBuilder.DEFAULT_OPERATIONAL_OVERRIDE_LOCATION + applicationName + PropertiesMergerBuilder.DEFAULT_CONFIGURATION_LOCATION);
            } else {
                // Make it null and do not read anything for the overrides
                opsOverride = null;
            }
        }

        this.operationalOverridesResourceLoader =  opsOverride;
        this.resourceLoaderForLoadingConfigurationProperties = builder.getResourceLoaderForLoadingConfigurationProperties();


        this.extensionForPropertiesFile = builder.getExtensionForPropertiesFile();
        this.extensionSeparatorCharForPropertiesFile = builder.getExtensionSeparatorCharForPropertiesFile();

        this.nameOfDefaultPropertiesFile = builder.getNameOfDefaultPropertiesFile() +
                extensionSeparatorCharForPropertiesFile + extensionForPropertiesFile;

        this.strictMergingOfProperties = builder.isStrictMergingOfProperties();
        this.delimiterUsedForSeparatingSwitchingConfigurationVariables = builder.getDelimiterUsedForSeparatingSwitchingConfigurationVariables();
        this.operatingEnvironmentVariableReader = builder.getOperatingEnvironmentVariableReader();

        this.relativeLocationOfFilesOverridingDefaultProperties = builder.getRelativeLocationOfFilesOverridingDefaultProperties();
        this.sensitivePropertyMasker = builder.getSensitivePropertyMasker();
        this.outputtingPropertiesInDebugMode = builder.isOutputtingPropertiesInDebugMode();

//        SERVER_ENV
//        SERVER_ENV.TARGET_ENV
//        SERVER_ENV.SERVER_TYPE
//        SERVER_ENV.SERVER_TYPE.TARGET_ENV

        String[][] configurationPossibilities = builder.getVariablesUsedForSwitchingConfiguration();

        for(int i = 0;i<configurationPossibilities.length;i++) {
            String variables[] = configurationPossibilities[i];

            StringBuilder continuedFileName = (relativeLocationOfFilesOverridingDefaultProperties == null ||
                    relativeLocationOfFilesOverridingDefaultProperties.trim().length()==0) ?
                    new StringBuilder() :
                    new StringBuilder(relativeLocationOfFilesOverridingDefaultProperties);
            int noOfVars = 1;
            boolean done = false;
            for(String variable : variables) {

                String variableValue = operatingEnvironmentVariableReader.getProperty(variable, operatingEnvironmentVariableReader.getEnv(variable, ""));
                if(variableValue==null || variableValue.trim().length()==0) break;
                else {
                    continuedFileName.append(variableValue);
                    if(noOfVars++!=variables.length) continuedFileName.append(delimiterUsedForSeparatingSwitchingConfigurationVariables);
                    else done = true;
                }
            }

            if(done) {
                continuedFileName.append(extensionSeparatorCharForPropertiesFile).append(extensionForPropertiesFile);
                possibleOverrideFiles.add(continuedFileName.toString());
            }

        }

        mergedProperties = mergeProperties();

    }

    /**
     * <p>
     * uses java.util.Properties.load(InputStream), to load in the given stream into a
     * Properties object.  This method also checks to see if
     * the logger is in debug mode and output the contents of the properties object into it.
     * </p>
     * <p>
     * Developers need to be aware that this means  sensitive information could leak into log files
     * so they need to ensure that they aren't logging at debug level on live platforms.
     * </p>
     * <p>
     * A Pattern masking object can be used to blank out sensitive properties, so that output does
     * not leak into the log files
     * </p>
     *
     * @param stream
     * @return
     * @throws java.io.IOException
     */
    private Properties load(InputStream stream) throws IOException {

        Properties properties = new Properties();
        properties.load(stream);

        if (log.isDebugEnabled() && isOutputtingPropertiesInDebugMode()) {

            for (Object keyItem : properties.keySet()) {
                String key = (String)keyItem;
                String value = properties.getProperty(key);

                Matcher m = getSensitivePropertyMasker().matcher(key);
                if(m.matches()) {
                    log.debug(key + " : " + "######");
                }
                else {
                    log.debug(key + " : " + value);
                }

            }
        }
        return properties;
    }

    /**
     * For a given file, that represents a properties file, loads the contents into a Properties file
     * @param resource the file to convert to a Properties file.
     * @return a Properties object, will always return a properties object.
     */
    private Properties load(Resource resource) {
        if(resource == null) {
            return new Properties();
        }

        InputStream is = null;
        Properties p = null;
        try {
            log.debug("loading properties from {}",resource);
            is = resource.getStream();
            p = load(is);
        } catch(IOException e ) {
            log.warn("Unable to read file: {}",resource,e);
        } finally {
            if(is!=null) {
                try {
                    is.close();
                } catch (Exception e) {

                }
            }
        }

        return (p==null) ? new Properties() : p;
    }

    /**
     * Merges two properties objects by creating a new Properties object, adding all of the
     * first set of properties and then adding properties from the second. In effect, the first
     * set of properties is the default.<br/>
     * If the merging is strict, then an exception is throw if a property exists
     * in the second properties object that does not exist in the first.
     *
     *
     *
     * @param first can be null, the first set of properties that represents the defaults to be overridden
     * @param second can be null, the second set of properties that override the defaults in the first set of properties
     * @return The merge of the second properties over the top of the first set of properties
     */
    public static Properties mergeProperties(Properties first, Properties second, boolean strict) throws NoMatchingPropertyException {

        final Properties merged = new Properties();


        if(first!=null){
            for (final Object entry : first.keySet()) {
                final String key = entry.toString();
                merged.setProperty(key,first.getProperty(key));
            }
        }


        for (Object key : second.keySet()) {
            if (first.get(key)==null) {
                String msg = "NoMatchingPropertyWarning: Property \"" + key + "\" from overriding properties does not exist in original properties";
                if (strict) {
                    log.warn(msg);
                    throw new NoMatchingPropertyException(msg);
                } else {
                    log.warn(msg);
                }
            }
        }

        if (second != null) {
            for (final Object entry : second.keySet()) {
                final String key = entry.toString();
                merged.setProperty(key,second.getProperty(key));
            }
        }

        return merged;

    }

    private Resource[] loadFiles(List<String> files, ResourceLoader resourceLoader) {
        List<Resource> foundFiles = new ArrayList<Resource>(files.size());


        for(String location : files) {
            Resource override = resourceLoader.getFile(location);
            if(override.isAvailable())
                foundFiles.add(override);
        }

        return foundFiles.toArray(new Resource[foundFiles.size()]);
    }

    private Properties mergeProperties() {
        Properties merged;

        ResourceLoader defaultAppPropertiesLoader = getResourceLoaderForLoadingConfigurationProperties();
        Resource defaultProperties = defaultAppPropertiesLoader.getFile(getNameOfDefaultPropertiesFile());

        log.debug("Attempting to source default properties file {}",defaultProperties);
        if(!defaultProperties.isAvailable()) {// || !defaultProperties.canRead()) {
            String message = "Unable to source default properties file:" + getNameOfDefaultPropertiesFile();
            log.warn(message);
            if(isStrictMergingOfProperties())
                throw new NoDefaultPropertiesFileException(message);
        }


        merged = load(defaultProperties);

        for(Resource file : loadFiles(getPossibleOverrideFiles(),defaultAppPropertiesLoader)) {
            merged = mergeProperties(merged,load(file),strictMergingOfProperties);

        }

        // read overrides
        ResourceLoader overridesResourceLoader = getOperationalOverridesResourceLoader();
        if(overridesResourceLoader!=null)
            merged = mergePropertiesAgainstOverrides(merged,overridesResourceLoader);

        return merged;
    }

    /**
     * This method provided the functionality of overriding the currently read properties; against
     * those in a specific location that can be override by a operations team; without the need to modify the
     * application code.
     *
     * @param currentProperties The current properties that are to be override from the set of overrides
     *                          that are to be found by the overrides location's resource loader
     */
    protected Properties mergePropertiesAgainstOverrides(Properties currentProperties, ResourceLoader overridesLocationLoader) {
        Properties merged = new Properties();

        if(currentProperties!=null){
            for (final Object entry : currentProperties.keySet()) {
                final String key = entry.toString();
                merged.setProperty(key,currentProperties.getProperty(key));
            }
        }

        List<String> possibleOverrideFiles = new ArrayList<String>(getPossibleOverrideFiles());
        possibleOverrideFiles.add(0,getNameOfDefaultPropertiesFile());

        for(Resource file : loadFiles(possibleOverrideFiles,overridesLocationLoader)) {
            merged = mergeProperties(merged,load(file),strictMergingOfProperties);
        }

        return merged;
    }





    @Override
    public Properties getMergedProperties() {
        Properties mergedProperties = _getMergedProperties();
        Properties p = new Properties();
        for(Object key : mergedProperties.keySet()) {
            p.setProperty((String)key,mergedProperties.getProperty((String) key));
        }
        return p;
    }

    @Override
    public Map<String, String> getMergedPropertiesAsMap() {
        Properties mergedProperties = _getMergedProperties();
        return new HashMap(mergedProperties);
    }


    private boolean isOutputtingPropertiesInDebugMode() {
        return outputtingPropertiesInDebugMode;
    }

    private Pattern getSensitivePropertyMasker() {
        return sensitivePropertyMasker;
    }

    private String getNameOfDefaultPropertiesFile() {
        return nameOfDefaultPropertiesFile;
    }


    private List<String> getPossibleOverrideFiles() {
        return this.possibleOverrideFiles;
    }

    private ResourceLoader getResourceLoaderForLoadingConfigurationProperties() {
        return resourceLoaderForLoadingConfigurationProperties;
    }

    private boolean isStrictMergingOfProperties() {
        return strictMergingOfProperties;
    }

    private ResourceLoader getOperationalOverridesResourceLoader() {
        return operationalOverridesResourceLoader;
    }

    private Properties _getMergedProperties() {
        return mergedProperties;
    }


}
