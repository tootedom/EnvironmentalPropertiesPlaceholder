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
import org.greencheek.utils.environment.propertyplaceholder.resolver.EnvironmentSpecificPropertiesResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.PropertiesResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.ValueResolver;

/**
 * User: dominictootell
 * Date: 09/06/2012
 * Time: 13:12
 */
public class SystemAndEnvironmentSpecificPropertiesResolverBuilder implements PropertiesResolverBuilder {

    private boolean trimmingPropertyValues = DEFAULT_TRIMMING_PROPERTY_VALUES ;
    private ValueResolver propertyValueResolver = DEFAULT_PROPERTY_VALUE_RESOLVER;

    @Override
    public PropertiesResolverBuilder setTrimmingPropertyValues(boolean trimValues) {
        this.trimmingPropertyValues = trimValues;
        return this;
    }

    @Override
    public boolean isTrimmingPropertyValues() {
        return this.trimmingPropertyValues;
    }

    @Override
    public PropertiesResolverBuilder setPropertyValueResolver(ValueResolver resolver) {
        propertyValueResolver = resolver;
        return this;
    }

    @Override
    public ValueResolver getValueResolver() {
        return propertyValueResolver;
    }


    @Override
    public PropertiesResolver build(PropertiesMerger mergedPropertiesLoader) {
        ValueResolver resolver = getValueResolver();
        if(resolver==null) resolver = DEFAULT_PROPERTY_VALUE_RESOLVER;

        return new EnvironmentSpecificPropertiesResolver(this,mergedPropertiesLoader,resolver);
    }

}
