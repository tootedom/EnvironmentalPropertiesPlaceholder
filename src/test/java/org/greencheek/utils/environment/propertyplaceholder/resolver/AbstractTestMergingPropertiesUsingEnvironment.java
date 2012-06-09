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

import junit.framework.Assert;
import org.greencheek.utils.environment.propertyplaceholder.builder.EnvironmentSpecificPropertiesMergerBuilder;
import org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesMergerBuilder;
import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentProperties;
import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentVariableReader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ClassPathResourceLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * User: dominictootell
 * Date: 05/06/2012
 * Time: 14:51
 */
public abstract class AbstractTestMergingPropertiesUsingEnvironment
{
    PropertiesResolver resolver;
    PropertiesResolver nonTrimmingResolver;

    @Before
    public void setup() {
        resolver = buildResolver(true);
        nonTrimmingResolver = buildResolver(false);
    }

    public Properties getProperties() {
        final Properties p = new Properties();
        p.put("one","1");
        p.put("two","${one}+${one}");
        p.put("trimmed"," trimmed ");
        p.put("resolvedtrimmed"," resolved ${trimmed} ");
        return p;
    }

    public abstract PropertiesResolver buildResolver(boolean trimValues);



    @Test
    public void testResolverResolvesPropertyValuesContainingVariables() {


        assertEquals("1", resolver.getProperty("one"));
        assertEquals("1+1",resolver.getProperty("two"));
    }

    @Test
    public void testResolverReturnsVerbatimPropertyValuesContainingVariables() {
        assertEquals("1",resolver.getUnResolvedProperty("one"));
        assertEquals("${one}+${one}",resolver.getUnResolvedProperty("two"));
    }

    @Test
    public void testResolverReturnsNullForNonExistentProperty() {
        assertNull(resolver.getUnResolvedProperty(UUID.randomUUID().toString()));
        assertNull(resolver.getProperty(UUID.randomUUID().toString()));
    }

    @Test
    public void testResolverTrimsProperties() {
        assertEquals("trimmed",resolver.getProperty("trimmed"));
        assertEquals("resolved trimmed",resolver.getProperty("resolvedtrimmed"));

        assertEquals("resolved ${trimmed}",resolver.getUnResolvedProperty("resolvedtrimmed"));


    }

    @Test
    public void testResolverDoesNotTrimsProperties() {
        assertEquals(" trimmed ",nonTrimmingResolver.getProperty("trimmed"));
        assertEquals(" resolved  trimmed  ",nonTrimmingResolver.getProperty("resolvedtrimmed"));

        assertEquals(" resolved ${trimmed} ",nonTrimmingResolver.getUnResolvedProperty("resolvedtrimmed"));


    }

//    private PropertiesMergerBuilder resolverSystemProperties;
//    private PropertiesMergerBuilder resolverEnvironment;
//    private PropertiesMergerBuilder resolverSystemPropertiesAndEnvironment;
//
//
//    @Before
//    public void setUp() {
//
//        resolverSystemProperties = new EnvironmentSpecificPropertiesMergerBuilder(new ClassPathResourceLoader("/system_properties/config/"));
//        resolverSystemProperties.setVariablesUsedForSwitchingConfiguration(new String[] {"TARGET_PLATFORM"})
//        .setOperatingEnvironmentVariableReader(new OperatingEnvironmentVariableReader() {
//            @Override
//            public String getProperty(String property, String defaultValue) {
//                return "production";
//            }
//
//            @Override
//            public String getEnv(String property, String defaultValue) {
//                return null;
//            }
//        })
//        .setOperatingEnvironmentProperties( new OperatingEnvironmentProperties() {
//            @Override
//            public Properties getEnvironmentProperties() {
//                return new Properties();
//            }
//
//            @Override
//            public Properties getSystemProperties() {
//                Properties props = new Properties();
//                props.setProperty("TIME","today");
//                props.setProperty("SERVER_LOCATION","london");
//                return props;
//            }
//        })
//        .setRelativeLocationOfFilesOverridingDefaultProperties("environments")
//        .setResolvingEnvironmentVariables(false)
//        .setResolvingSystemProperties(true);
//
//    }
//
//    @Test
//    public void testPropertiesResolveSystemPropertiesOk() {
//
//
//        PropertiesResolver resolverForSystemProperties = resolverSystemProperties.build();
//
//        Properties p = resolverForSystemProperties.getMergedProperties();
//        Map<String,String> actual = new HashMap(p);
//        assertTrue(actual.size()==2);
//
//        assertThat(actual, allOf(
//                hasEntry("default", "today"),
//                hasEntry("platform", "london")
//        ));
//    }


}
