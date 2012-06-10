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
 * User: dominictootell
 * Date: 10/06/2012
 * Time: 20:46
 */
public interface ValueResolverConfig {
    final static boolean DEFAULT_SYSTEM_PROPERTIES_RESOLUTION_ENABLED = true;
    final static boolean DEFAULT_ENVIRONMENT_PROPERTIES_RESOLUTION_ENABLED = true;
    final static OperatingEnvironmentProperties DEFAULT_OPERATING_ENVIRONMENT_PROPERTIES = new JavaPlatformOperatingEnvironmentProperties();
    final static boolean DEFAULT_IGNORE_UNRESOLVABLE_PLACEHOLDERS = true;
    final static String DEFAULT_PLACEHOLDER_PREFIX = "${";
    final static String DEFAULT_PLACEHOLDER_SUFFIX = "}";
    final static String DEFAULT_PLACEHOLDER_DEFAULT_VALUE_SEPARATOR = ":";
    final static boolean DEFAULT_TRIMMING_PROPERTY_VALUES = true;

    boolean isSystemPropertiesResolutionEnabled();

    ValueResolverConfig setSystemPropertiesResolutionEnabled(boolean defaultSystemPropertiesResolutionEnabled);

    boolean isEnvironmentPropertiesResolutionEnabled();

    ValueResolverConfig setEnvironmentPropertiesResolutionEnabled(boolean defaultEnvironmentPropertiesResolutionEnabled);

    OperatingEnvironmentProperties getOperatingEnvironmentProperties();

    ValueResolverConfig setOperatingEnvironmentProperties(OperatingEnvironmentProperties defaultOperatingEnvironmentProperties);

    boolean isIgnoreUnresolvablePlaceholders();

    ValueResolverConfig setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders);

    String getPlaceholderPrefix();

    ValueResolverConfig setPlaceholderPrefix(String placeholderPrefix);

    String getPlaceholderSuffix();

    ValueResolverConfig setPlaceholderSuffix(String placeholderSuffix);

    String getPlaceholderDefaultValueSeparator();

    ValueResolverConfig setPlaceholderDefaultValueSeparator(String placeholderDefaultValueSeparator);

    boolean isTrimmingPropertyValues();

    ValueResolverConfig setTrimmingPropertyValues(boolean trimValues);
}
