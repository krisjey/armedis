
package com.github.armedis.http.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.http.service.request.RedisRequest;
import com.github.armedis.http.service.request.RedisRequestBuilder;
import com.github.armedis.http.service.request.RedisRequestBuilderFactory;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisCommandExecutor;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.MediaType;

public class BaseService implements ArmeriaAnnotatedHttpService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected static final ObjectMapper mapper = new ObjectMapper();

    private static final ObjectNode emptyResult = mapper.createObjectNode();

    @Autowired
    private RedisCommandExecutor executor;

    protected HttpResponse buildStatResponse(ResponseCode responseCode, String stats) {
        return HttpResponse.of(responseCode.getStatusCode(), MediaType.JSON_UTF_8, stats);
    }

    protected HttpResponse buildResponse(ResponseCode responseCode, RedisRequest redisRequest) {
        return buildResponse(responseCode, redisRequest, null);
    }

    protected final HttpResponse buildResponse(RedisRequest redisRequest,
            RedisCommandExecuteResult redisCommandExecuteResult) {
        return buildResponse(ResponseCode.SUCCESS, redisRequest, redisCommandExecuteResult);
    }

    /**
     * Final message builder<br/>
     * Build {@link HttpResponse} object using {@code redisCommandExecuteResult}
     * parameter.<br/>
     * Actual response writer
     * 
     * @param code
     * @param redisCommandExecuteResult
     * @return Object of {@link HttpResponse}
     */
    protected final HttpResponse buildResponse(ResponseCode code, RedisRequest redisRequest,
            RedisCommandExecuteResult redisCommandExecuteResult) {

        // 응답 type에 따른 구분 처리.
        switch (redisRequest.getResponseDataType()) {
            case JSON:
                return buildJsonResponse(code, (redisCommandExecuteResult == null) ? emptyResult : redisCommandExecuteResult.toObjectNode());

            case PLAIN_TEXT:
                return buildPlainTextResponse(code, (redisCommandExecuteResult == null) ? RedisCommandExecuteResult.getEmptyResult("OK") : redisCommandExecuteResult);

            default:
                // default response type is json
                String resultMessage = "Can not detect response data type";
                logger.error(resultMessage);
                return buildJsonResponse(code, redisCommandExecuteResult.toObjectNode());
        }
    }

    private HttpResponse buildPlainTextResponse(ResponseCode code,
            RedisCommandExecuteResult redisCommandExecuteResult) {
        String responseData = redisCommandExecuteResult.toResponseString();
        return HttpResponse.of(code.getStatusCode(), MediaType.PLAIN_TEXT_UTF_8, responseData);
    }

    private HttpResponse buildJsonResponse(ResponseCode code, ObjectNode objectNode) {
        String responseData = null;
        try {
            responseData = mapper.writeValueAsString(objectNode);
        }
        catch (JsonProcessingException e) {
            logger.error("Can not convert string to JsonNode" + objectNode);
        }

        // TODO if response value is null then should be error response?
        if (responseData == null) {
            responseData = "{}";
        }

        return HttpResponse.of(code.getStatusCode(), MediaType.JSON_UTF_8, responseData);
    }

    protected final RedisRequest buildRedisRequest(String redisCommand, String key, AggregatedHttpRequest httpRequest,
            JsonNode jsonBody) {
        RedisRequestBuilder builder = RedisRequestBuilderFactory.createRedisRequestBuilder(redisCommand);

        // Build RedisRequestVO by command name of the HttpRequest param.
        RedisRequest redisRequest = builder.build(httpRequest, jsonBody, key);

        return redisRequest;
    }

    protected final RedisRequest buildRedisRequest(String redisCommand, AggregatedHttpRequest httpRequest,
            JsonNode jsonBody) {
        RedisRequestBuilder builder = RedisRequestBuilderFactory.createRedisRequestBuilder(redisCommand);

        // Build RedisRequestVO by command name of the HttpRequest param.
        RedisRequest redisRequest = builder.build(httpRequest, jsonBody);

        return redisRequest;
    }

    protected RedisCommandExecuteResult executeCommand(RedisRequest redisRequest) {
        RedisCommandExecuteResult redisCommandExecuteResult = null;

        // 명령에 해당하는 CommandExecutor 가져오기.
        try {
            redisCommandExecuteResult = executor.execute(redisRequest);
        }
        catch (Exception e) {
            logger.info("Can not execute redis command " + redisRequest.toString(), e);
        }

        return redisCommandExecuteResult;
    }

    protected final JsonNode getAsJsonBody(AggregatedHttpRequest httpRequest) {
        JsonNode jsonBody = null;
        String content = httpRequest.contentUtf8();

        if (StringUtils.isBlank(content)) {
            jsonBody = emptyResult;
        }
        else {
            try {
                jsonBody = mapper.readTree(content);
            }
            catch (IOException e) {
                logger.info("Can not read content from request body data! [" + content + "]");
                jsonBody = emptyResult;
            }
        }

        return jsonBody;
    }
}
