
package com.github.armedis.http.service.string;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.http.service.BaseService;
import com.github.armedis.http.service.request.RedisRequest;
import com.github.armedis.redis.RedisCommandExecutor;
import com.github.armedis.redis.command.RedisGetRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Consumes;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.Path;
import com.linecorp.armeria.server.annotation.Post;

/**
 * Redis get http request endpoint service.
 * @author krisjey
 *
 */
@Component
public class RedisSetService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(RedisSetService.class);

    private static final String REDIS_COMMAND = "set";

    private static final String COMMAND_URL = "/v1/" + REDIS_COMMAND;

    private static final String COMMAND_URL_WITH_KEY = COMMAND_URL + "/:key";

    @Autowired
    private RedisCommandExecutor executor;

    /**
     * Process set command request by x-www-form-urlencoded without redis key at URL.
     * @param redisRequest
     * @return HttpResponse
     */
    @Get
    @Post
    @Path(COMMAND_URL)
    @Consumes("application/x-www-form-urlencoded")
    public HttpResponse urlencodedWithoutKey(RedisGetRequest redisRequest) {
        logger.info("Text request " + REDIS_COMMAND + " command without key at URL " + redisRequest.toString());

        // execute redis command by http request params.
        ObjectNode result = executor.execute(redisRequest);

        return buildResponse(redisRequest, result);
    }

    /**
     * Process set command request by x-www-form-urlencoded with redis key at URL.
     * @param redisRequest
     * @return
     */
    @Get
    @Post
    @Path(COMMAND_URL_WITH_KEY)
    @Consumes("application/x-www-form-urlencoded")
    public HttpResponse urlencodedWithKey(RedisGetRequest redisRequest) {
        logger.info("Text request " + REDIS_COMMAND + " command without key at URL " + redisRequest.toString());

        // execute redis command by http request params.
        ObjectNode result = executor.execute(redisRequest);

        return buildResponse(redisRequest, result);
    }

    /**
     * Process set command request by application json without redis key at URL.
     * 
     * When request body is absent then JacksonRequestConverterFunction not working. <br/>
     * So, just use AggregatedHttpRequest.contentUtf8() method and convert to JsonNode.
     * @param httpRequest
     * @return HttpResponse
     */
    @Get
    @Post
    @Path(COMMAND_URL)
    @Consumes("application/json")
    public HttpResponse jsonWithoutKey(AggregatedHttpRequest httpRequest) {
        JsonNode jsonBody = getJsonBody(httpRequest);

        RedisRequest redisRequest = buildRedisRequest(REDIS_COMMAND, httpRequest, jsonBody);

        logger.info("Json request " + REDIS_COMMAND + " command without key at URL " + redisRequest.toString());

        ObjectNode result = executor.execute(redisRequest);

        return buildResponse(redisRequest, result);
    }

    /**
     * Process set command request by application json with redis key at URL.
     * 
     * When request body is absent then JacksonRequestConverterFunction not working. <br/>
     * So, just use AggregatedHttpRequest.contentUtf8() method and convert to JsonNode.
     * @param httpRequest
     * @param key
     * @return HttpResponse
     */
    @Post
    @Get
    @Path(COMMAND_URL_WITH_KEY)
    @Consumes("application/json")
    public HttpResponse jsonWithKey(AggregatedHttpRequest httpRequest, @Param("key") String key) {
        JsonNode jsonBody = getJsonBody(httpRequest);

        RedisRequest redisRequest = buildRedisRequest(REDIS_COMMAND, key, httpRequest, jsonBody);

        logger.info("Json request " + REDIS_COMMAND + " command with key at URL " + redisRequest.toString());

        ObjectNode result = executor.execute(redisRequest);

        return buildResponse(redisRequest, result);
    }
}
