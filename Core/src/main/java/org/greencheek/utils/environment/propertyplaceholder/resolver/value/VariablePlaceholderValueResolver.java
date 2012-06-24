/*
 * Copyright 2012 the original author or authors
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <p>
 * Taken and adapted from the source code of "org.springframework.util.PropertyPlaceholderHelper".
 * The code that performs the resolution of the place holder values in the properties is taken from the
 * previously mention; source code.  Thanks goes to the spring framework for this code.
 * </p>
 * <p>
 * Utility class for working with Strings that have placeholder values in them. A placeholder takes the form
 * <code>${name}</code>. Code taken from <code>org.springframework.util.PropertyPlaceholderHelper</code> is used, so that
 * these placeholders can be substituted for user-supplied values.
 * </p>
 * <p> The values used for substitution can be supplied using a {@link java.util.Properties} instance or
 * a Map<String,String> instance.
 * </p>
 *
 * User: dominictootell
 * Date: 03/06/2012
 * Time: 17:46
 */
public class VariablePlaceholderValueResolver implements ValueResolver {

    private static final Logger logger = LoggerFactory.getLogger(VariablePlaceholderValueResolver.class);

    private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<String, String>(4);

    static {
        wellKnownSimplePrefixes.put("}", "{");
        wellKnownSimplePrefixes.put("]", "[");
        wellKnownSimplePrefixes.put(")", "(");
    }


    private final String placeholderPrefix;

    private final String placeholderSuffix;

    private final String simplePrefix;

    private final String valueSeparator;

    private final boolean ignoreUnresolvablePlaceholders;

    private final boolean resolvingSystemProperties;
    private final boolean resolvingEnvironmentVariables;
    private final OperatingEnvironmentProperties operatingEnvironmentProperties;
    private final Properties systemProperties;
    private final Properties environmentProperties;
    private final boolean trimmingPropertyValues;


    public VariablePlaceholderValueResolver(boolean resolvingEnvironmentVariables,boolean resolvingSystemProperties) {
        this(new VariablePlaceholderValueResolverConfig()
                .setEnvironmentPropertiesResolutionEnabled(resolvingEnvironmentVariables)
                .setSystemPropertiesResolutionEnabled(resolvingSystemProperties));
    }

    public VariablePlaceholderValueResolver() {
        this(new VariablePlaceholderValueResolverConfig());
    }



    public VariablePlaceholderValueResolver(ValueResolverConfig config) {
        String placeholderPrefix = config.getPlaceholderPrefix();
        String placeholderSuffix = config.getPlaceholderSuffix();
        assertNotNull(placeholderPrefix, "placeholderPrefix must not be null");
        assertNotNull(placeholderSuffix, "placeholderSuffix must not be null");

        this.resolvingSystemProperties = config.isSystemPropertiesResolutionEnabled();
        this.resolvingEnvironmentVariables = config.isEnvironmentPropertiesResolutionEnabled();
        this.operatingEnvironmentProperties = config.getOperatingEnvironmentProperties();

        environmentProperties = resolvingEnvironmentVariables ? operatingEnvironmentProperties.getEnvironmentProperties() : new Properties();
        systemProperties = resolvingSystemProperties ? operatingEnvironmentProperties.getSystemProperties() : new Properties();


        this.placeholderPrefix = placeholderPrefix;
        this.placeholderSuffix = placeholderSuffix;
        String simplePrefixForSuffix = wellKnownSimplePrefixes.get(this.placeholderSuffix);
        if (simplePrefixForSuffix != null && this.placeholderPrefix.endsWith(simplePrefixForSuffix)) {
            this.simplePrefix = simplePrefixForSuffix;
        }
        else {
            this.simplePrefix = this.placeholderPrefix;
        }
        this.valueSeparator = config.getPlaceholderDefaultValueSeparator();
        this.ignoreUnresolvablePlaceholders = config.isIgnoreUnresolvablePlaceholders();

        this.trimmingPropertyValues = config.isTrimmingPropertyValues();
    }





