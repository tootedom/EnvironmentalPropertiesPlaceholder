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
import org.greencheek.utils.environment.propertyplaceholder.builder.PropertiesMergerBuilder;
import org.greencheek.utils.environment.propertyplaceholder.merger.EnvironmentSpecificPropertiesMerger;
import org.greencheek.utils.environment.propertyplaceholder.resolver.exception.NoDefaultPropertiesFileException;
import org.greencheek.utils.environment.propertyplaceholder.resolver.exception.NoMatchingPropertyException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;

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
public class TestStrictNoDefaults {

    static PropertiesMergerBuilder strictMergerBuilder;


    @Before
    public void setUp() {
        strictMergerBuilder = new EnvironmentSpecificPropertiesMergerBuilder()
        .setLocationForLoadingConfigurationProperties("classpath:/nodefaults")
        .setStrictMergingOfProperties(true);

    }

    @Test(expected = NoDefaultPropertiesFileException.class)
    public void testPropertiesAreNotResolvedDueToStrictMerging()
    {
        strictMergerBuilder.build();
    }


}
