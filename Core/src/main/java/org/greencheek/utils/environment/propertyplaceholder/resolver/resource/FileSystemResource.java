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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: dominictootell
 * Date: 17/06/2012
 * Time: 18:48
 */
public class FileSystemResource implements Resource{

    private Logger log = LoggerFactory.getLogger(FileSystemResource.class);
    private final File file;

    public FileSystemResource(File f) {
        this.file = f;
    }

    @Override
    public InputStream getStream() {
        if(file==null) {
            log.warn("FileSystemResource has been constructed with a null file");
            return null;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (IOException e) {
            log.warn("Unable to obtain stream for file: {}",file);
        }
        return fis;
    }

    @Override
    public boolean isAvailable() {
        if(file==null) return false;
        return file.canRead();
    }

    public String toString() {
        if(file!=null) return file.toString();
        else return null;
    }
}
