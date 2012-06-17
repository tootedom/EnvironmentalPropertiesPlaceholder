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
package org.greencheek.utils.environment.propertyplaceholder.resolver.builder;

import org.greencheek.utils.environment.propertyplaceholder.resolver.value.VariablePlaceholderValueResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.VariablePlaceholderValueResolverConfig;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * User: dominictootell
 * Date: 17/06/2012
 * Time: 15:13
 */
public class TestCompositeBuilder{

    ResolvedPropertiesBuilder defaultComposite;
    ResolvedPropertiesBuilder customComposite;

    @Before
    public void setUp() {

        System.setProperty("ENV_TYPE","prod");
        System.setProperty("ENV","prod");
        defaultComposite = new CompositeResolvedPropertiesBuilder();
        ((CompositeResolvedPropertiesBuilder)defaultComposite).setLocationForLoadingOperationalOverrides("classpath:/nonexistent");

        CompositeResolvedPropertiesBuilder customComposite = new CompositeResolvedPropertiesBuilder();
        customComposite.setLocationForLoadingOperationalOverrides("classpath:/composite/opsoverrides")
                .setLocationForLoadingConfigurationProperties("classpath:/composite")
                .setRelativeLocationOfFilesOverridingDefaultProperties("environmental/")
                .setVariablesUsedForSwitchingConfiguration(new String[] {"ENV_TYPE"})
                .setApplicationName("hi")
                .setStrictMergingOfProperties(false)
                .setExtensionForPropertiesFile(".props")
                .setExtensionSeparatorCharForPropertiesFile('-')
                .setDelimiterUsedForSeparatingSwitchingConfigurationVariables('-');



        customComposite.setPropertyValueResolver(new VariablePlaceholderValueResolver(new VariablePlaceholderValueResolverConfig().setEnvironmentPropertiesResolutionEnabled(false)))
                .setTrimmingPropertyValues(true);

        this.customComposite = customComposite;
    }


    /**
     * Tests for the properties:
     * default=default
     * platform=prod
     * ops-override-env=basedefault
     * ops-override=basedefault
     *
     * @param p
     */
    private void testForDefaults(Properties p) {
        assertEquals("default",p.getProperty("default"));
        assertEquals("prod",p.getProperty("platform"));
        assertEquals("basedefault",p.getProperty("ops-override"));
    }


    @Test
    public void testCompositeDefaultsReturnExpectedProperties() {
        testForDefaults(defaultComposite.buildResolvedProperties());

        assertEquals("basedefault",defaultComposite.buildResolvedProperties().getProperty("ops-override-env"));

    }

    @Test
    public void testCustomizedCompositeReturnsExpectedProperties() {
        Properties p = customComposite.buildResolvedProperties();

        testForDefaults(p);

        assertEquals("overridedefault",p.getProperty("ops-override-env"));
        assertEquals("${PATH}",p.getProperty("environment"));
        assertEquals("prod",p.getProperty("systemproperty"));

    }
}
