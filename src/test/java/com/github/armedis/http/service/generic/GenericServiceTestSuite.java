package com.github.armedis.http.service.generic;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ RedisTtlServiceTest.class, RedisExpireServiceTest.class })
/**
 *  Test는 4종류 필요.COMMAND_URL_WITH_KEY, NOKEY, form-urlencoded, app/json
 *  get/post/put
 *  COMMAND_URL_WITH_KEY + "application/x-www-form-urlencoded"
 *  COMMAND_URL + "application/x-www-form-urlencoded"
 *  COMMAND_URL_WITH_KEY + "application/json"
 *  COMMAND_URL + "application/json"
 */
public class GenericServiceTestSuite {
    static final String TEST_KEY = "keyname:nokey";
    static final String TEST_VALUE = "Hello world";
}
