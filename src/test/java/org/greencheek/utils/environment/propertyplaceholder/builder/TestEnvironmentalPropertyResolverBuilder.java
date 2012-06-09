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

import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ClassPathResourceLoader;

/**
 * User: dominictootell
 * Date: 09/06/2012
 * Time: 14:53
 */
public class TestEnvironmentalPropertyResolverBuilder extends AbstractTestPropertiesResolverBuilder {
    @Override
    public PropertiesResolverBuilder createBuilder() {
        return new SystemAndEnvironmentSpecificPropertiesResolverBuilder();

    }
}
