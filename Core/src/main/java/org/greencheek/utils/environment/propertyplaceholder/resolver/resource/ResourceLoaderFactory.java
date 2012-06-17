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

/**
 * The resource loader factory provides a convenient mechanism from which to
 * create a resource loader that is able to load a resource from a given location.  The
 * idea of the resource loader is to take in a string and return a resource loader that is
 * for example capable of reading from the classpath or the file system based on the string prefix.
 *
 * User: dominictootell
 * Date: 16/06/2012
 * Time: 15:59
 */
public interface ResourceLoaderFactory
{

    /**
     * Based on the given string the factory will return a resource loader that is
     * ready to load resources from the given location specified.
     *
     * @param locationToLoadResourcesFrom
     * @return
     */
    public ResourceLoader createResourceLoader(String locationToLoadResourcesFrom);
}
