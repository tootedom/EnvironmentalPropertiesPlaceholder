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
package org.greencheek.utils.environment.propertyplaceholder.resolver;

import java.util.Map;
import java.util.Properties;

/**
 * User: dominictootell
 * Date: 20/05/2012
 * Time: 13:43
 */
public interface PropertiesResolver {

    boolean isTrimmingPropertyValues();

    boolean isResolvingSystemProperties();

    boolean isResolvingEnvironmentVariables();

    /**
     * Returns a property from the map.
     * If a property does not exist null is will be returned
     *
     * @param simplePropertyName the name of the property to return
     * @return
     */
    String getProperty(String simplePropertyName);

    Properties getMergedProperties();
    Map<String,String> getMergedPropertiesAsMap();
}
