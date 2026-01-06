
package com.github.armedis.http.service.string;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServerTest;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.common.RequestHeaders;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class RedisSetexServiceTest extends AbstractRedisServerTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testSetCommand() throws JsonParseException, JsonMappingException, IOException {
        // TODO data 응답 크기 제한 필요.

        String responseString = null;

        RequestHeaders headers = RequestHeaders.builder()
                .method(HttpMethod.POST)
                .path("/v1/setex/" + StringServiceTestSuite.TEST_KEY)
                .contentType(MediaType.FORM_DATA) // = "application/x-www-form-urlencoded"
                .build();

        // expire 300
        String formBody = "value=" + StringServiceTestSuite.TEST_VALUE + "&seconds=300";

        HttpRequest request = HttpRequest.of(headers, HttpData.ofUtf8(formBody));

        AggregatedHttpResponse response = client.execute(request).aggregate().join();

        assertThat(response.status()).isEqualTo(HttpStatus.OK);

        responseString = response.content().toStringUtf8();

        assertThat(responseString).isNotNull();

        assertThatJson(responseString)
                .as("Check result field in result json")
                .node(RedisCommandExecuteResult.RESULT_KEY).isPresent()
                .isEqualTo("OK");

        String value = stringRedisTemplate.opsForValue().get(StringServiceTestSuite.TEST_KEY);
        assertThat(value).isEqualTo(StringServiceTestSuite.TEST_VALUE);

        stringRedisTemplate.persist(StringServiceTestSuite.TEST_KEY);
    }
}
