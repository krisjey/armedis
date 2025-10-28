
package com.github.armedis.http.service.management;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.http.service.BaseService;
import com.github.armedis.http.service.ResponseCode;
import com.github.armedis.http.service.management.configs.AllowedConfigCommands;
import com.github.armedis.http.service.management.configs.AllowedConfigCommands.ConfigCommand;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.management.RedisConfigRequest;
import com.linecorp.armeria.common.HttpResponse;
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
public class RedisConfigsService extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String REDIS_COMMAND = "configs";

    private static final String COMMAND_URL = "/v1/management/settings/" + REDIS_COMMAND;

    private static final String COMMAND_URL_WITH_KEY = COMMAND_URL;

    @Get
    @Path(COMMAND_URL_WITH_KEY)
    @Consumes("application/x-www-form-urlencoded")
    public HttpResponse getUrlencodedWithKey(RedisConfigRequest redisRequest) {
        logger.info("Text request " + REDIS_COMMAND + " command without key at URL " + redisRequest.toString());

        String result = null;
        try {
            // TODO Redis Template로 전환 필요

//            // 클래스패스에서 configKeys.json 읽기
//            InputStream is = this.getClass().getResourceAsStream("/configKeys.json");
//            result = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            // TODO 1. 저장한 값을 변경.
            // TODO 2. 저장된 값을 조회. 최초 로드시

            if (AllowedConfigCommands.isInitialized()) {
                // do nothing
            }
            else {
                for (ConfigCommand configCommand : AllowedConfigCommands.all()) {
                    redisRequest.setKey(Optional.of(configCommand.getKey()));
                    RedisCommandExecuteResult commandResult = executeCommand(redisRequest);
                    ObjectNode loopResult = commandResult.toObjectNode();

                    configCommand.setCurrentValueFromDB(configCommand.parseValue(loopResult.get(RedisCommandExecuteResult.RESULT_KEY)));
                }

                AllowedConfigCommands.initialized();
            }

            result = AllowedConfigCommands.toJsonString();
        }
        catch (Exception e) {
            logger.error("Can not execute redis command ", e);
            return buildResponse(ResponseCode.UNKNOWN_ERROR, redisRequest);
        }

        return buildStatResponse(ResponseCode.SUCCESS, result);
    }
}
