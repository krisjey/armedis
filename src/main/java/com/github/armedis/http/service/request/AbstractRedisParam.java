
package com.github.armedis.http.service.request;

import java.util.Optional;

import com.linecorp.armeria.server.annotation.Param;

public abstract class AbstractRedisParam {
    @Param("key")
    protected Optional<String> key;
}
