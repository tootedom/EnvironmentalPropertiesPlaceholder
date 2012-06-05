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

import org.greencheek.utils.environment.propertyplaceholder.resolver.EnvironmentSpecificPropertiesResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.PropertiesResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentVariableReader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ResourceLoader;

import java.util.List;
import java.util.regex.Pattern;

/**
 * User: dominictootell
 * Date: 20/05/2012
 * Time: 13:32
 */
public class EnvironmentSpecificPropertiesResolverBuilder implements PropertiesResolverBuilder {

    private Pattern sensitiveProperyMasker = DEFAULT_SENSITIVE_PROPERTY_MASK;
    private boolean outputtingPropertiesInDebugMode = DEFAULT_OUTPUTTING_PROPERTIES_IN_DEBUG_MODE;
    private boolean trimmingPropertyValues= DEFAULT_TRIMMING_PROPERTY_VALUES ;
    private boolean resolvingSystemProperties = DEFAULT_SYSTEM_PROPERTIES_RESOLUTION_ENABLED;
    private boolean resolvingEnvironmentVariables = DEFAULT_ENVIRONMENT_PROPERTIES_RESOLUTION_ENABLED;
    private boolean strictMergingOfProperties = DEFAULT_STRICT_MERGING_OF_PROPERTIES;
    private String nameOfDefaultPropertiesFile = DEFAULT_DEFAULT_PROPERTIES_FILENAME;

    private char delimiterUsedForSeparatingSwitchingConfigurationVariables = DEFAULT_DELIMITER_USER_FOR_SEPARATING_SWITCHING_CONFIGURATION_VARIABLES;
    private char extensionSeparatorCharForPropertiesFile = DEFAULT_EXTENSION_SEPARATOR_CHAR_FOR_PROPERTIES_FILE;
    private String extensionForPropertiesFile =  DEFAULT_EXTENSION_FOR_PROPERTIES_FILE;
    private String relativeLocationOfFilesOverridingDefaultProperties = DEFAULT_RELATIVE_LOCATION_OF_FILES_OVERRIDE_DEFAULT;

    private String[][] variablesUsedForSwitchingPropertyFiles = new String[0][];

    private String applicationName;

    private OperatingEnvironmentVariableReader operatingEnvironmentVariableReader;

    private ResourceLoader resourceLoaderForLoadingConfigurationProperties = DEFAULT_RESOURCE_LOADER_FOR_LOADING_CONFIGURATION_PROPERTIES;
    private ResourceLoader operationalOverridesResourceLoader = DEFAULT_RESOURCE_LOADER_FOR_OPERATION_OVERRIDES;

    public EnvironmentSpecificPropertiesResolverBuilder(ResourceLoader locationOfConfiguration) {
        this.resourceLoaderForLoadingConfigurationProperties = locationOfConfiguration;
    }


    @Override
    public PropertiesResolverBuilder setOutputtingPropertiesInDebugMode(boolean outputtingPropertiesInDebugMode) {
        this.outputtingPropertiesInDebugMode = outputtingPropertiesInDebugMode;
        return this;
    }

    @Override
    public boolean isOutputtingPropertiesInDebugMode() {
        return outputtingPropertiesInDebugMode;
    }

    @Override
    public PropertiesResolverBuilder setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;

    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public PropertiesResolverBuilder setSensitivePropertyMasker(Pattern pattern) {
        sensitiveProperyMasker = pattern;
        return this;
    }

    @Override
    public Pattern getSensitivePropertyMasker() {
        return this.sensitiveProperyMasker;
    }


    @Override
    public PropertiesResolverBuilder setOperationalOverridesResourceLoader(ResourceLoader resourceLoader) {
        this.operationalOverridesResourceLoader = resourceLoader;
        return this;
    }

    @Override
    public ResourceLoader getOperationalOverridesResourceLoader() {
        return this.operationalOverridesResourceLoader;
    }

    @Override
    public PropertiesResolverBuilder setResourceLoaderForLoadingConfigurationProperties(ResourceLoader loader) {
        this.resourceLoaderForLoadingConfigurationProperties = loader;
        return this;
    }

    @Override
    public ResourceLoader getResourceLoaderForLoadingConfigurationProperties() {
        return this.resourceLoaderForLoadingConfigurationProperties;
    }

    @Override
    public PropertiesResolverBuilder setTrimmingPropertyValues(boolean trimValues) {
        this.trimmingPropertyValues = trimValues;
        return this;
    }

    @Override
    public boolean isTrimmingPropertyValues() {
        return this.trimmingPropertyValues;
    }

    @Override
    public PropertiesResolverBuilder setResolvingSystemProperties(boolean useSystemPropeties) {
        this.resolvingSystemProperties = useSystemPropeties;
        return this;
    }

    @Override
    public boolean isResolvingSystemProperties() {
        return this.resolvingSystemProperties;
    }

