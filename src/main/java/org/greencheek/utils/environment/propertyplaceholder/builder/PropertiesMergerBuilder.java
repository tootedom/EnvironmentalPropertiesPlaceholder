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
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ClassPathResourceLoader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.FileSystemResourceLoader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ResourceLoader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.ValueResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.VariablePlaceholderValueResolver;

import java.util.List;
import java.util.regex.Pattern;

/**
 * User: dominictootell
 * Date: 20/05/2012
 * Time: 13:31
 */
public interface PropertiesMergerBuilder {

    final static Pattern DEFAULT_SENSITIVE_PROPERTY_MASK = Pattern.compile(".*pass.*|.*secret.*",Pattern.CASE_INSENSITIVE);
    final static boolean DEFAULT_OUTPUTTING_PROPERTIES_IN_DEBUG_MODE = true;

    final static char DEFAULT_DELIMITER_USER_FOR_SEPARATING_SWITCHING_CONFIGURATION_VARIABLES = '.';

    final static char DEFAULT_EXTENSION_SEPARATOR_CHAR_FOR_PROPERTIES_FILE = '.';
    final static String DEFAULT_EXTENSION_FOR_PROPERTIES_FILE = "properties";

    final static String DEFAULT_DEFAULT_PROPERTIES_FILENAME = "default";
    final static boolean DEFAULT_STRICT_MERGING_OF_PROPERTIES = true;
    final static String DEFAULT_RELATIVE_LOCATION_OF_FILES_OVERRIDE_DEFAULT = "environments";
    final static String DEFAULT_OPERATIONAL_OVERRIDE_LOCATION = "/data/opsoverrides/";
    final static String DEFAULT_CONFIGURATION_LOCATION = "/config";
    final static ResourceLoader DEFAULT_RESOURCE_LOADER_FOR_LOADING_CONFIGURATION_PROPERTIES = new ClassPathResourceLoader(DEFAULT_CONFIGURATION_LOCATION);
    final static ResourceLoader DEFAULT_RESOURCE_LOADER_FOR_OPERATION_OVERRIDES = new FileSystemResourceLoader(DEFAULT_OPERATIONAL_OVERRIDE_LOCATION);

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

    public PropertiesMergerBuilder setOperationalOverridesResourceLoader(ResourceLoader resourceLoader);
    public ResourceLoader getOperationalOverridesResourceLoader();

    public PropertiesMergerBuilder setResourceLoaderForLoadingConfigurationProperties(ResourceLoader loader);
    public ResourceLoader getResourceLoaderForLoadingConfigurationProperties();


    public PropertiesMergerBuilder setExtensionForPropertiesFile( String suffix);
    public String getExtensionForPropertiesFile();


    public PropertiesMergerBuilder setExtensionSeparatorCharForPropertiesFile(char separator);
    public char getExtensionSeparatorCharForPropertiesFile();


    public PropertiesMergerBuilder setNameOfDefaultPropertiesFile(String defaultName);
    public String getNameOfDefaultPropertiesFile();

    public PropertiesMergerBuilder setStrictMergingOfProperties(boolean strict);
    public boolean isStrictMergingOfProperties();






    /**
     * Sets the list of variables on which the relevant set of resources (properties)
     * will be sourced.  I.e.  USER, LANG, or more likely on a production platform : ENV, os.arch.
     * This means that the properties resolver would look for a default file (i.e. default.properties),
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
     * This means that the properties resolver would look for a default file (i.e. default.properties),
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

    public PropertiesMergerBuilder setRelativeLocationOfFilesOverridingDefaultProperties(String relativeLocation);
    public String getRelativeLocationOfFilesOverridingDefaultProperties();

    public PropertiesMergerBuilder setDelimiterUsedForSeparatingSwitchingConfigurationVariables(char c);
    public char getDelimiterUsedForSeparatingSwitchingConfigurationVariables();



    public PropertiesMergerBuilder setOperatingEnvironmentVariableReader(OperatingEnvironmentVariableReader variableReader);
    public OperatingEnvironmentVariableReader getOperatingEnvironmentVariableReader();


    public PropertiesMerger build();
}
