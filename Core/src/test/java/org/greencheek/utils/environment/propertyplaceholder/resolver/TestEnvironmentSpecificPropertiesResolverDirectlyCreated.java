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

import org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesResolverBuilder;
import org.greencheek.utils.environment.propertyplaceholder.builder.SystemAndEnvironmentSpecificPropertiesResolverBuilder;
import org.greencheek.utils.environment.propertyplaceholder.merger.PropertiesMerger;
import org.greencheek.utils.environment.propertyplaceholder.merger.SystemAndEnvironmentSpecificPropertiesMerger;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.ValueResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.VariablePlaceholderValueResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * User: dominictootell
 * Date: 09/06/2012
 * Time: 14:59
 */
public class TestEnvironmentSpecificPropertiesResolverDirectlyCreated extends AbstractTestMergingPropertiesUsingEnvironment{





    @Override
    public PropertiesResolver buildResolver(boolean trimValues) {
        final Properties p = getProperties();

        PropertiesResolver resolver =
                new SystemAndEnvironmentSpecificPropertiesResolverBuilder()
                .setTrimmingPropertyValues(trimValues)
                .setPropertyValueResolver(new VariablePlaceholderValueResolver())
                .build(new PropertiesMerger() {
                    @Override
                    public Properties getMergedProperties() {
                        return p;
                    }

                    @Override
                    public Map<String, String> getMergedPropertiesAsMap() {
                        return new HashMap(p);
                    }
                });

        return resolver;
    }
}
