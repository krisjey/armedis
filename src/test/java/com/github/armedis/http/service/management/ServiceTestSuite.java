package com.github.armedis.http.service.management;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        ArmedisActuatorTest.class,
        RedisClientListServiceTest.class,
        RedisConfigServiceTest.class,
        RedisConfigsServiceTest.class,
        RedisLoginServiceTest.class,
})
public class ServiceTestSuite {
    
}
