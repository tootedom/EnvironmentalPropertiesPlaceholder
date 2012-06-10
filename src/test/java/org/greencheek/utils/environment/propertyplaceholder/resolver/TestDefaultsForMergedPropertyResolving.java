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
 * Tests that we can create the resolver to merge the properties in the following order,
 * Which are the defaults for the builders:
 * <ol>
 *     <li>/config/default.properties</li>
 *     <li>/config/environments/prod.properties</li>
 *     <li>/data/opsoverrides/appname/defaults.properties</li>
 *     <li>/data/opsoverrides/appname/environments/prod.properties</li>
 * </ol>
 * </p>
 * <p>
 *     The files are varied on the System property: ENV
 * </p>
 *
 * This is not a unit test, it relies on /data/opsoverrides/appname, i want to actually test that
 * the default read from the appropriate default locations.
 *
 * Looking for result of:
 * <pre>
 *     default=default
       platform=prod
       ops-override-env=prod
       ops-override=default
 * </pre>
 * /data/opsoverrides/appname/environments/prod.properties contains "ops-override-env=prod"
 * /data/opsoverrides/appname/default.properties contains "ops-override=default"
 *
 *
 * User: dominictootell
 * Date: 09/06/2012
 * Time: 18:19
 */
public class TestDefaultsForMergedPropertyResolving {

    static PropertiesResolver appNameResolver;
    static PropertiesResolver nullAppNameResolver;

    public static void main(String[] args) {
        setUp();
        testPropertiesWithAppNameAreResolved();
        testPropertiesWithoutAppNameAreResolved();
    }

    public static void setUp() {
        System.setProperty("ENV","prod");
        PropertiesMerger merger = new EnvironmentSpecificPropertiesMergerBuilder().
                                      setApplicationName("appname").build();


        appNameResolver = new SystemAndEnvironmentSpecificPropertiesResolverBuilder().build(merger);

        nullAppNameResolver = new SystemAndEnvironmentSpecificPropertiesResolverBuilder().build(
                new EnvironmentSpecificPropertiesMergerBuilder().build()
        );
    }



    public static void testPropertiesWithAppNameAreResolved()
    {
        assertEquals("default",appNameResolver.getProperty("default"));
        assertEquals("prod",appNameResolver.getProperty("platform"));
        assertEquals("prod",appNameResolver.getProperty("ops-override-env"));
        assertEquals("default",appNameResolver.getProperty("ops-override"));
    }

    public static void testPropertiesWithoutAppNameAreResolved()
    {
        assertEquals("default",nullAppNameResolver.getProperty("default"));
        assertEquals("prod",nullAppNameResolver.getProperty("platform"));
        assertEquals("basedefault",nullAppNameResolver.getProperty("ops-override-env"));
        assertEquals("basedefault",nullAppNameResolver.getProperty("ops-override"));
    }
}
