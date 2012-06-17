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
package org.greencheek.utils.environment.propertyplaceholder.builder;

import org.greencheek.utils.environment.propertyplaceholder.merger.PropertiesMerger;
import org.greencheek.utils.environment.propertyplaceholder.resolver.PropertiesResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.JavaPlatformOperatingEnvironmentVariableReader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentProperties;
import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentVariableReader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.*;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.ValueResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.VariablePlaceholderValueResolver;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * <p>
 * The PropertiesMerger is the class that is responsible for locating resources in the classpath and filesystem
 * and producing a Properties object that is a combination of:
 * <ol>
 *     <li>Default Application Properties</li>
 *     <li>Overrides based on environmental or System properties</li>
 *     <li>Default Operational Overrides</li>
 *     <li>Operational Overrides based on environmental or system properties</li>
 * </ol>
 * </p>
 * <p>
 *     This builder is meant for a single thread to construct the PropertiesMerger, that can then be used across multiple
 *     threads
 * </p>
 *
 * User: dominictootell
 * Date: 20/05/2012
 * Time: 13:31
 */
public interface PropertiesMergerBuilder {

    final static Pattern DEFAULT_SENSITIVE_PROPERTY_MASK = Pattern.compile(".*pass.*|.*secret.*",Pattern.CASE_INSENSITIVE);
    final static boolean DEFAULT_OUTPUTTING_PROPERTIES_IN_DEBUG_MODE = true;

    final static char DEFAULT_DELIMITER_USER_FOR_SEPARATING_SWITCHING_CONFIGURATION_VARIABLES = '.';
    final static String[] DEFAULT_VARIABLES_USED_FOR_SWITCHING_CONFIGURATION = new String[]{"ENV"};

    final static char DEFAULT_EXTENSION_SEPARATOR_CHAR_FOR_PROPERTIES_FILE = '.';
    final static String DEFAULT_EXTENSION_FOR_PROPERTIES_FILE = "properties";

    final static String DEFAULT_DEFAULT_PROPERTIES_FILENAME = "default";
    final static boolean DEFAULT_STRICT_MERGING_OF_PROPERTIES = true;
    final static String DEFAULT_RELATIVE_LOCATION_OF_FILES_OVERRIDE_DEFAULT = "environments/";
    final static String DEFAULT_OPERATIONAL_OVERRIDE_LOCATION = (System.getProperty("os.name")==null ||
                                                                 System.getProperty("os.name").toLowerCase().startsWith("win"))
                                                                ? "c:/data/opsoverrides/" : "/data/opsoverrides/" ;

    final static String DEFAULT_CONFIGURATION_LOCATION = "/config";
    final static ResourceLoader DEFAULT_RESOURCE_LOADER_FOR_LOADING_CONFIGURATION_PROPERTIES = new ClassPathResourceLoader(DEFAULT_CONFIGURATION_LOCATION);
    final static ResourceLoader DEFAULT_RESOURCE_LOADER_FOR_OPERATION_OVERRIDES = new FileSystemResourceLoader(DEFAULT_OPERATIONAL_OVERRIDE_LOCATION);
    final static ResourceLoaderFactory DEFAULT_RESOURCE_LOADER_FACTORY = new PrefixBasedResourceLoaderFactory();

    final static OperatingEnvironmentVariableReader DEFAULT_OPERATING_ENVIRONMENT_VARIABLE_READER = new JavaPlatformOperatingEnvironmentVariableReader();

    final static ValueResolver DEFAULT_PROPERTY_VALUE_RESOLVER = new VariablePlaceholderValueResolver();


    public PropertiesMergerBuilder setOutputtingPropertiesInDebugMode(boolean outputtingPropertiesInDebugMode);
    public boolean isOutputtingPropertiesInDebugMode();
    /**
     * The name of the application that is using the properties resolver.
     * This can be used when an implementation wishes to use the application name as a
     * location in which the operations department can store overrides for the for the application
     *
     * @param applicationName The name of the application, could be the tomcat name for instance
     * @return The builder object
     */
    public PropertiesMergerBuilder setApplicationName(String applicationName);
    public String getApplicationName();

    public PropertiesMergerBuilder setSensitivePropertyMasker(Pattern pattern);
    public Pattern getSensitivePropertyMasker();

    /**
     * Sets the resource loader from which to load the environmental properties.  This is the location
     * from which to load the generic properties, that are to be overridden by environmental specific properties.
     * See {@link #setLocationForLoadingConfigurationProperties(String)} for specifying the resource Loader via a
     * string.
     *
     * @param loader The resource loader responsible for loading application configuration resources, for the application
     *               defaults and then the environmental overrides.
     * @return
     */
    public PropertiesMergerBuilder setResourceLoaderForLoadingConfigurationProperties(ResourceLoader loader);

    /**
     * Sets the resource loader from which to load the environmental properties.  This is the location
     * from which to load the generic properties, that are to be overridden by environmental specific properties.
     * The location is specified via string.  This will be prefixed with a controlled volcabulary that is specific to the
     * implementation, an example would be "classpath:/xxx" or "filesystem:/xxx" to specify that the properties are
     * to be loaded from the classpath or filesystem.
     * string.
     *
     * @param location The location from which the application defaults and then the environmental overrides are read
     * @return
     */
    public PropertiesMergerBuilder setLocationForLoadingConfigurationProperties(String location);

    /**
     * Returns the resource loader that is responsible for loading the configuration properties, default and envrionmental
     * overrides for an application.
     *
     * @return The resource loader used to load application properties
     */
    public ResourceLoader getResourceLoaderForLoadingConfigurationProperties();

