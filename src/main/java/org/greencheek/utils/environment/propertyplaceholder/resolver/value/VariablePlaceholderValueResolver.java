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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.MalformedParameterizedTypeException;
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

    private static final Log logger = LogFactory.getLog(VariablePlaceholderValueResolver.class);

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


    public VariablePlaceholderValueResolver() {
        this(DEFAULT_PLACEHOLDER_PREFIX, DEFAULT_PLACEHOLDER_SUFFIX, DEFAULT_PLACEHOLDER_DEFAULT_VALUE_SEPARATOR, true);
    }
    /**
     * Creates a new <code>PropertyPlaceholderHelper</code> that uses the supplied prefix and suffix.
     * Unresolvable placeholders are ignored.
     * @param placeholderPrefix the prefix that denotes the start of a placeholder.
     * @param placeholderSuffix the suffix that denotes the end of a placeholder.
     */
    public VariablePlaceholderValueResolver(String placeholderPrefix, String placeholderSuffix) {
        this(placeholderPrefix, placeholderSuffix, null, true);
    }

    /**
     * Creates a new <code>PropertyPlaceholderHelper</code> that uses the supplied prefix and suffix.
     * @param placeholderPrefix the prefix that denotes the start of a placeholder
     * @param placeholderSuffix the suffix that denotes the end of a placeholder
     * @param valueSeparator the separating character between the placeholder variable
     * and the associated default value, if any
     * @param ignoreUnresolvablePlaceholders indicates whether unresolvable placeholders should be ignored
     * (<code>true</code>) or cause an exception (<code>false</code>).
     */
    public VariablePlaceholderValueResolver(String placeholderPrefix, String placeholderSuffix,
                                     String valueSeparator, boolean ignoreUnresolvablePlaceholders) {

        Assert.notNull(placeholderPrefix, "placeholderPrefix must not be null");
        Assert.notNull(placeholderSuffix, "placeholderSuffix must not be null");
        this.placeholderPrefix = placeholderPrefix;
        this.placeholderSuffix = placeholderSuffix;
        String simplePrefixForSuffix = wellKnownSimplePrefixes.get(this.placeholderSuffix);
        if (simplePrefixForSuffix != null && this.placeholderPrefix.endsWith(simplePrefixForSuffix)) {
            this.simplePrefix = simplePrefixForSuffix;
        }
        else {
            this.simplePrefix = this.placeholderPrefix;
        }
        this.valueSeparator = valueSeparator;
        this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
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
        Map<String,String> map = resolvedPropertyValues(new HashMap(properties));
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
        Map<String,String> map = new HashMap<String,String>(properties.size());
        for(Map.Entry<String,String> entry : properties.entrySet())  {
            map.put(entry.getKey(),parseStringValue(entry.getValue(),properties,new HashSet<String>()));
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
        return parseStringValue(properties.getProperty(key),new HashMap(properties), new HashSet<String>());
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
        return parseStringValue(map.get(key),map,new HashSet<String>());
    }

    private String parseStringValue(
            String strVal, Map<String,String> placeholderResolver, Set<String> visitedPlaceholders) {

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
                placeholder = parseStringValue(placeholder, placeholderResolver, visitedPlaceholders);

                // Now obtain the value for the fully resolved key...
                String propVal = placeholderResolver.get(placeholder);
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
                    propVal = parseStringValue(propVal, placeholderResolver, visitedPlaceholders);
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

        return buf.toString();
    }

    private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
        int index = startIndex + this.placeholderPrefix.length();
        int withinNestedPlaceholder = 0;
        while (index < buf.length()) {
            if (StringUtils.substringMatch(buf, index, this.placeholderSuffix)) {
                if (withinNestedPlaceholder > 0) {
                    withinNestedPlaceholder--;
                    index = index + this.placeholderSuffix.length();
                }
                else {
                    return index;
                }
            }
            else if (StringUtils.substringMatch(buf, index, this.simplePrefix)) {
                withinNestedPlaceholder++;
                index = index + this.simplePrefix.length();
            }
            else {
                index++;
            }
        }
        return -1;
    }

}
