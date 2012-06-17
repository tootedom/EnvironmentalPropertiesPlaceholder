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

import org.greencheek.utils.environment.propertyplaceholder.merger.EnvironmentSpecificPropertiesMerger;
import org.greencheek.utils.environment.propertyplaceholder.merger.PropertiesMerger;
import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentVariableReader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ResourceLoader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ResourceLoaderFactory;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * User: dominictootell
 * Date: 20/05/2012
 * Time: 13:32
 */
public class EnvironmentSpecificPropertiesMergerBuilder implements PropertiesMergerBuilder {

    private Pattern sensitiveProperyMasker = DEFAULT_SENSITIVE_PROPERTY_MASK;
    private boolean outputtingPropertiesInDebugMode = DEFAULT_OUTPUTTING_PROPERTIES_IN_DEBUG_MODE;

    private boolean strictMergingOfProperties = DEFAULT_STRICT_MERGING_OF_PROPERTIES;
    private String nameOfDefaultPropertiesFile = DEFAULT_DEFAULT_PROPERTIES_FILENAME;

    private char delimiterUsedForSeparatingSwitchingConfigurationVariables = DEFAULT_DELIMITER_USER_FOR_SEPARATING_SWITCHING_CONFIGURATION_VARIABLES;
    private char extensionSeparatorCharForPropertiesFile = DEFAULT_EXTENSION_SEPARATOR_CHAR_FOR_PROPERTIES_FILE;
    private String extensionForPropertiesFile =  DEFAULT_EXTENSION_FOR_PROPERTIES_FILE;
    private String relativeLocationOfFilesOverridingDefaultProperties = DEFAULT_RELATIVE_LOCATION_OF_FILES_OVERRIDE_DEFAULT;

    private String[][] variablesUsedForSwitchingPropertyFiles = new String[0][];

    private String applicationName;

    private OperatingEnvironmentVariableReader operatingEnvironmentVariableReader = DEFAULT_OPERATING_ENVIRONMENT_VARIABLE_READER;

    private ResourceLoader resourceLoaderForLoadingConfigurationProperties;
    private ResourceLoader resourceLoaderForOperationalOverrides = DEFAULT_RESOURCE_LOADER_FOR_OPERATION_OVERRIDES;

    private ResourceLoaderFactory resourceLoaderFactory = DEFAULT_RESOURCE_LOADER_FACTORY;

    public EnvironmentSpecificPropertiesMergerBuilder() {
        this(DEFAULT_RESOURCE_LOADER_FOR_LOADING_CONFIGURATION_PROPERTIES);
    }

    public EnvironmentSpecificPropertiesMergerBuilder(String locationOfConfiguration) {
        this(DEFAULT_RESOURCE_LOADER_FACTORY.createResourceLoader(locationOfConfiguration));
    }

    public EnvironmentSpecificPropertiesMergerBuilder(ResourceLoaderFactory resourceLoaderFactory) {
        this(resourceLoaderFactory,DEFAULT_RESOURCE_LOADER_FOR_LOADING_CONFIGURATION_PROPERTIES);
    }

    public EnvironmentSpecificPropertiesMergerBuilder(ResourceLoader locationOfConfiguration) {
        this(DEFAULT_RESOURCE_LOADER_FACTORY,locationOfConfiguration);
    }

    public EnvironmentSpecificPropertiesMergerBuilder(ResourceLoaderFactory resourceLoaderFactory, ResourceLoader locationOfConfiguration) {
        this.resourceLoaderFactory = resourceLoaderFactory;
        this.resourceLoaderForLoadingConfigurationProperties = locationOfConfiguration;
        this.setVariablesUsedForSwitchingConfiguration(DEFAULT_VARIABLES_USED_FOR_SWITCHING_CONFIGURATION);
    }





    @Override
    public PropertiesMergerBuilder setOutputtingPropertiesInDebugMode(boolean outputtingPropertiesInDebugMode) {
        this.outputtingPropertiesInDebugMode = outputtingPropertiesInDebugMode;
        return this;
    }

    @Override
    public boolean isOutputtingPropertiesInDebugMode() {
        return outputtingPropertiesInDebugMode;
    }

    @Override
    public PropertiesMergerBuilder setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;

    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public PropertiesMergerBuilder setSensitivePropertyMasker(Pattern pattern) {
        sensitiveProperyMasker = pattern;
        return this;
    }

    @Override
    public Pattern getSensitivePropertyMasker() {
        return this.sensitiveProperyMasker;
    }


    @Override
    public PropertiesMergerBuilder setResourceLoaderForOperationalOverrides(ResourceLoader resourceLoader) {
        this.resourceLoaderForOperationalOverrides = resourceLoader;
        return this;
    }

