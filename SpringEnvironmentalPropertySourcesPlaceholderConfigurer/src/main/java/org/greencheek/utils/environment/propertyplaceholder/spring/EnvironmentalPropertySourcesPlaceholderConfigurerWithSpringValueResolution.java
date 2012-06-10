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

import org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesMergerBuilder;
import org.greencheek.utils.environment.propertyplaceholder.merger.PropertiesMerger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.*;

import java.io.IOException;
import java.util.Properties;

/**
 * User: dominictootell
 * Date: 20/05/2012
 * Time: 11:47
 */
public class EnvironmentalPropertySourcesPlaceholderConfigurerWithSpringValueResolution extends PropertySourcesPlaceholderConfigurer
    implements InitializingBean
{
    private final String ENVIRONMENT_SPECIFIC_PROPERTIES = "environmentSpecificProperties";
    private final PropertiesMergerBuilder mergerBuilder;
    private boolean systemPropertiesResolutionEnabled = true;
    private boolean environmentPropertiesResolutionEnabled = true;

    public EnvironmentalPropertySourcesPlaceholderConfigurerWithSpringValueResolution(PropertiesMergerBuilder builder) {
        mergerBuilder = builder;
    }

    @Override
    public void afterPropertiesSet()  {
        if(mergerBuilder==null) {
            throw new IllegalStateException("The PropertyMergerBuilder must not be null.");
        }

        Properties p = mergerBuilder.build().getMergedProperties();

        StandardEnvironment env = new StandardEnvironment();
        MutablePropertySources sources = new MutablePropertySources();

        if(isSystemPropertiesResolutionEnabled())
            sources.addLast(new MapPropertySource(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, env.getSystemProperties()));
        if(isEnvironmentPropertiesResolutionEnabled())
            sources.addLast(new SystemEnvironmentPropertySource(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,env.getSystemEnvironment()));

        sources.addFirst(new PropertiesPropertySource(ENVIRONMENT_SPECIFIC_PROPERTIES,p));
        super.setPropertySources(sources);
    }

    public boolean isSystemPropertiesResolutionEnabled() {
        return systemPropertiesResolutionEnabled;
    }

    public void setSystemPropertiesResolutionEnabled(boolean systemPropertiesResolutionEnabled) {
        this.systemPropertiesResolutionEnabled = systemPropertiesResolutionEnabled;
    }

    public boolean isEnvironmentPropertiesResolutionEnabled() {
        return environmentPropertiesResolutionEnabled;
    }

    public void setEnvironmentPropertiesResolutionEnabled(boolean environmentPropertiesResolutionEnabled) {
        this.environmentPropertiesResolutionEnabled = environmentPropertiesResolutionEnabled;
    }
}
