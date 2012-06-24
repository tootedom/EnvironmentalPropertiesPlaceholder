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
 * User: dominictootell
 * Date: 20/05/2012
 * Time: 17:09
 */
public interface ResourceLoader
{

    /**
     * Obtains a resource from the classpath.<br/><br/>
     * The classpath lookup is done in the following order:
     * <ul>
     * <li>It will lookup the resource using the class.getResource() with uses the class location
     * as the basis on which to locate the file. In other words a relative path to the current
     * package the current class lives in.  I.e The location of the given class (LoadServerSpecificProperties)</li>
     * <li>If it fails to find the resource in a relative location
     * then it will use the current threads classloader to load the resource; which means the
     * path is absolute (an absolution classpath location - will search from the root of the classpath),</li>
     * <li>If the current thread doesn't know about it, then it defaults to the current classloader that loaded this class;
     * to attempt to find it in a absolution location</li>
     * </ul>
     *
     *
     *
     * @param resource of the file to get from the classpath
     * @return The File object that represents the resource from the classpath
     */
    public Resource getFile(String resource);


    /**
     * This is the location from which the files retrieved via the method @link {#getFile} are resolved
     * @return the location from which files are resolved
     */
    public String getBaseLocation();

}
