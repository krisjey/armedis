package com.github.armedis.http.service.string;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ RedisSetServiceTest.class, RedisGetServiceTest.class })
/**
 *  Test는 4종류 필요.COMMAND_URL_WITH_KEY, NOKEY, form-urlencoded, app/json
 *  get/post/put
 *  COMMAND_URL_WITH_KEY + "application/x-www-form-urlencoded"
 *  COMMAND_URL + "application/x-www-form-urlencoded"
 *  COMMAND_URL_WITH_KEY + "application/json"
 *  COMMAND_URL + "application/json"
 */
public class StringServiceTestSuite {
    static final String TEST_KEY = "keyname:nokey";
    static final String TEST_VALUE = "Hello world";
}
