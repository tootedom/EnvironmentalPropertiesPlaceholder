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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

/**
 * User: dominictootell
 * Date: 18/05/2012
 * Time: 17:18
 */
public class TestEnvironmentalPropertyMergerBuilder extends AbstractTestPropertiesMergerBuilder {

    @Override
    public PropertiesMergerBuilder createBuilder() {
        return new EnvironmentSpecificPropertiesMergerBuilder(new ClassPathResourceLoader(""));
    }

}
