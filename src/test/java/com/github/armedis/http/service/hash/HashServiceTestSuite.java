package com.github.armedis.http.service.hash;

import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
//@SelectClasses({ RedisTtlServiceTest.class, RedisHdelServiceTest.class })
@SelectPackages("com.github.armedis.http.service.management") // 기준 패키지
@IncludeClassNamePatterns(".*Test") // (선택) 클래스 이름 패턴 지정
/**
 *  Test는 4종류 필요.COMMAND_URL_WITH_KEY, NOKEY, form-urlencoded, app/json
 *  get/post/put
 *  COMMAND_URL_WITH_KEY + "application/x-www-form-urlencoded"
 *  COMMAND_URL + "application/x-www-form-urlencoded"
 *  COMMAND_URL_WITH_KEY + "application/json"
 *  COMMAND_URL + "application/json"
 */
public class HashServiceTestSuite {
    static final String TEST_KEY = "test:hashkey";
    static final String TEST_FIELD1 = "field1";
    static final String TEST_FIELD2 = "field2";
    static final String TEST_FIELD3 = "field3";
    static final String TEST_VALUE = "Hello world";
}
