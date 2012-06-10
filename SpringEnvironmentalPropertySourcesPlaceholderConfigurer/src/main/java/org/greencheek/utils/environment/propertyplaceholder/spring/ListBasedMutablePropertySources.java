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
package org.greencheek.utils.environment.propertyplaceholder.spring;

import org.springframework.core.env.*;

import java.util.List;

/**
 * User: dominictootell
 * Date: 10/06/2012
 * Time: 17:07
 */
public class ListBasedMutablePropertySources extends MutablePropertySources {


    public ListBasedMutablePropertySources(List<PropertySource> propertySources) {
        super();
        for(PropertySource p : propertySources) {
            addLast(p);
        }
    }

    public ListBasedMutablePropertySources(List<PropertySource> customPropertySources,PropertySources propertySources) {
        super();
        for(PropertySource p : propertySources) {
            addLast(p);
        }

        for(PropertySource p : propertySources) {
            addLast(p);
        }
    }

    public static PropertySource getSystemProperties() {
        StandardEnvironment env = new StandardEnvironment();
        return new MapPropertySource(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, env.getSystemProperties());
    }

    public static PropertySource getSystemEnvironmentProperties() {
        StandardEnvironment env = new StandardEnvironment();
        return new SystemEnvironmentPropertySource(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,env.getSystemEnvironment());
    }
}
