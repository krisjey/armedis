
package com.github.armedis.http.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CommandLookupTest {
    protected static final ApplicationContext springContext = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void test() {
        String[] beanNames = springContext.getBeanDefinitionNames();
    }

}
