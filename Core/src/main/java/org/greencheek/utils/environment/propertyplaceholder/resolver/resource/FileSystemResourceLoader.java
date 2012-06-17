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

/**
 * User: dominictootell
 * Date: 20/05/2012
 * Time: 17:20
 */
public class FileSystemResourceLoader implements ResourceLoader {

    private final String baseLocation;

    public FileSystemResourceLoader(String configurationResourceLocation) {
        if(configurationResourceLocation==null || configurationResourceLocation.length()==0) {
            baseLocation = "/";
        }
        else {
            if (!configurationResourceLocation.startsWith("/") &&
                !configurationResourceLocation.startsWith("\\\\") &&
                configurationResourceLocation.indexOf(':') == -1)
                configurationResourceLocation = "/" + configurationResourceLocation;

            if (!configurationResourceLocation.endsWith("/")) {
                baseLocation = configurationResourceLocation + "/";
            } else {
                baseLocation = configurationResourceLocation;
            }
        }
    }

    /**
     * Given resource, which should be absolute from the filesystem root, if not
     * the method will adjust the resource to prepend with the root.
     *
     *
     * @param resource of the file to get from the file system
     * @return
     */
    public Resource getFile(String resource) {
        if (resource == null || resource.trim().length() == 0) return null;
        if (resource.startsWith("/")) resource = resource.substring(1);

        return new FileSystemResource(new File(baseLocation + resource).getAbsoluteFile());
    }

    @Override
    public String getBaseLocation() {
        return baseLocation;
    }

}
