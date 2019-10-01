package com.github.armedis.http.service;

import com.linecorp.armeria.server.annotation.Default;
import com.linecorp.armeria.server.annotation.Param;

public abstract class AbstractRedisParam {
    @Param("key")
    @Default("")
    protected String key;
    
    @Param("command")
    @Default("")
    protected String command;
}
