
package com.github.armedis.http.service.request;

import java.util.Optional;

import com.linecorp.armeria.server.annotation.Default;
import com.linecorp.armeria.server.annotation.Header;
import com.linecorp.armeria.server.annotation.Param;

public class AbstractRedisParam {
    @Param("key")
    protected Optional<String> key;

    @Header("Accept")
    @Default("json")
    protected String responseDataType;
}
