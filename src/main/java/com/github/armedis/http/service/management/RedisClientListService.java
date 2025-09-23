
package com.github.armedis.http.service.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.armedis.http.service.BaseService;
import com.github.armedis.http.service.ResponseCode;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.management.RedisClientListRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Consumes;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Path;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.Put;

/**
 * Redis get http request endpoint service.
 * 
 * @author krisjey
 *
 */
@Component
public class RedisClientListService extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String REDIS_COMMAND = "clientlist";

    private static final String COMMAND_URL = "/v1/management/" + REDIS_COMMAND;

    private static final String COMMAND_URL_WITH_KEY = COMMAND_URL;

    @Get
    @Path(COMMAND_URL_WITH_KEY)
    @Consumes("application/x-www-form-urlencoded")
    public HttpResponse getUrlencodedWithKey(RedisClientListRequest redisRequest) {
        logger.info("Text request " + REDIS_COMMAND + " command without key at URL " + redisRequest.toString());

        // execute redis command by http request params.
        RedisCommandExecuteResult result = null;
        try {
            result = executeCommand(redisRequest);
        }
        catch (Exception e) {
            logger.error("Can not execute redis command ", e);
            return buildResponse(ResponseCode.UNKNOWN_ERROR, redisRequest);
        }

        return buildResponse(redisRequest, result);
    }

    /**
     * Process management command request by x-www-form-urlencoded with redis key at
     * URL.
     * 
     * @param redisRequest
     * @return
     */
    @Put
    @Post
    @Path(COMMAND_URL_WITH_KEY)
    @Consumes("application/x-www-form-urlencoded")
    public HttpResponse urlencodedWithKey(RedisClientListRequest redisRequest) {
        logger.info("Text request " + REDIS_COMMAND + " command without key at URL " + redisRequest.toString());

        // execute redis command by http request params.
        RedisCommandExecuteResult result = null;
        try {
            result = executeCommand(redisRequest);
        }
        catch (Exception e) {
            logger.error("Can not execute redis command ", e);
            return buildResponse(ResponseCode.UNKNOWN_ERROR, redisRequest);
        }

        return buildResponse(redisRequest, result);
    }
}
