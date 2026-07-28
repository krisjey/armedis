package com.github.armedis.http.service.management;

import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.github.armedis.http.service.management") // 기준 패키지
@IncludeClassNamePatterns(".*Test") // (선택) 클래스 이름 패턴 지정
public class ServiceTestSuite {
    /** 
     * activedefrag
     * maxmemory-policy
     * maxmemory-samples
     * maxmemory
     * timeout
     * maxclients
     * save
     * appendonly
     * lazyfree-lazy-expire
     * lazyfree-lazy-eviction
     * lazyfree-lazy-server-del
     */
}
