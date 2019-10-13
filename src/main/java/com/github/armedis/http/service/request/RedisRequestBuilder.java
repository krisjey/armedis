
package com.github.armedis.http.service.request;

import static java.util.Objects.requireNonNull;

import com.github.armedis.http.service.RedisRequest;

public class RedisRequestBuilder {
    private RedisRequestBuilderFactory factory;
    
    private String command;
    
    public RedisRequestBuilder(String command) {
        this.command = requireNonNull(command, "Command should be not null!");
    }
    
    public RedisRequest build() {
        if (this.command != null) {
            //            return factory.newRequest(uri, HttpClient.class, buildOptions());
//            return factory.newBuilder(this.command);
            factory.newCommandBuilder(this.command);
            
            
        }
        else {
            //            return factory.newRequest(scheme, endpoint, HttpClient.class, buildOptions());
        }
    }
}
