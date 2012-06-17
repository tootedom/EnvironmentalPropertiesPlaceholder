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

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import org.greencheek.utils.environment.propertyplaceholder.builder.EnvironmentSpecificPropertiesMergerBuilder;
import org.greencheek.utils.environment.propertyplaceholder.resolver.resource.ClassPathResourceLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertTrue;

/**
 * User: dominictootell
 * Date: 10/06/2012
 * Time: 10:58
 */
public class TestMaskedProperties {

    ByteArrayOutputStream output;
    OutputStreamAppender os;
    PatternLayoutEncoder enc;

    @Before
    public void setup() {
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
    }

    @Test
    public void testMaskedProperties()
    {
        PropertiesMerger merger = new EnvironmentSpecificPropertiesMergerBuilder().
                setOutputtingPropertiesInDebugMode(true).
                setNameOfDefaultPropertiesFile("masked").
                setExtensionForPropertiesFile("txt").
                setResourceLoaderForLoadingConfigurationProperties(new ClassPathResourceLoader("/")).
                setApplicationName("appname").build();

        String s = new String(output.toByteArray());
        assertTrue(s.contains("secret : ######"));
        assertTrue(s.contains("password : ######"));
        assertTrue(s.contains("bob : xxx"));

    }



    @After
    public void tearDown() {
        enc.stop();
        os.stop();

    }
}
