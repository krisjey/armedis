
package com.github.armedis.http.service.request;

import java.util.Optional;

import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.server.annotation.Default;
import com.linecorp.armeria.server.annotation.Header;
import com.linecorp.armeria.server.annotation.Param;

public class AbstractRedisParam {
    @Param("key")
    protected Optional<String> key;

    @Header("Accept")
    @Default("json")
    protected String responseDataType;

    /**
     * set value at constructor 
     */
    protected HttpMethod requestMethod;

    /**
     * @return the key
     */
    public String getKey() {
        return key.get();
    }

    /**
     * @param key the key to set
     */
    public void setKey(Optional<String> key) {
        this.key = key;
    }

    /**
     * @return the requestMethod
     */
    public HttpMethod getRequestMethod() {
        return requestMethod;
    }

    /**
     * @param httpMethod the requestMethod to set
     */
    public void setRequestMethod(HttpMethod httpMethod) {
        this.requestMethod = httpMethod;
    }
}
