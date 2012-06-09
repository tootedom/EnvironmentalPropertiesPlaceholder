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
import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentVariableReader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ClassPathResourceLoader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ResourceLoader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.ValueResolver;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * User: dominictootell
 * Date: 09/06/2012
 * Time: 09:11
 */
public abstract class AbstractTestPropertiesResolverBuilder {

    PropertiesResolverBuilder builder;

    public abstract PropertiesResolverBuilder createBuilder();

    @Before
    public void setup() {
        builder = createBuilder();
    }


    @Test
    public void testCanSetTrimmingPropertyValues() {
        builder.setTrimmingPropertyValues(true);
        assertTrue(builder.isTrimmingPropertyValues());
        builder.setTrimmingPropertyValues(false);
        assertFalse(builder.isTrimmingPropertyValues());
    }

    @Test
    public void testCanSetPropertyValueResolver() {
        builder.setPropertyValueResolver(new ValueResolver() {
            @Override
            public Properties resolvedPropertyValues(Properties properties) {
                return resolvedPropertyValues(properties,false);
            }
            public Properties resolvedPropertyValues(Properties properties,boolean trimValues){
                Properties p = new Properties();
                p.putAll(Collections.singletonMap("hey","hey"));
                return p;
            }

            @Override
            public Map<String, String> resolvedPropertyValues(Map<String, String> properties) {
                return resolvedPropertyValues(properties,false);
            }
            public Map<String, String> resolvedPropertyValues(Map<String, String> properties,boolean trimValues){
                return Collections.singletonMap("hey","hey");
            }

            @Override
            public String resolvedPropertyValue(Properties properties, String key) {
                return resolvedPropertyValue(properties,key,false);
            }
            public String resolvedPropertyValue(Properties properties, String key,boolean trimValues){
                return "hey";
            }

            @Override
            public String resolvedPropertyValue(Map<String, String> map, String key) {
                return resolvedPropertyValue(map,key,false);
            }
            public String resolvedPropertyValue(Map<String, String> map, String key,boolean trimValues) {
                return "hey";
            }
        });

        ValueResolver v = builder.getValueResolver();

        assertEquals("hey",v.resolvedPropertyValue(new Properties(),"x"));
        assertEquals("hey",v.resolvedPropertyValue(Collections.singletonMap("hey","hey"),"x"));
    }




//    @Test
//    public void testCanSetResolvingSystemProperties() {
//        builder.setResolvingSystemProperties(true);
//        assertTrue(builder.isResolvingSystemProperties());
//        builder.setResolvingSystemProperties(false);
//        assertFalse(builder.isResolvingSystemProperties());
//    }
//
//    @Test
//    public void testCanSetResolvingEnvironmentVariables() {
//        builder.setResolvingEnvironmentVariables(true);
//        assertTrue(builder.isResolvingEnvironmentVariables());
//        builder.setResolvingEnvironmentVariables(false);
//        assertFalse(builder.isResolvingEnvironmentVariables());
//    }








}
