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
package org.greencheek.utils.environment.propertyplaceholder.resolver.environment;

import java.util.Map;
import java.util.Properties;

/**
 * User: dominictootell
 * Date: 06/06/2012
 * Time: 22:51
 */
public class JavaPlatformOperatingEnvironmentProperties implements OperatingEnvironmentProperties {
    @Override
    public Properties getEnvironmentProperties() {
        Map<String,String> env = System.getenv();
        Properties properties = new Properties();
        properties.putAll(env);
        return properties;
    }

    @Override
    public Properties getSystemProperties() {
        return System.getProperties();
    }
}