    @Override
    public PropertiesMergerBuilder setLocationForLoadingOperationalOverrides(String location) {
        this.resourceLoaderForOperationalOverrides = resourceLoaderFactory.createResourceLoader(location);
        return this;
    }

    @Override
    public ResourceLoader getResourceLoaderForOperationalOverrides() {
        return this.resourceLoaderForOperationalOverrides;
    }

    @Override
    public PropertiesMergerBuilder setResourceLoaderForLoadingConfigurationProperties(ResourceLoader loader) {
        this.resourceLoaderForLoadingConfigurationProperties = loader;
        return this;
    }

    @Override
    public PropertiesMergerBuilder setLocationForLoadingConfigurationProperties(String location) {
        this.resourceLoaderForLoadingConfigurationProperties = resourceLoaderFactory.createResourceLoader(location);
        return this;
    }

    @Override
    public ResourceLoader getResourceLoaderForLoadingConfigurationProperties() {
        return this.resourceLoaderForLoadingConfigurationProperties;
    }

    @Override
    public PropertiesMergerBuilder setExtensionForPropertiesFile(String extensionForPropertiesFile) {
        if(extensionForPropertiesFile.indexOf('.')==0) {
            this.extensionForPropertiesFile = extensionForPropertiesFile.substring(1);
        } else {
            this.extensionForPropertiesFile = extensionForPropertiesFile;
        }
        return this;
    }

    @Override
    public String getExtensionForPropertiesFile() {
        return extensionForPropertiesFile;
    }

    @Override
    public PropertiesMergerBuilder setExtensionSeparatorCharForPropertiesFile(char separator) {
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
    public PropertiesMergerBuilder setVariablesUsedForSwitchingConfiguration(String[] environmentalSwitchingSourcesAsCSV) {
        if(environmentalSwitchingSourcesAsCSV==null || environmentalSwitchingSourcesAsCSV.length==0) return this;

        this.variablesUsedForSwitchingPropertyFiles = new String[environmentalSwitchingSourcesAsCSV.length][];
        int i = 0;
        for(String variableList : environmentalSwitchingSourcesAsCSV) {
            this.variablesUsedForSwitchingPropertyFiles[i++] = variableList.split(",");
        }
        return this;
    }

    @Override
    public PropertiesMergerBuilder setVariablesUsedForSwitchingConfiguration(List<List<String>> environmentalSwitchingSources) {
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

            System.arraycopy(variablesUsedForSwitchingPropertyFiles[i], 0, copy[i], 0, variablesUsedForSwitchingPropertyFiles[i].length);
        }
        return copy;
    }



    @Override
    public PropertiesMergerBuilder setDelimiterUsedForSeparatingSwitchingConfigurationVariables(char c) {
        delimiterUsedForSeparatingSwitchingConfigurationVariables = c;
        return this;
    }

    @Override
    public char getDelimiterUsedForSeparatingSwitchingConfigurationVariables() {
        return delimiterUsedForSeparatingSwitchingConfigurationVariables;
    }

    @Override
    public PropertiesMergerBuilder setOperatingEnvironmentVariableReader(OperatingEnvironmentVariableReader variableReader) {
        this.operatingEnvironmentVariableReader = variableReader;
        return this;
    }

    @Override
    public OperatingEnvironmentVariableReader getOperatingEnvironmentVariableReader() {
        return operatingEnvironmentVariableReader;
    }


    @Override
    public PropertiesMerger build() {
        return new EnvironmentSpecificPropertiesMerger(this);
    }

    @Override
    public Properties buildProperties() {
        return build().getMergedProperties();
    }



    @Override
    public PropertiesMergerBuilder setNameOfDefaultPropertiesFile(String defaultName) {
       this.nameOfDefaultPropertiesFile = defaultName;
       return this;
    }

    @Override
    public String getNameOfDefaultPropertiesFile() {
        return nameOfDefaultPropertiesFile;
    }

    @Override
    public PropertiesMergerBuilder setStrictMergingOfProperties(boolean strict) {
        this.strictMergingOfProperties = strict;
        return this;
    }

    @Override
    public boolean isStrictMergingOfProperties() {
        return strictMergingOfProperties;
    }


    @Override
    public PropertiesMergerBuilder setRelativeLocationOfFilesOverridingDefaultProperties(String relativeLocation) {
        if(!relativeLocation.endsWith("/")) relativeLocation = relativeLocation + "/";

        this.relativeLocationOfFilesOverridingDefaultProperties = relativeLocation;

        return this;
    }

    @Override
    public String getRelativeLocationOfFilesOverridingDefaultProperties() {
        return this.relativeLocationOfFilesOverridingDefaultProperties;
    }


}

