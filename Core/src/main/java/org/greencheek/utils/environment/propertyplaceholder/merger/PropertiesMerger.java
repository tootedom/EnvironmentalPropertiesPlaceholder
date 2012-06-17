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
package org.greencheek.utils.environment.propertyplaceholder.merger;

import java.util.Map;
import java.util.Properties;

/**
 * <p>
 * Interface that represents the service of obtain the set of properties that
 * have be sourced from the set of properties that are available.
 * </p>
 * <p>
 * How the properties are located is specific to the implementation.  However, the basic single implementation
 * is as follows: <br/>
 * <br/>
 * The properties are obtained first from either a location on the classpath or filesystem.  These set of
 * properties can vary depending upon a given set of environment or system variables: i.e:
 * <ol>
 *     <li>Read default.props</li>
 *     <li>Override the properties set in default.props with the contents
 *     of a .properties file in the environments/ folder that represents the current value of the ENV system property, i.e. dev.properties</li>
 * </ol>
 * The properties files that are read that override default can vary not only on one ENV variable but could vary on several:
 * <ol>
 *     <li>Read default.props</li>
 *     <li>Override the properties set in default.props with the contents
 *     of a .properties file in the environments/ folder that represents the current value of the ${ENV} system property, i.e. dev.properties</li>
 *     <li>Override dev.properties with a .properties file that represents the current value of ${ENV}.${OS.ARCH} system and env properties i.e. dev.x86_64.properties</li>
 * </ol>
 * The obtained properties' values are not modified in any way.  Variables contained within the values should not be
 * modified or resolved.
 * </p>
 * <p>
 *     Implementations must be thread safe and usable across threads, and is the reason no setter exists in the interface
 * </p>
 *
 * User: dominictootell
 * Date: 09/06/2012
 * Time: 11:58
 */
public interface PropertiesMerger {
    /**
     * This will return the properties
     * @return
     */
    Properties getMergedProperties();
    Map<String,String> getMergedPropertiesAsMap();
}
