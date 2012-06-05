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
package org.greencheek.utils.environment.propertyplaceholder.resolver.value;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;

/**
 * User: dominictootell
 * Date: 23/05/2012
 * Time: 18:59
 */
public class TestValueResolver {

    ValueResolver resolver;

    @Before
    public void setUp() {
        resolver = new VariablePlaceholderValueResolver();
    }

    @Test
    public void testValueResolverCanResolveAllProperties() {
        Properties p = new Properties();
        p.setProperty("prop1","ONE");
        p.setProperty("prop2","value=${prop1}");

        Map<String,String> map = new HashMap(p);


        Properties resolvedProperties = resolver.resolvedPropertyValues(p);

        Map<String,String> actual = new HashMap(resolvedProperties);
        System.out.println(actual);

        _checkResolvedMapValues(actual) ;

        Map<String,String> resolvedMap = resolver.resolvedPropertyValues(map);

        _checkResolvedMapValues(resolvedMap);
    }




    private void _checkResolvedMapValues(Map<String,String> actual) {
        assertThat(actual, allOf(
                hasEntry("prop1", "ONE"),
                hasEntry("prop2", "value=ONE")
        ));

    }

    private void _checkUnresolvedMapValues(Map<String,String> actual) {
        assertThat(actual, allOf(
                hasEntry("prop1", "ONE"),
                hasEntry("prop2", "value=${prop1}")
        ));

    }


    @Test
    public void testValueResolverCanResolveAProperty() {
        Properties p = new Properties();
        String key = "prop2";
        String expected = "value=ONE";

        p.setProperty("prop1","ONE");
        p.setProperty("prop2","value=${prop1}");

        String value = resolver.resolvedPropertyValue(p, key);

        assertEquals("value should be :"+expected,expected,value);

        Map<String,String> map = new HashMap(p);
        value = resolver.resolvedPropertyValue(map, key);

        assertEquals("value should be :"+expected,expected,value);
    }

    @Test
    public void testValueResolverUsesDefaultValue() {
        Properties p = new Properties();
        String key = "prop2";
        String expected = "value=DEFAULT";

        p.setProperty("prop1","ONE");
        p.setProperty("prop2","value=${prop3:DEFAULT}");

        String value = resolver.resolvedPropertyValue(p, key);

        assertEquals("value should be :"+expected,expected,value);

        Map<String,String> map = new HashMap(p);
        value = resolver.resolvedPropertyValue(map, key);

        assertEquals("value should be :"+expected,expected,value);
    }


}
