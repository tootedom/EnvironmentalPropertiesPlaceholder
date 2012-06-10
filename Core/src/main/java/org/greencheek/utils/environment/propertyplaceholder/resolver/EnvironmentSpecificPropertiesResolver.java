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
package org.greencheek.utils.environment.propertyplaceholder.resolver;

import org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesMergerBuilder;
import org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesResolverBuilder;
import org.greencheek.utils.environment.propertyplaceholder.merger.PropertiesMerger;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.ValueResolver;

import java.util.*;

/**
 * User: dominictootell
 * Date: 20/05/2012
 * Time: 13:34
 */
public class EnvironmentSpecificPropertiesResolver implements PropertiesResolver {

    private final boolean trimmingPropertyValues;
    private final ValueResolver propertyValueResolver;
    private final Properties properties;


    public EnvironmentSpecificPropertiesResolver(PropertiesResolverBuilder builder,
                                                 PropertiesMerger mergedPropertiesLoader,
                                                 ValueResolver propertyValueResolver) {
        this.trimmingPropertyValues = builder.isTrimmingPropertyValues();
        this.propertyValueResolver = propertyValueResolver;
        this.properties = mergedPropertiesLoader.getMergedProperties();
    }



    @Override
    public boolean isTrimmingPropertyValues() {
        return trimmingPropertyValues;
    }


    @Override
    public String getProperty(String propertyName) {
        if(properties.get(propertyName)==null) return null;
        return propertyValueResolver.resolvedPropertyValue(properties,propertyName,isTrimmingPropertyValues());
    }

    @Override
    public String getUnResolvedProperty(String propertyName) {
        String value = properties.getProperty(propertyName);
        if(value==null) return null;
        if(isTrimmingPropertyValues())
            return value.trim();

        return value;
    }

}
