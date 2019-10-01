package com.github.armedis.http.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.StringEscapeUtils;
import com.github.armedis.config.ConstantNames;
import com.github.armedis.http.service.response.ResponseCode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;

public class BaseService implements ArmeriaAnnotatedHttpService {
    private static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * EndPoint of API URL Constants.
     * 
     * @author krisjey
     *
     */
    protected final class ServiceUrl {
        /**
         * dummy api endpoint
         */
        public static final String HELLO_WORLD_GET = "/v1/hello/world/{userId}";

        /**
         * Server active check.
         */
        public static final String RUOK = "/v1/ruok";

        /**
         * Current memory status
         */
        public static final String FREE_MEMORY_GET = "/v1/free";
        
        /**
         * Current memory status
         */
        public static final String TEST_COMMAND = "/v1/{command}/{key}";
    }

    private static final Gson GSON_CONVERTER = new Gson();

    protected HttpResponse buildResponse(Map<String, ?> resultData) {
        return buildResponse((JsonObject) GSON_CONVERTER.toJsonTree(resultData));
    }

    protected HttpResponse buildResponse(ResponseCode responseCode) {
        return buildResponse(responseCode, null);
    }

    /**
     * Wrapper method of {@link #buildResponse(ResponseCode code, String resultData)} using {@link ResponseCode#SUCCESS} parameter.
     * 
     * @param resultData
     * @return
     */
    protected HttpResponse buildResponse(JsonObject resultData) {
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
    protected final HttpResponse buildResponse(ResponseCode code, JsonObject resultData) {
        if (resultData == null) {
            resultData = new JsonObject();
        }

        resultData.addProperty(ConstantNames.RESULT_CODE, code.getResultCode());
        resultData.addProperty(ConstantNames.RESULT_MESSAGE, code.getMessage());

        return HttpResponse.of(HttpStatus.valueOf(code.getStatusCode()), MediaType.JSON_UTF_8, resultData.toString());
    }

    protected void unescapeText(Map<String, Object> item, String key) {
        Object object = item.get(key);
        if (object == null) {
            // do nothing.
        }
        else {
            item.put(key, StringEscapeUtils.unescapeHtml4((String) object).replaceAll("&apos;", "'"));
        }
    }

    protected String unixTimestampToDateString(String unixtimestamp) {
        long timeStapm = NumberUtils.toLong(unixtimestamp);
        return LocalDateTime.ofEpochSecond(timeStapm, 0, ZoneOffset.ofHours(9)).format(pattern);
    }
}
