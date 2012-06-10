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
package org.greencheek.utils.environment.propertyplaceholder.resolver.value;

import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentProperties;

/**
 * Value object that simplifies the creation of the VariablePlaceholderValueResolver object.
 * This class is not thread safe meaning it should be created on the same thread as when the @{link VariablePlaceholderValueResolver}
 * is created for which this class is needed to construct the resolver
 *
 * User: dominictootell
 * Date: 09/06/2012
 * Time: 16:28
 */
public class VariablePlaceholderValueResolverConfig implements ValueResolverConfig {
    private boolean systemPropertiesResolutionEnabled = ValueResolverConfig.DEFAULT_SYSTEM_PROPERTIES_RESOLUTION_ENABLED;
    private boolean environmentPropertiesResolutionEnabled = ValueResolverConfig.DEFAULT_ENVIRONMENT_PROPERTIES_RESOLUTION_ENABLED;
    private OperatingEnvironmentProperties operatingEnvironmentProperties = ValueResolverConfig.DEFAULT_OPERATING_ENVIRONMENT_PROPERTIES;
    private boolean ignoreUnresolvablePlaceholders = ValueResolverConfig.DEFAULT_IGNORE_UNRESOLVABLE_PLACEHOLDERS;
    private String placeholderPrefix = ValueResolverConfig.DEFAULT_PLACEHOLDER_PREFIX;
    private String placeholderSuffix = ValueResolverConfig.DEFAULT_PLACEHOLDER_SUFFIX;
    private String placeholderDefaultValueSeparator = ValueResolverConfig.DEFAULT_PLACEHOLDER_DEFAULT_VALUE_SEPARATOR;
    private boolean trimmingPropertyValues = ValueResolverConfig.DEFAULT_TRIMMING_PROPERTY_VALUES;


    @Override
    public boolean isSystemPropertiesResolutionEnabled() {
        return systemPropertiesResolutionEnabled;
    }

    @Override
    public ValueResolverConfig setSystemPropertiesResolutionEnabled(boolean defaultSystemPropertiesResolutionEnabled) {
        this.systemPropertiesResolutionEnabled = defaultSystemPropertiesResolutionEnabled;
        return this;
    }

    @Override
    public boolean isEnvironmentPropertiesResolutionEnabled() {
        return environmentPropertiesResolutionEnabled;
    }

    @Override
    public ValueResolverConfig setEnvironmentPropertiesResolutionEnabled(boolean defaultEnvironmentPropertiesResolutionEnabled) {
        this.environmentPropertiesResolutionEnabled = defaultEnvironmentPropertiesResolutionEnabled;
        return this;
    }

    @Override
    public OperatingEnvironmentProperties getOperatingEnvironmentProperties() {
        return operatingEnvironmentProperties;
    }

    @Override
    public ValueResolverConfig setOperatingEnvironmentProperties(OperatingEnvironmentProperties defaultOperatingEnvironmentProperties) {
        this.operatingEnvironmentProperties = defaultOperatingEnvironmentProperties;
        return this;
    }

    @Override
    public boolean isIgnoreUnresolvablePlaceholders() {
        return ignoreUnresolvablePlaceholders;
    }

    @Override
    public ValueResolverConfig setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders) {
        this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
        return this;
    }

    @Override
    public String getPlaceholderPrefix() {
        return placeholderPrefix;
    }

    @Override
    public ValueResolverConfig setPlaceholderPrefix(String placeholderPrefix) {
        this.placeholderPrefix = placeholderPrefix;
        return this;
    }

    @Override
    public String getPlaceholderSuffix() {
        return placeholderSuffix;
    }

    @Override
    public ValueResolverConfig setPlaceholderSuffix(String placeholderSuffix) {
        this.placeholderSuffix = placeholderSuffix;
        return this;
    }

    @Override
    public String getPlaceholderDefaultValueSeparator() {
        return placeholderDefaultValueSeparator;
    }

    @Override
    public ValueResolverConfig setPlaceholderDefaultValueSeparator(String placeholderDefaultValueSeparator) {
        this.placeholderDefaultValueSeparator = placeholderDefaultValueSeparator;
        return this;
    }

    @Override
    public boolean isTrimmingPropertyValues() {
        return trimmingPropertyValues;
    }

    @Override
    public ValueResolverConfig setTrimmingPropertyValues(boolean trimValues) {
        this.trimmingPropertyValues = trimValues;
        return this;
    }
}
