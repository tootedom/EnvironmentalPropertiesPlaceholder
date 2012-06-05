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

//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.BeanInitializationException;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//import org.springframework.core.env.*;

import java.io.IOException;
import java.util.Properties;

/**
 * User: dominictootell
 * Date: 20/05/2012
 * Time: 11:47
 */
public class EnvironmentalPropertySourcesPlaceholderConfigurer
        //extends PropertySourcesPlaceholderConfigurer
{
//    private final String ENVIRONMENT_SPECIFIC_PROPERTIES = "environmentSpecifyProperties";
//
//    public EnvironmentalPropertySourcesPlaceholderConfigurer(Properties p) {
//
//        StandardEnvironment env = new StandardEnvironment();
//        MutablePropertySources sources = new MutablePropertySources();
//
//        sources.addLast(new MapPropertySource(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, env.getSystemProperties()));
//        sources.addLast(new SystemEnvironmentPropertySource(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,env.getSystemEnvironment()));
//
//        sources.addFirst(new PropertiesPropertySource(ENVIRONMENT_SPECIFIC_PROPERTIES,p));
//        super.setPropertySources(sources);
//    }

    /**
     * {@linkplain #mergeProperties Merge}, {@linkplain #convertProperties convert} and
     * {@linkplain #processProperties process} properties against the given bean factory.
     * @throws BeanInitializationException if any properties cannot be loaded
     */
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        try {
//            Properties mergedProps = mergeProperties();
//
//            // Convert the merged properties, if necessary.
//            convertProperties(mergedProps);
//
//            // Let the subclass process the properties.
//            processProperties(beanFactory, mergedProps);
//        }
//        catch (IOException ex) {
//            throw new BeanInitializationException("Could not load properties", ex);
//        }
//    }

}
