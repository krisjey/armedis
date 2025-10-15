package com.github.armedis.http.service.string;

import org.junit.jupiter.api.Disabled;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testbed")
@Suite
@SelectClasses({ RedisSetServiceTest.class, RedisGetServiceTest.class })
@Disabled
public class TestSuiteForRedisString {

}
