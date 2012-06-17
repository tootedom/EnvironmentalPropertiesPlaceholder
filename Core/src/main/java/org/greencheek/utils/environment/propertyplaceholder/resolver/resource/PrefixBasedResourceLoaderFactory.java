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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Based on the given prefix contained on the string representing the location to load resources from
 * a resource loader is created to load those resources.  The prefixes that are currently supported are:
 * <ul>
 *     <li>classpath:</li>
 *     <li>filesystem:</li>
 * </ul>
 * </p>
 * <p>
 *     The default, if no prefix is specified, is to return a classpath resource.
 * </p>
 *
 * User: dominictootell
 * Date: 16/06/2012
 * Time: 16:07
 */
public class PrefixBasedResourceLoaderFactory implements ResourceLoaderFactory {

    public static Logger log = LoggerFactory.getLogger(PrefixBasedResourceLoaderFactory.class);

    enum Prefix {
        CLASSPATH("classpath:"),
        FILESYSTEM("filesystem:");

        private final String prefix;
        private final String prefixNoColon;

        Prefix(String prefix) {
            if(prefix == null) {
                throw new InstantiationError("prefix must not be null");
            } else {
                prefix = prefix.trim().toLowerCase();
                if(prefix.length()==0) {
                    throw new InstantiationError("prefix must not be an empty string");
                }
            }

            int i = prefix.lastIndexOf(':');
            if(i == prefix.length()-1) {
                this.prefix = prefix;
                this.prefixNoColon = prefix.substring(0,i);
            } else {
                this.prefix = prefix+":";
                this.prefixNoColon = prefix;
            }


        }

        public String getPrefix() {
            return this.prefix;
        }

        private String getPrefixNoColon() {
            return this.prefixNoColon;
        }

        public String toString() {
            return this.prefix;
        }

        public static Prefix fromString(String text) {
            if (text != null) {
                text = text.toLowerCase();
                for (Prefix b : Prefix.values()) {
                    if (b.getPrefix().equals(text) || b.getPrefixNoColon().equals(text)) {
                        return b;
                    }
                }
            }
            throw new IllegalArgumentException("No constant with prefix " + text + " found");
        }


    }


    @Override
    public ResourceLoader createResourceLoader(String locationToLoadResourcesFrom) {
        int colonLocation = locationToLoadResourcesFrom.indexOf(':');
        if(colonLocation==-1) {
            return new ClassPathResourceLoader(locationToLoadResourcesFrom);
        } else {
            String resourceType = locationToLoadResourcesFrom.substring(0,colonLocation);
            String resource = locationToLoadResourcesFrom.substring(colonLocation+1);
            try {
                switch (Prefix.fromString(resourceType))
                {
                    case CLASSPATH :
                        return new ClassPathResourceLoader(resource);
                    case FILESYSTEM:
                        return new FileSystemResourceLoader(resource);
                    default :
                        return new ClassPathResourceLoader(resource);
                }
            } catch(IllegalArgumentException e) {
                log.warn("unsupported resource type {} requested, returning ClassPathResourceLoader as a default",resourceType);
                return new ClassPathResourceLoader(locationToLoadResourcesFrom);
            }
        }
    }


}
