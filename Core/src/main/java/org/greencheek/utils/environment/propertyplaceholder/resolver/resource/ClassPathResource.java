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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * User: dominictootell
 * Date: 17/06/2012
 * Time: 17:54
 */
public class ClassPathResource implements Resource {

    private final URL resource;
    private final Logger log = LoggerFactory.getLogger(ClassPathResource.class);

    public ClassPathResource(URL resource) {
        this.resource = resource;
    }

    @Override
    public InputStream getStream() {
        if(resource == null) return null;
        InputStream is = null;
        try {
            is = resource.openStream();
        } catch(IOException e) {
            log.warn("Unable to open resource {}",resource);
        }
        return is;
    }

    @Override
    public boolean isAvailable() {
        if(resource==null) return false;
        else return true;
    }

    public String toString() {
        if(resource!=null)
            return resource.toString();
        else return null;
    }

}
