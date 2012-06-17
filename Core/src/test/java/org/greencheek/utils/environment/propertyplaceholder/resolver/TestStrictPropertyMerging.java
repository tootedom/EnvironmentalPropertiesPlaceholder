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

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import org.greencheek.utils.environment.propertyplaceholder.builder.EnvironmentSpecificPropertiesMergerBuilder;
import org.greencheek.utils.environment.propertyplaceholder.builder.EnvironmentSpecificPropertiesResolverBuilder;
import org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesMergerBuilder;
import org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesResolverBuilder;
import org.greencheek.utils.environment.propertyplaceholder.merger.EnvironmentSpecificPropertiesMerger;
import org.greencheek.utils.environment.propertyplaceholder.merger.PropertiesMerger;
import org.greencheek.utils.environment.propertyplaceholder.resolver.environment.OperatingEnvironmentVariableReader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.exception.NoMatchingPropertyException;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ClassPathResourceLoader;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.ValueResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.VariablePlaceholderValueResolver;
import org.greencheek.utils.environment.propertyplaceholder.resolver.value.VariablePlaceholderValueResolverConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * Tests that we can create the resolver to merge the properties in the following order:
 * <ol>
 *     <li>integration/config/environment.properties</li>
 *     <li>integration/config/override/environments/dev.properties</li>
 *     <li>integration/config/override/environments/dev.mac.properties</li>
 * </ol>
 * </p>
 * <p>
 *     The files are varied on the System property: ENV, and the Environment Property OS
 * </p>
 * <p>
 *     Resulting values will be:
 *     <pre>
 *         url=maclocalhost:8080
           appName=test
           environment=dev
           empty=
 *     </pre>
 * </p>
 *
 * User: dominictootell
 * Date: 09/06/2012
 * Time: 18:19
 */
public class TestStrictPropertyMerging {

    static PropertiesMergerBuilder strictMergerBuilder;
    static PropertiesMergerBuilder notStrictMergerBuilder;

    ByteArrayOutputStream output;
    OutputStreamAppender os;
    PatternLayoutEncoder enc;

    @Before
    public void setUp() {

        LoggerContext lc = new LoggerContext();
        Logger root = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        Logger green = (Logger) LoggerFactory.getLogger(EnvironmentSpecificPropertiesMerger.class);
        output = new ByteArrayOutputStream();

        green.setAdditive(true);
        os = new OutputStreamAppender<ILoggingEvent>();
        os.setName("test");
        enc = new PatternLayoutEncoder( );
        enc.setPattern("%msg%n");
        enc.setContext(green.getLoggerContext());
        enc.start();
        os.setEncoder(enc);
        os.setContext(green.getLoggerContext());
        os.setOutputStream(output);
        os.start();
        green.addAppender(os);

        System.clearProperty("CURRENT_ENV");
        System.setProperty("CURRENT_ENV","dev");
        strictMergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder()
        .setLocationForLoadingConfigurationProperties("classpath:/strictmerge")
        .setVariablesUsedForSwitchingConfiguration(new String[]{"CURRENT_ENV"})
        .setStrictMergingOfProperties(true);

        notStrictMergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder()
                .setLocationForLoadingConfigurationProperties("classpath:/strictmerge")
                .setVariablesUsedForSwitchingConfiguration(new String[]{"CURRENT_ENV"})
                .setStrictMergingOfProperties(false);
    }

    @After
    public void tearDown() {
        enc.stop();
        os.stop();

    }

    @Test(expected = NoMatchingPropertyException.class)
    public void testPropertiesAreNotResolvedDueToStrictMerging()
    {
        strictMergerBuilder.build();
    }

    @Test
    public void testPropertiesAreResolved()
    {
        notStrictMergerBuilder.build();
        assertTrue(new String(output.toByteArray()).contains("NoMatchingPropertyWarning"));
    }
}
