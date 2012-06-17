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
package org.greencheek.utils.environment.propertyplaceholder.merger;

import org.greencheek.utils.environment.propertyplaceholder.builder.EnvironmentSpecificPropertiesMergerBuilder;
import org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesMergerBuilder;
import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentVariableReader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ClassPathResourceLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * User: dominictootell
 * Date: 05/06/2012
 * Time: 14:51
 */
public class TestMergingPropertiesOnOsArchAndEnvironment
{
    private PropertiesMergerBuilder resolverEnvAndOsBuilder;
    private PropertiesMergerBuilder resolverTargetPlatformBuilder;
    private PropertiesMergerBuilder resolverTargetPlatformWithOpsOverridesBuilder;


    @Before
    public void setUp() {
        System.setProperty("ENV","dev");
        System.setProperty("TARGET_PLATFORM","production");

        resolverEnvAndOsBuilder = new EnvironmentSpecificPropertiesMergerBuilder(new ClassPathResourceLoader("/"));
        resolverEnvAndOsBuilder.setVariablesUsedForSwitchingConfiguration(new String[] {"ENV","ENV,os.arch"});
        resolverEnvAndOsBuilder.setOperatingEnvironmentVariableReader(new OperatingEnvironmentVariableReader() {
            @Override
            public String getProperty(String property, String defaultValue) {
                if(property.equals("ENV")) { return "dev"; }
                else return defaultValue;
            }

            @Override
            public String getEnv(String property, String defaultValue) {
                return "x86_64";
            }
        });
        resolverEnvAndOsBuilder.setRelativeLocationOfFilesOverridingDefaultProperties("");

        resolverTargetPlatformBuilder = new EnvironmentSpecificPropertiesMergerBuilder(new ClassPathResourceLoader("/config"));
        resolverTargetPlatformBuilder.setVariablesUsedForSwitchingConfiguration(new String[] {"TARGET_PLATFORM"});
        resolverTargetPlatformBuilder.setOperatingEnvironmentVariableReader(new OperatingEnvironmentVariableReader() {
            @Override
            public String getProperty(String property, String defaultValue) {
                return "production";
            }

            // will never get here
            @Override
            public String getEnv(String property, String defaultValue) {
                return null;
            }
        });

        resolverTargetPlatformBuilder.setRelativeLocationOfFilesOverridingDefaultProperties("environments");


        resolverTargetPlatformWithOpsOverridesBuilder = new EnvironmentSpecificPropertiesMergerBuilder(new ClassPathResourceLoader("/overrides_testing/config"));
        resolverTargetPlatformWithOpsOverridesBuilder.setVariablesUsedForSwitchingConfiguration(new String[] {"TARGET_PLATFORM"});
        resolverTargetPlatformWithOpsOverridesBuilder.setNameOfDefaultPropertiesFile("environment");
        resolverTargetPlatformWithOpsOverridesBuilder.setResourceLoaderForOperationalOverrides(new ClassPathResourceLoader("/overrides_testing/config/platform_opsoverrides/config"));
        resolverTargetPlatformWithOpsOverridesBuilder.setOperatingEnvironmentVariableReader(new OperatingEnvironmentVariableReader() {
            @Override
            public String getProperty(String property, String defaultValue) {
                return "production";
            }

            // will never get here
            @Override
            public String getEnv(String property, String defaultValue) {
                return null;
            }
        });

        resolverTargetPlatformWithOpsOverridesBuilder.setRelativeLocationOfFilesOverridingDefaultProperties("environments");

    }

    @Test
    public void testPropertiesMergedOnServerEnvAndOsArch() {
        PropertiesMerger resolverEnvAndOs = resolverEnvAndOsBuilder.build();

        Properties p = resolverEnvAndOs.getMergedProperties();
        Map<String,String> actual = new HashMap(p);
        assertTrue(actual.size()==3);

        assertThat(actual, allOf(
                hasEntry("default", "default"),
                hasEntry("dev", "dev"),
                hasEntry("os.arch", "x86_64")
        ));
    }

    @Test
    public void testPropertiesMergedOnTargetPlatform() {
        PropertiesMerger resolverTargetPlatform  =  resolverTargetPlatformBuilder.build();
        Properties p = resolverTargetPlatform.getMergedProperties();
        Map<String,String> actual = new HashMap(p);
        assertTrue(actual.size()==4);

        assertThat(actual, allOf(
                hasEntry("default", "default"),
                hasEntry("platform", "production")
        ));
    }

    @Test
    public void testPropertiesMergedOnTargetPlatformWithOperationsOverrides() {
        PropertiesMerger resolverTargetPlatform  =  resolverTargetPlatformWithOpsOverridesBuilder.build();
        Properties p = resolverTargetPlatform.getMergedProperties();
        Map<String,String> actual = new HashMap(p);
        assertTrue(actual.size()==4);

        assertThat(actual, allOf(
                hasEntry("default", "environment"),
                hasEntry("platform", "production"),
                hasEntry("override", "environment_override"),
                hasEntry("override_platform","override_production")
        ));
    }
}
