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

import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.JavaPlatformOperatingEnvironmentProperties;
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
public class VariablePlaceholderValueResolverConfig
{
    private boolean systemPropertiesResolutionEnabled = ValueResolver.DEFAULT_SYSTEM_PROPERTIES_RESOLUTION_ENABLED;
    private boolean environmentPropertiesResolutionEnabled = ValueResolver.DEFAULT_ENVIRONMENT_PROPERTIES_RESOLUTION_ENABLED;
    private OperatingEnvironmentProperties operatingEnvironmentProperties = ValueResolver.DEFAULT_OPERATING_ENVIRONMENT_PROPERTIES;
    private boolean ignoreUnresolvablePlaceholders = ValueResolver.DEFAULT_IGNORE_UNRESOLVABLE_PLACEHOLDERS;
    private String placeholderPrefix = ValueResolver.DEFAULT_PLACEHOLDER_PREFIX;
    private String placeholderSuffix = ValueResolver.DEFAULT_PLACEHOLDER_SUFFIX;
    private String placeholderDefaultValueSeparator = ValueResolver.DEFAULT_PLACEHOLDER_DEFAULT_VALUE_SEPARATOR;
    private boolean trimValues = ValueResolver.DEFAULT_TRIM_VALUES;


    public boolean isSystemPropertiesResolutionEnabled() {
        return systemPropertiesResolutionEnabled;
    }

    public VariablePlaceholderValueResolverConfig setSystemPropertiesResolutionEnabled(boolean defaultSystemPropertiesResolutionEnabled) {
        this.systemPropertiesResolutionEnabled = defaultSystemPropertiesResolutionEnabled;
        return this;
    }

    public boolean isEnvironmentPropertiesResolutionEnabled() {
        return environmentPropertiesResolutionEnabled;
    }

    public VariablePlaceholderValueResolverConfig setEnvironmentPropertiesResolutionEnabled(boolean defaultEnvironmentPropertiesResolutionEnabled) {
        this.environmentPropertiesResolutionEnabled = defaultEnvironmentPropertiesResolutionEnabled;
        return this;
    }

    public OperatingEnvironmentProperties getOperatingEnvironmentProperties() {
        return operatingEnvironmentProperties;
    }

    public VariablePlaceholderValueResolverConfig setOperatingEnvironmentProperties(OperatingEnvironmentProperties defaultOperatingEnvironmentProperties) {
        this.operatingEnvironmentProperties = defaultOperatingEnvironmentProperties;
        return this;
    }

    public boolean isIgnoreUnresolvablePlaceholders() {
        return ignoreUnresolvablePlaceholders;
    }

    public VariablePlaceholderValueResolverConfig setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders) {
        this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
        return this;
    }

    public String getPlaceholderPrefix() {
        return placeholderPrefix;
    }

    public VariablePlaceholderValueResolverConfig setPlaceholderPrefix(String placeholderPrefix) {
        this.placeholderPrefix = placeholderPrefix;
        return this;
    }

    public String getPlaceholderSuffix() {
        return placeholderSuffix;
    }

    public VariablePlaceholderValueResolverConfig setPlaceholderSuffix(String placeholderSuffix) {
        this.placeholderSuffix = placeholderSuffix;
        return this;
    }

    public String getPlaceholderDefaultValueSeparator() {
        return placeholderDefaultValueSeparator;
    }

    public VariablePlaceholderValueResolverConfig setPlaceholderDefaultValueSeparator(String placeholderDefaultValueSeparator) {
        this.placeholderDefaultValueSeparator = placeholderDefaultValueSeparator;
        return this;
    }

    public boolean isTrimValues() {
        return trimValues;
    }

    public VariablePlaceholderValueResolverConfig setTrimValues(boolean trimValues) {
        this.trimValues = trimValues;
        return this;
    }
}
