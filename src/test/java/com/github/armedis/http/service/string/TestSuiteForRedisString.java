package com.github.armedis.http.service.string;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({RedisSetServiceTest.class, RedisGetServiceTest.class})
public class TestSuiteForRedisString {

}
