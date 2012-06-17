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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * User: dominictootell
 * Date: 20/05/2012
 * Time: 17:10
 */
public class ClassPathResourceLoader implements ResourceLoader {

    public final String baseLocation;

    public ClassPathResourceLoader (String configurationResourceLocation) {

        if (configurationResourceLocation.indexOf('.')>-1)
            configurationResourceLocation = configurationResourceLocation.replace('.', '/');

        if (configurationResourceLocation != null && configurationResourceLocation.startsWith("/"))
            configurationResourceLocation = configurationResourceLocation.substring(1);

        if (configurationResourceLocation != null && !configurationResourceLocation.endsWith("/")) {
            configurationResourceLocation = configurationResourceLocation + "/";
        }

        if (configurationResourceLocation.length() == 1 && configurationResourceLocation.equals("/")) {
            this.baseLocation = "";
        }
        else {
            this.baseLocation = configurationResourceLocation;
        }
    }

    /**
     * Obtains a resource from the classpath.<br/><br/>
     * The classpath lookup is done in the following order:
     * <ul>
     *
     * <li>uses the current threads classloader to load the resource.  It will search from the root of the classpath</li>
     * <li>If the current thread doesn't know about it, then it defaults to the classloader that loaded this class.
     * This will still attempt to find the file from an absolute location</li>
     * </ul>
     *
     * @param resource of the file to get from the classpath
     * @return The File object that represents the resource from the classpath
     */
    public Resource getFile(String resource) {
        URL base = null;

        // strip out the starting '/'
        if (resource.startsWith("/")) resource = resource.substring(1);

        resource = baseLocation + resource;

        if (base == null) base = Thread.currentThread().getContextClassLoader().getResource(resource);
        if (base == null) base = ClassPathResourceLoader.class.getClassLoader().getResource(resource);

        //if (base == null) return null;


        return new ClassPathResource(base);
    }

    @Override
    public String getBaseLocation() {
        return baseLocation;
    }

    /**
     * Takes the URL object that represents a file object, and returns a File
     * representation
     *
     * @param fileurl the url representing the file
     * @return The File object representation from a URL
     */

    private static File getFile(URL fileurl) {
        try {
            return new File(toURI(fileurl.toString()).getSchemeSpecificPart());
        } catch (URISyntaxException ex) {
            // Fallback for URLs that are not valid URIs (should hardly ever happen).
            return new File(fileurl.getFile());
        }
    }

    /**
     * Changes spaces into %20.
     *
     * @param location The location of the file on the file system.  If it includes spaces they
     *                 are converted to %20
     * @return The location as a URI
     * @throws URISyntaxException
     */
    private static URI toURI(String location) throws URISyntaxException {
        return new URI(location.replaceAll(" ", "%20"));
    }


}
