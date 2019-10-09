
package com.github.armedis.http.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.net.MediaType;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.annotation.Consumes;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Path;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.Produces;

/**
 * Service activity api.
 * @author krisjey
 *
 */
@Component
public class RedisGetService extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(RedisGetService.class);
    
    // http/command with key
    // http/command
    
    // json/command with key
    // json/command

    // consume을 메서드 안에서 처리.

    /**
     * Are you ok service for server instance.
     * @param redisRequest
     * @return
     */
    @Get
    @Post
    @Path(COMMAND_URL)
    @Consumes("application/x-www-form-urlencoded")
    //    @Produces("application/json;charset=UTF-8")
    public HttpResponse command(RedisRequest redisRequest) {
        logger.info(redisRequest.toString());
        logger.info(redisRequest.getCommand());
        return HttpResponse.of(HttpStatus.OK);
    }
    
    /**
     * Are you ok service for server instance.
     * @param redisRequest
     * @return
     */
    @Get
    @Post
    @Path(COMMAND_URL_WITH_KEY)
    @Consumes("application/x-www-form-urlencoded")
    //    @Produces("application/json;charset=UTF-8")
    public HttpResponse commandWithKey(RedisRequest redisRequest) {
        logger.info(redisRequest.toString());
        logger.info(redisRequest.getCommand());
        return HttpResponse.of(HttpStatus.OK);
    }
    
    @Post
    @Get
    @Path(COMMAND_URL_WITH_KEY)
    @Consumes("application/json")
    public HttpResponse ruokPost(JsonNode body) {
        logger.info(body.toString());
        return HttpResponse.of(HttpStatus.OK);
    }
}
