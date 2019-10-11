
package com.github.armedis.http.service.request;

import com.github.armedis.http.service.RedisRequest;

public class RedisRequestBuilder {
    private RedisRequestFactory factory;
    private String command;

    public RedisRequestBuilder setCommand(String command) {
        this.command = command;
        
        return this;
    }

    public RedisRequest build() {
        if (uri != null) {
            return factory.newRequest(uri, HttpClient.class, buildOptions());
        }
        else if (path != null) {
            return factory.newRequest(scheme, endpoint, path, HttpClient.class, buildOptions());
        }
        else {
            return factory.newRequest(scheme, endpoint, HttpClient.class, buildOptions());
        }
    }
}
