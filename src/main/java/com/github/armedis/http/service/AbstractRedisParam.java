
package com.github.armedis.http.service;

import java.util.Optional;

import com.linecorp.armeria.server.annotation.Param;

public abstract class AbstractRedisParam {
    @Param("key")
    protected Optional<String> key;

    @Param("command")
    protected Optional<String> command;
}
