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

/**
 * User: dominictootell
 * Date: 05/06/2012
 * Time: 15:14
 */
public class JavaPlatformOperatingEnvironmentVariableReader implements OperatingEnvironmentVariableReader {
    @Override
    public String getProperty(String property, String defaultValue) {
        String prop = System.getProperty(property,defaultValue);
        return (prop==null) ? "" : prop;
    }

    @Override
    public String getEnv(String property, String defaultValue) {
        String prop = System.getenv(property);
        if(null == prop) {
            if(defaultValue!=null)
                return defaultValue;
            else return "";
        }
        else
            return prop;
    }
}
