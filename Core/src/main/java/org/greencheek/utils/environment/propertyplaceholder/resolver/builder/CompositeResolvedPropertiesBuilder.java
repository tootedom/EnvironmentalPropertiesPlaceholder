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
package org.greencheek.utils.environment.propertyplaceholder.resolver.builder;

import org.greencheek.utils.environment.propertyplaceholder.builder.EnvironmentSpecificPropertiesMergerBuilder;
import org.greencheek.utils.environment.propertyplaceholder.builder.EnvironmentSpecificPropertiesResolverBuilder;
import org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesMergerBuilder;
import org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesResolverBuilder;
import org.greencheek.utils.environment.propertyplaceholder.merger.PropertiesMerger;
import org.greencheek.utils.environment.propertyplaceholder.resolver.PropertiesResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentVariableReader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ResourceLoader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.ValueResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.VariablePlaceholderValueResolver;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * User: dominictootell
 * Date: 10/06/2012
 * Time: 19:04
 */
public class CompositeResolvedPropertiesBuilder
        implements ResolvedPropertiesBuilder, PropertiesMergerBuilder,PropertiesResolverBuilder
{
    private final PropertiesMergerBuilder mergerBuilder;
    private final PropertiesResolverBuilder resolverBuilder;


    private static ValueResolver DEFAULT_VALUE_RESOLVER() {
        try {
            return DEFAULT_VALUE_RESOLVER.newInstance();
        } catch (Exception e) {
            return new VariablePlaceholderValueResolver();
        }
    }

    private static PropertiesMergerBuilder DEFAULT_PROPERTIES_MERGER_BUILDER() {
        try {
            return DEFAULT_PROPERTIES_MERGER_BUILDER.newInstance();
        } catch (Exception e) {
            return new EnvironmentSpecificPropertiesMergerBuilder();
        }
    }


    private static PropertiesResolverBuilder DEFAULT_PROPERTIES_RESOLVER_BUILDER() {
        try {
            return DEFAULT_PROPERTIES_RESOLVER_BUILDER.newInstance();
        } catch (Exception e) {
            return new EnvironmentSpecificPropertiesResolverBuilder();
        }
    }

    public CompositeResolvedPropertiesBuilder() {
        this(DEFAULT_VALUE_RESOLVER(),
             DEFAULT_PROPERTIES_MERGER_BUILDER(),
             DEFAULT_PROPERTIES_RESOLVER_BUILDER());
    }

    public CompositeResolvedPropertiesBuilder(ValueResolver resolver) {
        this(resolver,
                DEFAULT_PROPERTIES_MERGER_BUILDER(),
                DEFAULT_PROPERTIES_RESOLVER_BUILDER());
    }

    public CompositeResolvedPropertiesBuilder(ValueResolver valueResolver,
                                              PropertiesMergerBuilder mergerBuilder,
                                              PropertiesResolverBuilder resolverBuilder) {
        this.mergerBuilder = mergerBuilder;
        this.resolverBuilder = resolverBuilder;
        resolverBuilder.setPropertyValueResolver(valueResolver);
    }

    @Override
    public PropertiesMergerBuilder setOutputtingPropertiesInDebugMode(boolean outputtingPropertiesInDebugMode) {
        mergerBuilder.setOutputtingPropertiesInDebugMode(outputtingPropertiesInDebugMode);
        return this;
    }

    @Override
    public boolean isOutputtingPropertiesInDebugMode() {
        return mergerBuilder.isOutputtingPropertiesInDebugMode();
    }

    @Override
    public PropertiesMergerBuilder setApplicationName(String applicationName) {
        mergerBuilder.setApplicationName(applicationName);
        return this;
    }

    @Override
    public String getApplicationName() {
        return mergerBuilder.getApplicationName();
    }

    @Override
    public PropertiesMergerBuilder setSensitivePropertyMasker(Pattern pattern) {
        mergerBuilder.setSensitivePropertyMasker(pattern);
        return this;
    }

    @Override
    public Pattern getSensitivePropertyMasker() {
        return mergerBuilder.getSensitivePropertyMasker();
    }

    @Override
    public PropertiesMergerBuilder setResourceLoaderForOperationalOverrides(ResourceLoader resourceLoader) {
        mergerBuilder.setResourceLoaderForOperationalOverrides(resourceLoader);
        return this;
    }

    @Override
    public PropertiesMergerBuilder setLocationForLoadingOperationalOverrides(String location) {
        return mergerBuilder.setLocationForLoadingOperationalOverrides(location);
    }

    @Override
    public ResourceLoader getResourceLoaderForOperationalOverrides() {
        return mergerBuilder.getResourceLoaderForOperationalOverrides();
    }

    @Override
    public PropertiesMergerBuilder setResourceLoaderForLoadingConfigurationProperties(ResourceLoader loader) {
        mergerBuilder.setResourceLoaderForLoadingConfigurationProperties(loader);
        return this;
    }

    @Override
    public PropertiesMergerBuilder setLocationForLoadingConfigurationProperties(String location) {
        return mergerBuilder.setLocationForLoadingConfigurationProperties(location);
    }

    @Override
    public ResourceLoader getResourceLoaderForLoadingConfigurationProperties() {
        return mergerBuilder.getResourceLoaderForLoadingConfigurationProperties();
    }

    @Override
    public PropertiesMergerBuilder setExtensionForPropertiesFile(String suffix) {
        mergerBuilder.setExtensionForPropertiesFile(suffix);
        return this;
    }

    @Override
    public String getExtensionForPropertiesFile() {
        return mergerBuilder.getExtensionForPropertiesFile();
    }

    @Override
    public PropertiesMergerBuilder setExtensionSeparatorCharForPropertiesFile(char separator) {
        mergerBuilder.setExtensionSeparatorCharForPropertiesFile(separator);
        return this;
    }

    @Override
    public char getExtensionSeparatorCharForPropertiesFile() {
        return mergerBuilder.getExtensionSeparatorCharForPropertiesFile();
    }

    @Override
    public PropertiesMergerBuilder setNameOfDefaultPropertiesFile(String defaultName) {
        mergerBuilder.setNameOfDefaultPropertiesFile(defaultName);
        return this;
    }

    @Override
    public String getNameOfDefaultPropertiesFile() {
        return mergerBuilder.getNameOfDefaultPropertiesFile();
    }

    @Override
    public PropertiesMergerBuilder setStrictMergingOfProperties(boolean strict) {
        mergerBuilder.setStrictMergingOfProperties(strict);
        return this;
    }

    @Override
    public boolean isStrictMergingOfProperties() {
        return mergerBuilder.isStrictMergingOfProperties();
    }

    @Override
    public PropertiesMergerBuilder setVariablesUsedForSwitchingConfiguration(List<List<String>> var) {
        mergerBuilder.setVariablesUsedForSwitchingConfiguration(var);
        return this;
    }

    @Override
    public PropertiesMergerBuilder setVariablesUsedForSwitchingConfiguration(String[] csv) {
        mergerBuilder.setVariablesUsedForSwitchingConfiguration(csv);
        return this;
    }

    @Override
    public String[][] getVariablesUsedForSwitchingConfiguration() {
        return mergerBuilder.getVariablesUsedForSwitchingConfiguration();
    }

    @Override
    public PropertiesMergerBuilder setRelativeLocationOfFilesOverridingDefaultProperties(String relativeLocation) {
        mergerBuilder.setRelativeLocationOfFilesOverridingDefaultProperties(relativeLocation);
        return this;
    }

    @Override
    public String getRelativeLocationOfFilesOverridingDefaultProperties() {
        return mergerBuilder.getRelativeLocationOfFilesOverridingDefaultProperties();
    }

    @Override
    public PropertiesMergerBuilder setDelimiterUsedForSeparatingSwitchingConfigurationVariables(char c) {
        mergerBuilder.setDelimiterUsedForSeparatingSwitchingConfigurationVariables(c);
        return this;
    }

    @Override
    public char getDelimiterUsedForSeparatingSwitchingConfigurationVariables() {
        return mergerBuilder.getDelimiterUsedForSeparatingSwitchingConfigurationVariables();
    }

    @Override
    public PropertiesMergerBuilder setOperatingEnvironmentVariableReader(OperatingEnvironmentVariableReader variableReader) {
        mergerBuilder.setOperatingEnvironmentVariableReader(variableReader);
        return this;
    }

    @Override
    public OperatingEnvironmentVariableReader getOperatingEnvironmentVariableReader() {
        return mergerBuilder.getOperatingEnvironmentVariableReader();
    }

    @Override
    public PropertiesMerger build() {
        return mergerBuilder.build();
    }

    @Override
    public Properties buildProperties() {
        return mergerBuilder.buildProperties();
    }

    @Override
    public PropertiesResolver build(PropertiesMerger mergedPropertiesLoader) {
        return resolverBuilder.build(build());
    }

    @Override
    public Properties buildProperties(PropertiesMerger mergedPropertiesLoader) {
        return resolverBuilder.buildProperties(build());
    }

    @Override
    public Properties buildProperties(PropertiesMergerBuilder builder) {
        return resolverBuilder.buildProperties(builder);

    }


    @Override
    public PropertiesResolverBuilder setPropertyValueResolver(ValueResolver resolver) {
        resolverBuilder.setPropertyValueResolver(resolver);
        return this;
    }

    /**
     * @return
     */
    @Override
    public ValueResolver getValueResolver() {
        return resolverBuilder.getValueResolver();
    }

    @Override
    public PropertiesResolverBuilder setTrimmingPropertyValues(boolean b) {
        resolverBuilder.setTrimmingPropertyValues(b);
        return this;
    }

    @Override
    public boolean isTrimmingPropertyValues() {
        return resolverBuilder.isTrimmingPropertyValues();
    }

    @Override
    public Properties buildResolvedProperties() {
        return resolverBuilder.buildProperties(build());
    }

}
