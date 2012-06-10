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
package org.greencheek.utils.environment.propertyplaceholder.resolver.environment;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * User: dominictootell
 * Date: 10/06/2012
 * Time: 10:42
 */
public class TestEnvironmentVariablesReader {
    OperatingEnvironmentVariableReader reader;

    @Before
    public void setUp() {
        reader = new JavaPlatformOperatingEnvironmentVariableReader();
    }

    @Test
    public void testGetEnvironmentVariable() {
        assertNotNull(reader.getEnv("PATH",""));
        assertTrue(reader.getEnv("PATH", "").length() > 0);
        String path = System.getenv("PATH");
        assertEquals(path,reader.getEnv("PATH",""));

        assertEquals("defaultvalue",reader.getEnv(UUID.randomUUID().toString(),"defaultvalue"));
    }

    @Test
    public void testGetSystemProperty() {
        System.setProperty("ENV", "test");
        assertNotNull(reader.getProperty("ENV", ""));
        assertTrue(reader.getProperty("ENV", "").length() > 0);
        assertEquals("test",reader.getProperty("ENV",""));

        assertEquals("defaultvalue",reader.getProperty(UUID.randomUUID().toString(),"defaultvalue"));
    }

    @Test
    public void testNullSystemProperty() {
        System.clearProperty("ENV");
        assertNotNull(reader.getProperty("ENV", null));
        assertTrue(reader.getProperty("ENV", null).length() == 0);
        assertEquals("",reader.getProperty("ENV",null));

        assertEquals("",reader.getProperty(UUID.randomUUID().toString(),null));
    }

    @Test
    public void testNullEnvProperty() {
        System.clearProperty("ENV");
        assertNotNull(reader.getEnv("ENV", null));
        assertTrue(reader.getEnv("ENV", null).length() == 0);
        assertEquals("",reader.getEnv("ENV",null));

        assertEquals("",reader.getEnv(UUID.randomUUID().toString(),null));
    }
}