    /**
     * Sets the resource loader from which to load the environmental properties.  This is the location
     * from which to source operationally overridden properties, which can also be overridden on an environmental basis.
     * You would either choose to call this method or it's counter part (which might be easier)
     * @{link #setLocationForLoadingOperationalOverrides(String)}.
     *
     *
     * @param resourceLoader The resource loader responsible for loading application configuration resources from a
     *                       location that the operations teams can override values.
     * @return
     */
    public PropertiesMergerBuilder setResourceLoaderForOperationalOverrides(ResourceLoader resourceLoader);

    /**
     * Sets the resource loader from which to load the environmental properties.  This is the location
     * from which to source operationally overridden properties, which can also be overridden on an environmental basis.
     * The location is specified via string.  This will be prefixed with a controlled volcabulary that is specific to the
     * implementation, an example would be "classpath:/xxx" or "filesystem:/xxx" to specify that the properties are
     * to be loaded from the classpath or filesystem.
     * string.
     *
     * @param location The location from which the application defaults and then the environmental overrides are read
     * @return
     */
    public PropertiesMergerBuilder setLocationForLoadingOperationalOverrides(String location);

    /**
     * The resource loader that is used to read the operations teams overrides for an applications configuration
     * @return
     */
    public ResourceLoader getResourceLoaderForOperationalOverrides();

    /**
     * The location relative to {@link #setLocationForLoadingConfigurationProperties(String)} and {@link #setLocationForLoadingOperationalOverrides(String)}
     * from which to read the application properties, which override the defaults, that are source based on environmental
     * or system properties.
     *
     * @param relativeLocation The relative location in which application overrides based on environmental or system
     *                         properties are read.
     * @return A string representing a location relative to the Resource Loaders used for reading application properties
     * and operational overrides
     */
    public PropertiesMergerBuilder setRelativeLocationOfFilesOverridingDefaultProperties(String relativeLocation);

    /**
     * Returns the relative location, relative to the ResourceLoaders, of where the environment and system specific
     * property overrides are located
     * @return
     */
    public String getRelativeLocationOfFilesOverridingDefaultProperties();


    /**
     * Properties files usually have the extension: ".properties".  This gives you the ability to specify a different extension
     * DO NOT INCLUDE separator (i.e. '.')
     *
     * @param suffix the suffix for the properties files
     * @return
     */
    public PropertiesMergerBuilder setExtensionForPropertiesFile( String suffix);

    /**
     * Returns the extension used for the properties file
     * @return
     */
    public String getExtensionForPropertiesFile();


    /**
     * The separator for extension and the properties file name. i..e '.' in ".properties"
     * @param separator
     * @return
     */
    public PropertiesMergerBuilder setExtensionSeparatorCharForPropertiesFile(char separator);

    /**
     * Returns the separator.
     * @return
     */
    public char getExtensionSeparatorCharForPropertiesFile();

    /**
     * Name of the default properties file (this DOES NOT include the extension).  This is just the name
     *
     * @param defaultName
     * @return
     */
    public PropertiesMergerBuilder setNameOfDefaultPropertiesFile(String defaultName);

    /**
     * Name of the default properties file
     * @return
     */
    public String getNameOfDefaultPropertiesFile();

    /**
     *
     * @param strict
     * @return
     */
    public PropertiesMergerBuilder setStrictMergingOfProperties(boolean strict);
    public boolean isStrictMergingOfProperties();






    /**
     * Sets the list of variables on which the relevant set of resources (properties)
     * will be sourced.  I.e.  USER, LANG, or more likely on a production platform : ENV, os.arch.
     * This means that the properties resolver would look for a default file (i.e. default.props),
     * and then for a properties file: <ENV>.properties (i.e. production.properties) to merge on top of the default
     * properties.  Then it would look for <ENV>.<os.arch>.properties (i.e. production.x86_64.properties)
     * to merge on top of those previous properties.
     *
     * [
     *   [ ENV ],
     *   [ ENV , os.arch ]
     * ]
     *
     * @param var A List of a list of environment/system properties, against which to look for override files.
     * @return The builder
     */
    public PropertiesMergerBuilder setVariablesUsedForSwitchingConfiguration(List<List<String>> var);

    /**
     * Sets the list of variables on which the relevant set of resources (properties), via an array of comma separated
     * String values: [ "ENV", "ENV,os.arch" ], will be sourced.
     * This means that the properties resolver would look for a default file (i.e. default.props),
     * and then for a properties file: <ENV>.properties (i.e. production.properties) to merge on top of the default
     * properties.  Then it would look for <ENV>.<os.arch>.properties (i.e. production.x86_64.properties)
     * to merge on top of those previous properties.
     *
     * @param csv A List of a list of environment/system properties, against which to look for override files.
     * @return The builder
     */
    public PropertiesMergerBuilder setVariablesUsedForSwitchingConfiguration(String[] csv);

    /**
     * The array of variables the properties (resources), will be sourced/overridden sequentially against.
     * For a call to @link {#setVariablesUsedForSwitchingConfiguration(new String[]{"ENV","ENV,os.arch"})} the returned
     * value would be:
     * [
     *   [ "ENV" ],
     *   [ "ENV", "os.arch" ]
     * ]
     *
     * @return
     */
    public String[][] getVariablesUsedForSwitchingConfiguration();



    public PropertiesMergerBuilder setDelimiterUsedForSeparatingSwitchingConfigurationVariables(char c);
    public char getDelimiterUsedForSeparatingSwitchingConfigurationVariables();



    public PropertiesMergerBuilder setOperatingEnvironmentVariableReader(OperatingEnvironmentVariableReader variableReader);
    public OperatingEnvironmentVariableReader getOperatingEnvironmentVariableReader();





    public PropertiesMerger build();
    public Properties buildProperties();
}
