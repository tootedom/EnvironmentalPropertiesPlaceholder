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

import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentVariableReader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ClassPathResourceLoader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ResourceLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertArrayEquals;

/**
 * User: dominictootell
 * Date: 09/06/2012
 * Time: 09:11
 */
public abstract class AbstractTestPropertiesMergerBuilder {

    PropertiesMergerBuilder builder;

    public abstract PropertiesMergerBuilder createBuilder();

    @Before
    public void setup() {
        builder = createBuilder();
    }


    @Test
    public void testCanSetOutputtingPropertiesInDebugMode() {
        builder.setOutputtingPropertiesInDebugMode(true);
        assertTrue(builder.isOutputtingPropertiesInDebugMode());

        builder.setOutputtingPropertiesInDebugMode(false);
        assertFalse(builder.isOutputtingPropertiesInDebugMode());
    }

    @Test
    public void testCanSetApplicationName() {
        builder.setApplicationName("bob");
        assertEquals("bob",builder.getApplicationName());

        builder.setApplicationName("tom");
        assertEquals("tom",builder.getApplicationName());
    }

    @Test
    public void testCanSetSensitivePropertyMasker() {
        builder.setSensitivePropertyMasker(Pattern.compile("xxx"));
        Pattern p = builder.getSensitivePropertyMasker();
        assertEquals("xxx",p.pattern());

    }

    @Test
    public void testCanSetOperationsOverridesResourceLoader() {
        builder.setResourceLoaderForOperationalOverrides(new ClassPathResourceLoader("/xyz/xyz/"));
        ResourceLoader loader = builder.getResourceLoaderForOperationalOverrides();
        File f = loader.getFile("xx.properties");
        assertNotNull(f);
    }

    @Test
    public void testCanSetResourceLoaderForLoadingConfigurationProperties() {
        builder.setResourceLoaderForLoadingConfigurationProperties(new ClassPathResourceLoader("/xyz/xyz/"));
        ResourceLoader loader = builder.getResourceLoaderForLoadingConfigurationProperties();
        File f = loader.getFile("xx.properties");
        assertNotNull(f);
    }

//    @Test
//    public void testCanSetTrimmingPropertyValues() {
//        builder.setTrimmingPropertyValues(true);
//        assertTrue(builder.isTrimmingPropertyValues());
//        builder.setTrimmingPropertyValues(false);
//        assertFalse(builder.isTrimmingPropertyValues());
//    }
//
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

    @Test
    public void testCanSetExtensionForPropertiesFile() {
        builder.setExtensionForPropertiesFile("bob");
        assertEquals("bob",builder.getExtensionForPropertiesFile());
    }

    @Test
    public void testCanSetExtensionSeparatorCharForPropertiesFile() {
        builder.setExtensionSeparatorCharForPropertiesFile('{');
        assertEquals('{',builder.getExtensionSeparatorCharForPropertiesFile());
    }

    @Test
    public void testCanSetNameOfDefaultPropertiesFile() {
        builder.setNameOfDefaultPropertiesFile("conran");
        assertEquals("conran",builder.getNameOfDefaultPropertiesFile());
    }

    @Test
    public void testCanSetStrctMergingOfProperties() {
        builder.setStrictMergingOfProperties(true);
        assertTrue(builder.isStrictMergingOfProperties());
        builder.setStrictMergingOfProperties(false);
        assertFalse(builder.isStrictMergingOfProperties());
    }

    @Test
    public void testCanSetVariablesUsedForSwitchingConfigurationViaListOfLists() {
        String envKey = UUID.randomUUID().toString();

        List<List<String>> strings = new ArrayList<List<String>>();
        strings.add(Arrays.asList(new String[] { "SERVER_ENV" }));
        strings.add(Arrays.asList(new String[] { "SERVER_ENV",envKey }));

        builder.setVariablesUsedForSwitchingConfiguration(strings);

        testSwitchingVariables(envKey);
    }

    @Test
    public void testCanSetVariablesUsedForSwitchingConfigurationViaStringArray() {
        String envKey = UUID.randomUUID().toString();

        builder.setVariablesUsedForSwitchingConfiguration(new String[] {"SERVER_ENV","SERVER_ENV,"+envKey});

        testSwitchingVariables(envKey);
    }

    private void testSwitchingVariables(String additionalItem) {
        String[][] variables = builder.getVariablesUsedForSwitchingConfiguration();

        assertEquals(1,variables[0].length);
        assertEquals(2,variables[1].length);

        assertArrayEquals(new String[]{"SERVER_ENV"},variables[0]);
        assertArrayEquals(new String[]{"SERVER_ENV",additionalItem},variables[1]);
    }

    @Test
    public void testCanSetRelativeLocationOfFilesOverridingDefaultProperties() {
        builder.setRelativeLocationOfFilesOverridingDefaultProperties("env/parrot/cage/");
        assertTrue(builder.getRelativeLocationOfFilesOverridingDefaultProperties().contains("parrot"));
    }

    @Test
    public void testCanSetDelimiterUsedForSeparatingSwitchingConfigurationVariables() {
        builder.setDelimiterUsedForSeparatingSwitchingConfigurationVariables('p');
        assertEquals('p',builder.getDelimiterUsedForSeparatingSwitchingConfigurationVariables());
    }

    @Test
    public void testCanSetOperatingEnvironmentVariableReader() {
        builder.setOperatingEnvironmentVariableReader(new OperatingEnvironmentVariableReader() {
            @Override
            public String getProperty(String property, String defaultValue) {
                return "SystemProperty";
            }

            @Override
            public String getEnv(String property, String defaultValue) {
                return "EnvironmentProperty";
            }
        });

        OperatingEnvironmentVariableReader reader = builder.getOperatingEnvironmentVariableReader();

        assertEquals("SystemProperty",reader.getProperty("xxx","xxx"));
        assertEquals("EnvironmentProperty",reader.getEnv("xxx", "xxx"));
    }

//    @Test
//    public void testCatSetOperatingEnvironmentProperties() {
//        builder.setOperatingEnvironmentProperties(new OperatingEnvironmentProperties() {
//            @Override
//            public Properties getEnvironmentProperties() {
//                Properties p = new Properties();
//                p.setProperty("parrot","conran");
//                return p;
//            }
//
//            @Override
//            public Properties getSystemProperties() {
//                Properties p = new Properties();
//                p.setProperty("parrot","poppy");
//                return p;
//            }
//        });
//
//        OperatingEnvironmentProperties p = builder.getOperatingEnvironmentProperties();
//
//        assertEquals("poppy",p.getSystemProperties().get("parrot"));
//        assertEquals("conran",p.getEnvironmentProperties().get("parrot"));
//    }



}
