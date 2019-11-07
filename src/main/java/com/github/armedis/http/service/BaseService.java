
package com.github.armedis.http.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.config.ConstantNames;
import com.github.armedis.http.service.request.RedisRequest;
import com.github.armedis.http.service.request.RedisRequestBuilder;
import com.github.armedis.http.service.request.RedisRequestBuilderFactory;
import com.github.armedis.redis.RedisCommandExecutor;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;

public class BaseService implements ArmeriaAnnotatedHttpService {
    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    // CompositeRouter used.
    private static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    protected static final ObjectMapper mapper = new ObjectMapper();

    private static final ObjectNode emptyResult = mapper.createObjectNode();

    @Autowired
    private RedisCommandExecutor executor;

    protected HttpResponse buildResponse(ResponseCode responseCode) {
        return buildResponse(responseCode, null);
    }

    /**
     * Wrapper method of {@link #buildResponse(ResponseCode code, String resultData)} using {@link ResponseCode#SUCCESS} parameter.
     * 
     * @param resultData
     * @return
     */
    protected HttpResponse buildResponse(JsonNode resultData) {
        return buildResponse(ResponseCode.SUCCESS, resultData);
    }

    /**
     * Final message builder<br/>
     * Build {@link HttpResponse} object using {@code resultData} parameter.<br/>
     * Actual response writer
     * 
     * @param code
     * @param resultData
     * @return Object of {@link HttpResponse}
     */
    protected final HttpResponse buildResponse(ResponseCode code, JsonNode resultData) {
        if (resultData == null) {
            resultData = mapper.createObjectNode();
        }

        resultData.put(ConstantNames.RESULT_CODE, code.getResultCode());
        resultData.put(ConstantNames.RESULT_MESSAGE, code.getMessage());

        return HttpResponse.of(HttpStatus.valueOf(code.getStatusCode()), MediaType.JSON_UTF_8, resultData.toString());
    }

    protected final HttpResponse buildResponse(RedisRequest redisRequest, ObjectNode resultData) {
        if (resultData == null) {
            resultData = emptyResult;
        }

        ResponseCode code = ResponseCode.SUCCESS;

        resultData.put(ConstantNames.RESULT_CODE, code.getResultCode());
        resultData.put(ConstantNames.RESULT_MESSAGE, code.getMessage());

        return HttpResponse.of(HttpStatus.valueOf(code.getStatusCode()), MediaType.JSON_UTF_8, resultData.toString());
    }

    protected final RedisRequest buildRedisRequest(String redisCommand, String key, AggregatedHttpRequest httpRequest,
            JsonNode jsonBody) {
        RedisRequestBuilder builder = RedisRequestBuilderFactory.createRedisRequestBuilder(redisCommand);

        // Build RedisRequestVO by command name of the HttpRequest param.
        RedisRequest redisRequest = builder.build(jsonBody, key);

        return redisRequest;
    }

    protected final RedisRequest buildRedisRequest(String redisCommand, AggregatedHttpRequest httpRequest, JsonNode jsonBody) {
        RedisRequestBuilder builder = RedisRequestBuilderFactory.createRedisRequestBuilder(redisCommand);

        // Build RedisRequestVO by command name of the HttpRequest param.
        RedisRequest redisRequest = builder.build(jsonBody);

        return redisRequest;
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

    protected String parseKeyFromPath(String path) {
        return StringUtils.substringAfterLast(path, "/");
    }

    protected String unixTimestampToDateString(String unixtimestamp) {
        long timeStampValue = NumberUtils.toLong(unixtimestamp);
        return LocalDateTime.ofEpochSecond(timeStampValue, 0, ZoneOffset.ofHours(9)).format(pattern);
    }

    protected JsonNode executeCommand(RedisRequest redisRequest) {
        JsonNode node = null;

        try {
            node = executor.execute(redisRequest);
        }
        catch (Exception e) {
            logger.info("Can not execute redis command " + redisRequest.toString());
        }

        return node;
    }
}
