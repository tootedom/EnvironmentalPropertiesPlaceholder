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

import java.util.Map;
import java.util.Properties;

/**
 * User: dominictootell
 * Date: 03/06/2012
 * Time: 17:38
 */
public interface ValueResolver {
    final static boolean DEFAULT_SYSTEM_PROPERTIES_RESOLUTION_ENABLED = true;
    final static boolean DEFAULT_ENVIRONMENT_PROPERTIES_RESOLUTION_ENABLED = true;
    final static OperatingEnvironmentProperties DEFAULT_OPERATING_ENVIRONMENT_PROPERTIES = new JavaPlatformOperatingEnvironmentProperties();
    final static boolean DEFAULT_IGNORE_UNRESOLVABLE_PLACEHOLDERS = true;
    final static String DEFAULT_PLACEHOLDER_PREFIX = "${";
    final static String DEFAULT_PLACEHOLDER_SUFFIX = "}";
    final static String DEFAULT_PLACEHOLDER_DEFAULT_VALUE_SEPARATOR = ":";
    final static boolean DEFAULT_TRIM_VALUES = true;

    /**
     * Replaces all placeholders of format <code>${name}</code> with the corresponding property
     * from the supplied {@link Properties}.  All entries in the properties map are visited on by one to
     * resolve any placeholders from within the properties object.
     *
     * @param properties the <code>Properties</code> to use for replacement, and also the properties for which
     *                   we loop around and resolve.
     * @return the supplied value with placeholders replaced inline.
     */
    Properties resolvedPropertyValues(Properties properties,boolean trimValues);
    Properties resolvedPropertyValues(Properties properties);


    /**
     * Replaces all placeholders of format <code>${name}</code> with the corresponding property
     * from the supplied {@link Map<String,String>}.  All entries in the Map<String,String>
     * map are visited on by one to resolve any placeholders from within the same Map object.
     *
     * @param properties the <code>Map<String,String></code> to use for replacement,
     *                   and also the properties for which we loop around and resolve.
     * @return the supplied value with placeholders replaced inline.
     */
    Map<String,String> resolvedPropertyValues(Map<String,String> properties,boolean trimValues);
    Map<String,String> resolvedPropertyValues(Map<String,String> properties);
    /**
     * Replaces all placeholders of format <code>${name}</code> with the value returned from the supplied
     * {@link Properties} object.
     * @param key the entry within the given Properties object; for which we which to replace any placeholder that exist
     *            in the value; from the set of other properties contained within the given Properties object.
     * @param properties the <code>Properties</code> to use as the source of the placeholders replacements
     * @return the supplied value with placeholders replaced inline.
     */
    String resolvedPropertyValue(Properties properties, String key,boolean trimValues);
    String resolvedPropertyValue(Properties properties, String key);

    /**
     * Replaces all placeholders of format <code>${name}</code> with the value returned from the supplied
     * {@link Map} object.
     * @param key the entry within the given Map object; for which we which to replace any placeholder that exist
     *            in the value.  The replacement is taken from the set of other key/value pairs
     *            also contained within the given Map object.
     * @param map the <code>Map</code> to use as the source of the placeholders replacements
     * @return the supplied value with placeholders replaced inline.
     */
    String resolvedPropertyValue(Map<String,String> map, String key,boolean trimValues);
    String resolvedPropertyValue(Map<String,String> map, String key);
}
