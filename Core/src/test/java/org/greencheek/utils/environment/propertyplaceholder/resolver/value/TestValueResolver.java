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

import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentProperties;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;

import static junit.framework.Assert.assertNull;
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
    ValueResolver systemPropertiesValueResolver;
    ValueResolver environmentPropertiesValueResolver;
    ValueResolver systemAndEnvironmentPropertiesValueResolver;

    @Before
    public void setUp() {
        resolver = new VariablePlaceholderValueResolver();
        ValueResolverConfig config = new VariablePlaceholderValueResolverConfig();
        config.setEnvironmentPropertiesResolutionEnabled(false)
              .setSystemPropertiesResolutionEnabled(true)
              .setOperatingEnvironmentProperties(new OperatingEnvironmentProperties() {
            @Override
            public Properties getEnvironmentProperties() {
                Properties props = new Properties();
                props.setProperty("ENVIRONMENT_LOCATION","ealing");
                return props;
            }

            @Override
            public Properties getSystemProperties() {
                Properties props = new Properties();
                props.setProperty("TIME","today");
                props.setProperty("SERVER_LOCATION","london");
                return props;
            }
        });
        systemPropertiesValueResolver = new VariablePlaceholderValueResolver(config);

        config = new VariablePlaceholderValueResolverConfig();
        config.setEnvironmentPropertiesResolutionEnabled(true)
              .setSystemPropertiesResolutionEnabled(false)
              .setOperatingEnvironmentProperties(new OperatingEnvironmentProperties() {
                  @Override
                  public Properties getEnvironmentProperties() {
                      Properties props = new Properties();
                      props.setProperty("ENVIRONMENT_LOCATION","ealing");
                      return props;
                  }

                  @Override
                  public Properties getSystemProperties() {
                      Properties props = new Properties();
                      props.setProperty("TIME","today");
                      props.setProperty("SERVER_LOCATION","london");
                      return props;
                  }
              });

        environmentPropertiesValueResolver = new VariablePlaceholderValueResolver(config);

        config = new VariablePlaceholderValueResolverConfig();
        config.setEnvironmentPropertiesResolutionEnabled(true)
                .setSystemPropertiesResolutionEnabled(true)
                .setOperatingEnvironmentProperties(new OperatingEnvironmentProperties() {
                    @Override
                    public Properties getEnvironmentProperties() {
                        Properties props = new Properties();
                        props.setProperty("ENVIRONMENT_LOCATION","ealing");
                        return props;
                    }

                    @Override
                    public Properties getSystemProperties() {
                        Properties props = new Properties();
                        props.setProperty("TIME","today");
                        props.setProperty("SERVER_LOCATION","london");
                        return props;
                    }
                });
        systemAndEnvironmentPropertiesValueResolver = new VariablePlaceholderValueResolver(config);
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
    public void testValueResolverReturnsNullForNonExistentProperty() {
        Properties p = new Properties();
        String key = "prop2";
        String expected = "value=ONE";

        p.setProperty("prop1","ONE");
        p.setProperty("prop2","value=${prop1}");

        String value = resolver.resolvedPropertyValue(p, UUID.randomUUID().toString());

        assertNull(value);

        assertNull(resolver.resolvedPropertyValue(new HashMap(p), UUID.randomUUID().toString()));
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



    @Test
    public void testValueResolverCanResolveSystemProperties() {

        Properties p = new Properties();
        p.setProperty("prop1","${TIME}");
        p.setProperty("prop2","${SERVER_LOCATION}");
        p.setProperty("prop3","${ENVIRONMENT_LOCATION}");

        Map<String,String> map = new HashMap(p);


        Properties resolvedProperties = systemPropertiesValueResolver.resolvedPropertyValues(p);

        Map<String,String> actual = new HashMap(resolvedProperties);

        assertThat(actual, allOf(
                hasEntry("prop1", "today"),
                hasEntry("prop2", "london"),
                hasEntry("prop3","${ENVIRONMENT_LOCATION}")));

    }


    @Test
    public void testValueResolverCanResolveEnvironmentProperties() {

        Properties p = new Properties();
        p.setProperty("prop1","${TIME}");
        p.setProperty("prop2","${SERVER_LOCATION}");
        p.setProperty("prop3","${ENVIRONMENT_LOCATION}");

        Map<String,String> map = new HashMap(p);


        Properties resolvedProperties = environmentPropertiesValueResolver.resolvedPropertyValues(p);

        Map<String,String> actual = new HashMap(resolvedProperties);

        assertThat(actual, allOf(
                hasEntry("prop1", "${TIME}"),
                hasEntry("prop2", "${SERVER_LOCATION}"),
                hasEntry("prop3","ealing")));

    }

    @Test
    public void testValueResolverCanResolveEnvironmentAndSystemProperties() {

        Properties p = new Properties();
        p.setProperty("prop1","${TIME}");
        p.setProperty("prop2","${SERVER_LOCATION}");
        p.setProperty("prop3","${ENVIRONMENT_LOCATION}");

        Map<String,String> map = new HashMap(p);


        Properties resolvedProperties = systemAndEnvironmentPropertiesValueResolver.resolvedPropertyValues(p);

        Map<String,String> actual = new HashMap(resolvedProperties);

        assertThat(actual, allOf(
                hasEntry("prop1", "today"),
                hasEntry("prop2", "london"),
                hasEntry("prop3","ealing")));

    }

    @Test
    public void testValueResolverCanResolveEnvironmentAndSystemPropertiesRecursively() {

        Properties p = new Properties();
        p.setProperty("prop1","${TIME}");
        p.setProperty("prop2","${prop1}${prop4}${SERVER_LOCATION}");
        p.setProperty("prop3","${ENVIRONMENT_LOCATION}");
        p.setProperty("prop4","${prop3}");

        Map<String,String> map = new HashMap(p);


        Properties resolvedProperties = systemAndEnvironmentPropertiesValueResolver.resolvedPropertyValues(p);

        Map<String,String> actual = new HashMap(resolvedProperties);

        assertThat(actual, allOf(
                hasEntry("prop1", "today"),
                hasEntry("prop2", "todayealinglondon"),
                hasEntry("prop3","ealing"),
                hasEntry("prop4","ealing")));

    }
}
