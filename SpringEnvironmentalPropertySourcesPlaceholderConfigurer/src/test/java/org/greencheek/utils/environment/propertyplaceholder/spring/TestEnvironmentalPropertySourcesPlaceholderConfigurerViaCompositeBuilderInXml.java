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

import org.junit.After;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;

/**
 * User: dominictootell
 * Date: 10/06/2012
 * Time: 13:25
 */
public class TestEnvironmentalPropertySourcesPlaceholderConfigurerViaCompositeBuilderInXml {

    ClassPathXmlApplicationContext ctx;

    @Test
    public void testPropertyResolved() {
        System.setProperty("ENVIRONMENT","dev");
        System.setProperty("ENV", "dev");
        ctx = new ClassPathXmlApplicationContext(new String[]{"classpath:SpringPropertySourcesPlaceholderConfigurer-composite-config.xml"});


        String value = (String) ctx.getBean("string");
        String valueSys = (String) ctx.getBean("string-sys");
        String valueEnv = (String) ctx.getBean("string-env");

        String valuePath = (String)ctx.getBean("string-path");

        assertEquals("devproperties",value);

        assertEquals("dev",valueSys);
        assertEquals(System.getenv("PATH"), valueEnv);

        assertEquals(System.getenv("PATH"), valuePath);
    }

    @After
    public void tearDown() {
        if(ctx!=null) ctx.close();
    }



}
