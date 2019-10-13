
package com.github.armedis.http.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.math.NumberUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.config.ConstantNames;
import com.github.armedis.http.service.request.RedisRequestBuilder;
import com.github.armedis.http.service.response.ResponseCode;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;

public class BaseService implements ArmeriaAnnotatedHttpService {
    private static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ObjectMapper mapper = new ObjectMapper();

    protected HttpResponse buildResponse(ResponseCode responseCode) {
        return buildResponse(responseCode, null);
    }

    /**
     * Wrapper method of {@link #buildResponse(ResponseCode code, String resultData)} using {@link ResponseCode#SUCCESS} parameter.
     * 
     * @param resultData
     * @return
     */
    protected HttpResponse buildResponse(ObjectNode resultData) {
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
    protected final HttpResponse buildResponse(ResponseCode code, ObjectNode resultData) {
        if (resultData == null) {
            resultData = mapper.createObjectNode();
        }

        resultData.put(ConstantNames.RESULT_CODE, code.getResultCode());
        resultData.put(ConstantNames.RESULT_MESSAGE, code.getMessage());

        return HttpResponse.of(HttpStatus.valueOf(code.getStatusCode()), MediaType.JSON_UTF_8,
                resultData.toString());
    }

    protected final HttpResponse buildRespnse(RedisRequest redisRequest, ObjectNode resultData) {
        if (resultData == null) {
            resultData = mapper.createObjectNode();
        }

        ResponseCode code = ResponseCode.SUCCESS;

        resultData.put(ConstantNames.RESULT_CODE, code.getResultCode());
        resultData.put(ConstantNames.RESULT_MESSAGE, code.getMessage());

        return HttpResponse.of(HttpStatus.valueOf(code.getStatusCode()), MediaType.JSON_UTF_8,
                resultData.toString());
    }

    protected final RedisRequest buildRedisRequest(String redisCommand, HttpRequest req, JsonNode body) {
        RedisRequestBuilder builder = RedisRequestBuilder.setCommand(redisCommand);
        
        // build redisRequest by http request param.
//        builder.
        return null;
    }

    protected String unixTimestampToDateString(String unixtimestamp) {
        long timeStapm = NumberUtils.toLong(unixtimestamp);
        return LocalDateTime.ofEpochSecond(timeStapm, 0, ZoneOffset.ofHours(9)).format(pattern);
    }
}
