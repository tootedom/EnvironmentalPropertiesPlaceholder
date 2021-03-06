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
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.ValueResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.VariablePlaceholderValueResolver;

import java.util.Properties;

/**
 * @{link #build} must be called on the same thread as the other setters.
 * This is so that the @{link PropertiesResolver} is created with all the values that have been set on the builder
 * object; otherwise the resulting resolver could be constructed with default values rather that those set on the
 * builder object; due to the visibility rules of the jvm memory model
 *
 * User: dominictootell
 * Date: 09/06/2012
 * Time: 13:08
 */
public interface PropertiesResolverBuilder {
    final static boolean DEFAULT_TRIMMING_PROPERTY_VALUES = true;
    final static ValueResolver DEFAULT_PROPERTY_VALUE_RESOLVER = new VariablePlaceholderValueResolver();

    public PropertiesResolver build(PropertiesMerger mergedPropertiesLoader);

    /**
     * Returns a set of properties that are retrieved from the given properties merger
     * with the values resolved of any variables they contained (if they can be resolved)
     *
     * @param mergedPropertiesLoader The merger that is responsible for reading the properties from the file system
     * @return the set of the properties, from the merger, with the values resolved of any variables (if possible)
     */
    public Properties buildProperties(PropertiesMerger mergedPropertiesLoader);

    /**
     * Returns a set of properties that are retrieved from the given properties merger (the merger is created
     * from the PropertiesMergerBuilder) with the values resolved of any variables they contained (if they can be resolved)
     *
     * @param builder The builder object that can be used to create a merger
     *                that is responsible for reading the properties from the file system; from which the properties
     *                are obtained, and the values resolved.
     *
     * @return the set of the properties, from the merger, with the values resolved of any variables (if possible)
     */
    public Properties buildProperties(PropertiesMergerBuilder builder);

    public PropertiesResolverBuilder setPropertyValueResolver(ValueResolver resolver);
    public ValueResolver getValueResolver();

    public PropertiesResolverBuilder setTrimmingPropertyValues(boolean b);
    public boolean isTrimmingPropertyValues();


}
