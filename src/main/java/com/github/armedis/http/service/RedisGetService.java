
package com.github.armedis.http.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.redis.RedisCommandExecutor;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Consumes;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Path;
import com.linecorp.armeria.server.annotation.Post;

/**
 * Redis get http request endpoint service.
 * @author krisjey
 *
 */
@Component
public class RedisGetService extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(RedisGetService.class);

    protected static final String REDIS_COMMAND = "get";

    protected static final String COMMAND_URL = "/v1/" + REDIS_COMMAND;

    protected static final String COMMAND_URL_WITH_KEY = COMMAND_URL + "/{key}";

    @Autowired
    private RedisCommandExecutor executor;

    /**
     * Redis get command 
     * @param redisRequest
     * @return
     */
    @Get
    @Post
    @Path(COMMAND_URL)
    @Consumes("application/x-www-form-urlencoded")
    public HttpResponse command(RedisRequest redisRequest) {
        logger.info(redisRequest.getCommand() + "][" + redisRequest.toString());

        // execute redis command by http request params.
        ObjectNode result = executor.execute(redisRequest);

        return buildRespnse(redisRequest, result);
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
    public HttpResponse commandWithKey(RedisRequest redisRequest) {
        logger.info(redisRequest.getCommand() + "][" + redisRequest.toString());

        // execute redis command by http request params.
        ObjectNode result = executor.execute(redisRequest);

        return buildRespnse(redisRequest, result);
    }

    @Post
    @Get
    @Path(COMMAND_URL)
    @Consumes("application/json")
    public HttpResponse jsonCommand(HttpRequest req, JsonNode body) {
        logger.info(body.toString());

        // TODO make redisRequest by jsonNode
        RedisRequest redisRequest = buildRedisRequest(REDIS_COMMAND, req, body);

        ObjectNode result = executor.execute(redisRequest);

        return buildRespnse(redisRequest, result);
    }

    @Post
    @Get
    @Path(COMMAND_URL_WITH_KEY)
    @Consumes("application/json")
    public HttpResponse jsonCommandWithKey(HttpRequest req, JsonNode body) {
        logger.info(body.toString());

        // TODO make redisRequest by jsonNode
        RedisRequest redisRequest = buildRedisRequest(REDIS_COMMAND, req, body);

        ObjectNode result = executor.execute(redisRequest);

        return buildRespnse(redisRequest, result);
    }
}
