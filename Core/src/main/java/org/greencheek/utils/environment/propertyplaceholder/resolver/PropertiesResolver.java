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

    /**
     * Returns a property from the map. If a property does not exist null will be returned.
     * The returned property value will have had its value resolved of any placeholders (variables i.e. ${xxx}).
     * They will only remain if they failed to resolve.
     *
     * @param propertyName the name of the property to return
     * @return the property value, or null.
     */
    String getProperty(String propertyName);

    /**
     * Returns a property from the map. If a property does not exist null will be returned.
     * The returned property value will <b>NOT</b> have had its value resolved of any placeholders
     * (variables i.e. ${xxx}).
     *
     * @param propertyName the name of the property to return
     * @return the property value, or null.
     */
    String getUnResolvedProperty(String propertyName);


}