    /**
     * Replaces all placeholders of format <code>${name}</code> with the corresponding property
     * from the supplied {@link Properties}.  All entries in the properties map are visited on by one to
     * resolve any placeholders from within the properties object.
     *
     * @param properties the <code>Properties</code> to use for replacement, and also the properties for which
     *                   we loop around and resolve.
     * @return the supplied value with placeholders replaced inline.
     */
    @Override
    public Properties resolvedPropertyValues(Properties properties) {
        return resolvedPropertyValues(properties,this.trimmingPropertyValues);
    }

    public Properties resolvedPropertyValues(Properties properties,boolean trimValues)
    {
        Map<String,String> map = resolvedPropertyValues(new HashMap(properties),trimValues);
        Properties p = new Properties();
        Set<Map.Entry<String,String>> set = map.entrySet();
        for (Map.Entry<String,String> entry : set) {
            p.put(entry.getKey(), entry.getValue());
        }
        return p;
    }

    /**
     * Replaces all placeholders of format <code>${name}</code> with the corresponding property
     * from the supplied {@link Map<String,String>}.  All entries in the Map<String,String>
     * map are visited on by one to resolve any placeholders from within the same Map object.
     *
     * @param properties the <code>Map<String,String></code> to use for replacement,
     *                   and also the properties for which we loop around and resolve.
     * @return the supplied value with placeholders replaced inline.
     */
    @Override
    public Map<String, String> resolvedPropertyValues(Map<String, String> properties) {
        return resolvedPropertyValues(properties,this.trimmingPropertyValues);
    }

    public Map<String, String> resolvedPropertyValues(Map<String, String> properties, boolean trimValues)
    {
        Map<String,String> map = new HashMap<String,String>(properties.size());
        for(Map.Entry<String,String> entry : properties.entrySet())  {
            map.put(entry.getKey(),parseStringValue(entry.getValue(),properties,
                                                    new HashSet<String>(),trimValues));
        }
        return map;
    }

    /**
     * Replaces all placeholders of format <code>${name}</code> with the value returned from the supplied
     * {@link Properties} object.
     * @param key the entry within the given Properties object; for which we which to replace any placeholder that exist
     *            in the value; from the set of other properties contained within the given Properties object.
     * @param properties the <code>Properties</code> to use as the source of the placeholders replacements
     * @return the supplied value with placeholders replaced inline.
     */
    @Override
    public String resolvedPropertyValue(Properties properties, String key) {
        return resolvedPropertyValue(properties,key,this.trimmingPropertyValues);
    }
    public String resolvedPropertyValue(Properties properties, String key, boolean trimValues) {
        if(properties.get(key)==null) return null;
        return parseStringValue(properties.getProperty(key),
                                new HashMap(properties),
                                new HashSet<String>(),trimValues);
    }

    /**
     * Replaces all placeholders of format <code>${name}</code> with the value returned from the supplied
     * {@link Map} object.
     * @param key the entry within the given Map object; for which we which to replace any placeholder that exist
     *            in the value.  The replacement is taken from the set of other key/value pairs
     *            also contained within the given Map object.
     * @param map the <code>Map</code> to use as the source of the placeholders replacements
     * @return the supplied value with placeholders replaced inline.
     */
    @Override
    public String resolvedPropertyValue(Map<String, String> map, String key) {
        return resolvedPropertyValue(map,key,this.trimmingPropertyValues);
    }

    public String resolvedPropertyValue(Map<String, String> map, String key,boolean trimValues){
        if(map.get(key)==null) return null;
        return parseStringValue(map.get(key),map,new HashSet<String>(),trimValues);
    }

