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
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.ValueResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.VariablePlaceholderValueResolver;

import java.util.Properties;

/**
 * A helper interface that is combination of the 3 interfaces:
 * <ul>
 *     <li>{@link org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesMergerBuilder}</li>
 *     <li>{@link org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesResolverBuilder}</li>
 *     <li>{@link org.greencheek.utils.environment.propertyplaceholder.resolver.value.ValueResolver}</li>
 * </ul>
 *
 * User: dominictootell
 * Date: 10/06/2012
 * Time: 18:41
 */
public interface ResolvedPropertiesBuilder  {

    public static final ValueResolver DEFAULT_VALUE_RESOLVER = new VariablePlaceholderValueResolver();
    public static final PropertiesMergerBuilder DEFAULT_PROPERTIES_MERGER_BUILDER = new EnvironmentSpecificPropertiesMergerBuilder();
    public static final PropertiesResolverBuilder DEFAULT_PROPERTIES_RESOLVER_BUILDER = new EnvironmentSpecificPropertiesResolverBuilder();

    /**
     * Returns the set of properties, with all embedded property values resolved.  The properties themselves
     * come from a set of combined property files that are source dependent on system or environment properties.
     * Any property value that contain variables (placeholders i.e. ${..})  are resolved.
     *
     * @return
     */
    Properties buildResolvedProperties();
}
