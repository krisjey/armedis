
package com.github.armedis.http.service.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.armedis.http.service.BaseService;
import com.github.armedis.http.service.ResponseCode;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.management.RedisNodeListRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.MediaTypeNames;
import com.linecorp.armeria.server.annotation.Consumes;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Path;

/**
 * Redis get http request endpoint service.
 * 
 * @author krisjey
 *
 */
@Component
public class RedisNodeListService extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String REDIS_COMMAND = "nodelist";

    private static final String COMMAND_URL = "/v1/management/" + REDIS_COMMAND;

    private static final String COMMAND_URL_WITH_KEY = COMMAND_URL;

    @Get
    @Path(COMMAND_URL_WITH_KEY)
    @Consumes(MediaTypeNames.FORM_DATA)
    public HttpResponse getUrlencodedWithKey(RedisNodeListRequest redisRequest) {
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
//
        return buildResponse(redisRequest, result);
    }
}
