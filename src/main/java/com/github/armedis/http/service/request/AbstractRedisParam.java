
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

    /**
     * set value at constructor 
     */
    protected String requestMethod;

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
    public String getRequestMethod() {
        return requestMethod;
    }

    /**
     * @param requestMethod the requestMethod to set
     */
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
}
