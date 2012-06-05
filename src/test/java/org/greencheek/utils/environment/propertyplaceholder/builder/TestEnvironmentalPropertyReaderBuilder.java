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
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

/**
 * User: dominictootell
 * Date: 18/05/2012
 * Time: 17:18
 */
public class TestEnvironmentalPropertyReaderBuilder {

    PropertiesResolverBuilder builder;
    private final static String SIMPLE_PROPERTY_NAME = "simple";
    private final static String SIMPLE_PROPERTY_VALUE = "simplevalue";

    @Before
    public void setup() {
        builder = new EnvironmentSpecificPropertiesResolverBuilder(new ClassPathResourceLoader(""));
    }

    @Test
    public void testPotentialFileNames() {
        Map<String,String> env = System.getenv();
        String envKey = (String)env.keySet().toArray()[0];
        String envValue = env.get(envKey);
        System.setProperty("SERVER_ENV","xxx");
        builder.setVariablesUsedForSwitchingConfiguration(new String[] {"SERVER_ENV","SERVER_ENV,"+envKey});

        String[][] variables = builder.getVariablesUsedForSwitchingConfiguration();

        assertEquals(1,variables[0].length);
        assertEquals(2,variables[1].length);

        assertArrayEquals(new String[]{"SERVER_ENV"},variables[0]);
        assertArrayEquals(new String[]{"SERVER_ENV",envKey},variables[1]);
    }
}