    private String parseStringValue(
            String strVal, Map<String,String> placeholderResolver, Set<String> visitedPlaceholders,
            boolean trimValues) {

        StringBuilder buf = new StringBuilder(strVal);

        int startIndex = strVal.indexOf(this.placeholderPrefix);
        while (startIndex != -1) {
            int endIndex = findPlaceholderEndIndex(buf, startIndex);
            if (endIndex != -1) {
                String placeholder = buf.substring(startIndex + this.placeholderPrefix.length(), endIndex);
                if (!visitedPlaceholders.add(placeholder)) {
                    throw new IllegalArgumentException(
                            "Circular placeholder reference '" + placeholder + "' in property definitions");
                }
                // Recursive invocation, parsing placeholders contained in the placeholder key.
                placeholder = parseStringValue(placeholder, placeholderResolver, visitedPlaceholders,trimValues);

                // Now obtain the value for the fully resolved key...
                String propVal = placeholderResolver.get(placeholder);
                if(propVal == null && (this.resolvingEnvironmentVariables || this.resolvingSystemProperties)) {
                    if(this.resolvingEnvironmentVariables) {
                        propVal = environmentProperties==null ? null : environmentProperties.getProperty(placeholder);
                    }
                    if(this.resolvingSystemProperties) {
                        String temp =  systemProperties==null ? null : systemProperties.getProperty(placeholder);
                        if(temp!=null) {
                            propVal = temp;
                        }
                    }
                }

                if (propVal == null && this.valueSeparator != null) {
                    int separatorIndex = placeholder.indexOf(this.valueSeparator);
                    if (separatorIndex != -1) {
                        String actualPlaceholder = placeholder.substring(0, separatorIndex);
                        String defaultValue = placeholder.substring(separatorIndex + this.valueSeparator.length());
                        propVal = placeholderResolver.get(actualPlaceholder);
                        if (propVal == null) {
                            propVal = defaultValue;
                        }
                    }
                }
                if (propVal != null) {
                    // Recursive invocation, parsing placeholders contained in the
                    // previously resolved placeholder value.
                    propVal = parseStringValue(propVal, placeholderResolver, visitedPlaceholders,trimValues);
                    buf.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
                    if (logger.isTraceEnabled()) {
                        logger.trace("Resolved placeholder '" + placeholder + "'");
                    }
                    startIndex = buf.indexOf(this.placeholderPrefix, startIndex + propVal.length());
                }
                else if (this.ignoreUnresolvablePlaceholders) {
                    // Proceed with unprocessed value.
                    startIndex = buf.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
                }
                else {
                    throw new IllegalArgumentException("Could not resolve placeholder '" + placeholder + "'");
                }

                visitedPlaceholders.remove(placeholder);
            }
            else {
                startIndex = -1;
            }
        }

        if(trimValues) {
            return buf.toString().trim();
        } else {
            return buf.toString();
        }
    }

    private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
        int index = startIndex + this.placeholderPrefix.length();
        int withinNestedPlaceholder = 0;
        while (index < buf.length()) {
            if (substringMatch(buf, index, this.placeholderSuffix)) {
                if (withinNestedPlaceholder > 0) {
                    withinNestedPlaceholder--;
                    index = index + this.placeholderSuffix.length();
                }
                else {
                    return index;
                }
            }
            else if (substringMatch(buf, index, this.simplePrefix)) {
                withinNestedPlaceholder++;
                index = index + this.simplePrefix.length();
            }
            else {
                index++;
            }
        }
        return -1;
    }

    /**
     * Throw an IllegalArgumentException if the given object is null, with the given message
     *
     * @param object object to check for null
     * @param message the message to throw in the  IllegalArgumentException
     */
    private void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * Taken from Spring StringUtils (org.springframework.util.StringUtils;)
     *
     * Test whether the given string matches the given substring
     * at the given index.
     * @param str the original string (or StringBuilder)
     * @param index the index in the original string to start matching against
     * @param substring the substring to match at the given index
     */
    public boolean substringMatch(CharSequence str, int index, CharSequence substring) {
        for (int j = 0; j < substring.length(); j++) {
            int i = index + j;
            if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    private OperatingEnvironmentProperties getOperatingEnvironmentProperties() {
        return this.operatingEnvironmentProperties;
    }

}
