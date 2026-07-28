
package com.github.armedis.http.service.hash;

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
public class RedisHexistsServiceTest extends AbstractRedisServerTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // TODO GET/POST/PUT 모두 테스트 필요.
    @Test
    void testHexistsCommand() throws JsonParseException, JsonMappingException, IOException {
        // set data for test
        stringRedisTemplate.opsForHash().put(HashServiceTestSuite.TEST_KEY, HashServiceTestSuite.TEST_FIELD3, HashServiceTestSuite.TEST_VALUE);

        String responseString = null;

        RequestHeaders headers = RequestHeaders.builder()
                .method(HttpMethod.GET)
                .path("/v1/hexists/" + HashServiceTestSuite.TEST_KEY)
                .contentType(MediaType.FORM_DATA) // = "application/x-www-form-urlencoded"
                .build();

        String formBody = "field=" + HashServiceTestSuite.TEST_FIELD3;

        HttpRequest request = HttpRequest.of(headers, HttpData.ofUtf8(formBody));

        AggregatedHttpResponse response = client.execute(request).aggregate().join();

        assertThat(response.status()).isEqualTo(HttpStatus.OK);

        responseString = response.content().toStringUtf8();

        assertThat(responseString).isNotNull();

        assertThatJson(responseString)
                .as("Check result field in result json")
                .node(RedisCommandExecuteResult.RESULT_KEY).isPresent()
                .isEqualTo(Boolean.TRUE);
    }
}