    @Override
    public PropertiesResolverBuilder setResolvingEnvironmentVariables(boolean useEnvironmentVariables) {
        this.resolvingEnvironmentVariables = useEnvironmentVariables;
        return this;
    }

    @Override
    public boolean isResolvingEnvrionmentVariables() {
        return this.resolvingEnvironmentVariables;
    }

    @Override
    public PropertiesResolverBuilder setExtensionForPropertiesFile(String extensionForPropertiesFile) {
        this.extensionForPropertiesFile = extensionForPropertiesFile;
        return this;
    }

    @Override
    public String getExtensionForPropertiesFile() {
        return extensionForPropertiesFile;
    }

    @Override
    public PropertiesResolverBuilder setExtensionSeparatorCharForPropertiesFile(char separator) {
        this.extensionSeparatorCharForPropertiesFile = separator;
        return this;
    }

    @Override
    public char getExtensionSeparatorCharForPropertiesFile() {
        return extensionSeparatorCharForPropertiesFile;
    }

    /**
     Specified in the format: String[] {"SERVER_ENV","SERVER_ENV,TARGET_ENV"}
            ]
    */
    @Override
    public PropertiesResolverBuilder setVariablesUsedForSwitchingConfiguration(String[] environmentalSwitchingSourcesAsCSV) {
        if(environmentalSwitchingSourcesAsCSV==null || environmentalSwitchingSourcesAsCSV.length==0) return this;

        this.variablesUsedForSwitchingPropertyFiles = new String[environmentalSwitchingSourcesAsCSV.length][];
        int i = 0;
        for(String variableList : environmentalSwitchingSourcesAsCSV) {
            this.variablesUsedForSwitchingPropertyFiles[i++] = variableList.split(",");
        }
        return this;
    }

    @Override
    public PropertiesResolverBuilder setVariablesUsedForSwitchingConfiguration(List<List<String>> environmentalSwitchingSources) {
        if(environmentalSwitchingSources.size()==0) return this;

        this.variablesUsedForSwitchingPropertyFiles = new String[environmentalSwitchingSources.size()][];
        int i=0;
        for(List<String> vars : environmentalSwitchingSources) {
            variablesUsedForSwitchingPropertyFiles[i++] = vars.toArray(new String[vars.size()]);
        }

        return this;
    }

    @Override
    public String[][] getVariablesUsedForSwitchingConfiguration() {
        String[][] copy = new String[variablesUsedForSwitchingPropertyFiles.length][];

        for (int i = 0; i < variablesUsedForSwitchingPropertyFiles.length; i++) {
            copy[i] = new String[variablesUsedForSwitchingPropertyFiles[i].length];

            for (int j = 0; j < variablesUsedForSwitchingPropertyFiles[i].length; j++) {
                copy[i][j] = variablesUsedForSwitchingPropertyFiles[i][j];
            }
        }
        return copy;
    }



    @Override
    public PropertiesResolverBuilder setDelimiterUsedForSeparatingSwitchingConfigurationVariables(char c) {
        delimiterUsedForSeparatingSwitchingConfigurationVariables = c;
        return this;
    }

    @Override
    public char getDelimiterUsedForSeparatingSwitchingConfigurationVariables() {
        return delimiterUsedForSeparatingSwitchingConfigurationVariables;
    }

    @Override
    public PropertiesResolverBuilder setOperatingEnvironmentVariableReader(OperatingEnvironmentVariableReader variableReader) {
        this.operatingEnvironmentVariableReader = variableReader;
        return this;
    }

    @Override
    public OperatingEnvironmentVariableReader getOperatingEnvironmentVariableReader() {
        return operatingEnvironmentVariableReader;
    }

    @Override
    public PropertiesResolver build() {
        return new EnvironmentSpecificPropertiesResolver(this);
    }




    @Override
    public PropertiesResolverBuilder setNameOfDefaultPropertiesFile(String defaultName) {
        this.nameOfDefaultPropertiesFile = defaultName;
        return this;
    }

    @Override
    public String getNameOfDefaultPropertiesFile() {
        return nameOfDefaultPropertiesFile;
    }

    @Override
    public PropertiesResolverBuilder setStrictMergingOfProperties(boolean strict) {
        this.strictMergingOfProperties = strict;
        return this;
    }

    @Override
    public boolean isStrictMergingOfProperties() {
        return strictMergingOfProperties;
    }


    @Override
    public PropertiesResolverBuilder setRelativeLocationOfFilesOverridingDefaultProperties(String relativeLocation) {
        if(!relativeLocation.endsWith("/")) relativeLocation = relativeLocation + "/";

        this.relativeLocationOfFilesOverridingDefaultProperties = relativeLocation;

        return this;
    }

    @Override
    public String getRelativeLocationOfFilesOverridingDefaultProperties() {
        return this.relativeLocationOfFilesOverridingDefaultProperties;
    }


}

