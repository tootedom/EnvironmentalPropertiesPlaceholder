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

import org.greencheek.utils.environment.propertyplaceholder.resolver.PropertiesResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ClassPathResourceLoader;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * User: dominictootell
 * Date: 04/06/2012
 * Time: 12:51
 */
public class TestEnvironmentPropertyReaderBuilderCanBuildPropertiesResolver {

//    PropertiesMergerBuilder builder;
//    private final static String SIMPLE_PROPERTY_NAME = "simple";
//    private final static String SIMPLE_PROPERTY_VALUE = "simplevalue";
//
//    @Before
//    public void setup() {
//        builder = new EnvironmentSpecificPropertiesMergerBuilder(new ClassPathResourceLoader(""));
//    }
//
//
//    @Test
//    public void testReturnedBuilderDoesNotHaveTrimmingEnabled() {
//
//        builder.setTrimmingPropertyValues(true);
//
//        PropertiesResolver reader = builder.build();
//
//        assertTrue(reader.isTrimmingPropertyValues());
//    }
//
//    @Test
//    public void testReturnedBuilderHasDefaults() {
//        PropertiesResolver reader = builder.build();
//        assertTrue(reader.isTrimmingPropertyValues());
//        assertTrue(reader.isResolvingEnvironmentVariables());
//        assertTrue(reader.isResolvingSystemProperties());
//    }
//
//    @Test
//    public void testReturnsDefaultPropertiesNoOverrides() {
//        PropertiesResolver reader = builder.build();
//        assertEquals(SIMPLE_PROPERTY_VALUE, reader.getProperty(SIMPLE_PROPERTY_NAME));
//    }
}
