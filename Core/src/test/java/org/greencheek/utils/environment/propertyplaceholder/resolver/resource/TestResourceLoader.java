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
package org.greencheek.utils.environment.propertyplaceholder.resolver.resource;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * User: dominictootell
 * Date: 20/05/2012
 * Time: 17:07
 */
public class TestResourceLoader
{
    private ResourceLoader classPath;
    private ResourceLoader fileSystem;

    @Before
    public void setup() {
        classPath = new ClassPathResourceLoader("/");
        File currentLocation;
        try {
            currentLocation= new File(this.getClass().getClassLoader().getResource("xx.properties").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Resource f = classPath.getFile("/xx.properties");
        fileSystem = new FileSystemResourceLoader(currentLocation.getAbsoluteFile().getParentFile().getAbsolutePath());

    }


    @Test
    public void testClassPathResourceLoader() {
        Resource f = classPath.getFile("/xx.properties");
        assertNotNull(f);
        testProperties(f);
    }

    @Test
    public void testFileSystemResourceLoader() {
        Resource f2 = fileSystem.getFile("xx.properties");
        assertNotNull(f2);
        testProperties(f2);
    }


    public void testProperties(Resource f) {
        InputStream is = null;
        try {
            is = f.getStream();
            Properties p = new Properties();
            p.load(is);
            assertTrue(p.containsKey("message"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if(is!=null) {
                try {
                    is.close();
                } catch(IOException e) {}
            }

        }
    }


}
