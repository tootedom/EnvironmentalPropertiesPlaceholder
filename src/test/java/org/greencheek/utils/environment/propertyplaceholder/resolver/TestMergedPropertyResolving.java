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

import org.greencheek.utils.environment.propertyplaceholder.builder.EnvironmentSpecificPropertiesMergerBuilder;
import org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesMergerBuilder;
import org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesResolverBuilder;
import org.greencheek.utils.environment.propertyplaceholder.builder.SystemAndEnvironmentSpecificPropertiesResolverBuilder;
import org.greencheek.utils.environment.propertyplaceholder.merger.PropertiesMerger;
import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentProperties;
import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentVariableReader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ClassPathResourceLoader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.ValueResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.VariablePlaceholderValueResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.VariablePlaceholderValueResolverConfig;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * <p>
 * Tests that we can create the resolver to merge the properties in the following order:
 * <ol>
 *     <li>integration/config/environment.properties</li>
 *     <li>integration/config/override/environments/dev.properties</li>
 *     <li>integration/config/override/environments/dev.mac.properties</li>
 * </ol>
 * </p>
 * <p>
 *     The files are varied on the System property: ENV, and the Environment Property OS
 * </p>
 * <p>
 *     Resulting values will be:
 *     <pre>
 *         url=maclocalhost:8080
           appName=test
           environment=dev
           empty=
 *     </pre>
 * </p>
 *
 * User: dominictootell
 * Date: 09/06/2012
 * Time: 18:19
 */
public class TestMergedPropertyResolving {

    static PropertiesResolver resolver;

    @BeforeClass
    public static void setUp() {
        System.clearProperty("OS");
        PropertiesMergerBuilder mergerConfiguration = new EnvironmentSpecificPropertiesMergerBuilder(new ClassPathResourceLoader("integration/config/"));
        PropertiesMerger mergedProperties = mergerConfiguration.setNameOfDefaultPropertiesFile("environment")
        .setRelativeLocationOfFilesOverridingDefaultProperties("override/environments/")
        .setVariablesUsedForSwitchingConfiguration(new String[]{"ENV","ENV,OS"})
        .setOutputtingPropertiesInDebugMode(true)
        .setOperatingEnvironmentVariableReader(new OperatingEnvironmentVariableReader() {
            @Override
            public String getProperty(String property, String defaultValue) {
                if(property.equals("ENV"))
                    return "dev";
                else return defaultValue;
            }

            @Override
            public String getEnv(String property, String defaultValue) {
                return "mac";
            }
        }).build();

        ValueResolver valueResolver = new VariablePlaceholderValueResolver(
                new VariablePlaceholderValueResolverConfig().
                        setEnvironmentPropertiesResolutionEnabled(true).
                        setSystemPropertiesResolutionEnabled(true)
                        );

        PropertiesResolverBuilder resolverConfig = new SystemAndEnvironmentSpecificPropertiesResolverBuilder();
        resolver = resolverConfig.setTrimmingPropertyValues(true)
                .setPropertyValueResolver(valueResolver).build(mergedProperties);
    }

    @Test
    public void testPropertiesAreResolved()
    {
        Properties p = new Properties();

        p.setProperty("url","maclocalhost:8080");
        p.setProperty("appName","test");
        p.setProperty("environment","dev");
        p.setProperty("empty","");

        assertEquals("maclocalhost:8080",resolver.getProperty("url"));
        assertEquals("test",resolver.getProperty("appName"));
        assertEquals("dev",resolver.getProperty("environment"));
        assertEquals("",resolver.getProperty("empty"));
    }
}
