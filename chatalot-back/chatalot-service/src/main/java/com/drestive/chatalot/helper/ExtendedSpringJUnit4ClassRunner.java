package com.drestive.chatalot.helper;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * For Unit tests, to set the environmental variable which is required to
 * configure spring application context
 *
 */
public class ExtendedSpringJUnit4ClassRunner extends SpringJUnit4ClassRunner {
    public ExtendedSpringJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);

        if (System.getProperty("APP_HOME") == null) System.setProperty("APP_HOME", "/home/mustafa/app");
    }
}
