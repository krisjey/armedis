
package com.github.armedis.http.service.string;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServiceTest;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpHeaderNames;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.common.RequestHeaders;

@ActiveProfiles("testbed")
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class RedisSetServiceTest extends AbstractRedisServiceTest {
    @Test
    void testSetAndGetCommand() throws JsonParseException, JsonMappingException, IOException {
        // TODO data 응답 크기 제한 필요.

        String response = null;
        
        // application/x-www-form-urlencoded Content-Type 지정
        RequestHeaders headers = RequestHeaders.builder()
                .method(HttpMethod.POST)
                .path("/v1/set/keyname:nokey")
                .contentType(MediaType.FORM_DATA) // = "application/x-www-form-urlencoded"
                .build();

     // form-urlencoded body 생성
        String formBody = "value=1759994898 hello world23";

        HttpRequest req = HttpRequest.of(headers, HttpData.ofUtf8(formBody));

        AggregatedHttpResponse res = client.execute(req)
                                           .aggregate()
                                           .join();
        
        /**
             * HttpResponse response = client.prepare()
             *                               .post("/foo")
             *                               .header(HttpHeaderNames.AUTHORIZATION, ...)
             *                               .content(MediaType.JSON, ...)
             *                               .execute();
         */
        assertThat(res.status()).isEqualTo(HttpStatus.OK);

        response = res.content().toStringUtf8();

        assertThat(response).isNotNull();

        assertThatJson(response)
                .as("Check result field in result json")
                .node("result").isPresent()
                .node("result").isEqualTo("OK");
    }
}
