/**
 * 
 */
package com.github.armedis.http.service.management;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServerTest;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.common.RequestHeaders;

/**
 * 
 */
@ActiveProfiles("testbed")
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
class RedisClientListServiceTest extends AbstractRedisServerTest {

    @Test
    void test() throws JsonMappingException, JsonProcessingException {
        String responseString = null;

        RequestHeaders headers = RequestHeaders.builder()
                .method(HttpMethod.GET)
                .path("/v1/management/clientlist/10")
                .contentType(MediaType.FORM_DATA) // MediaType.FORM_DATA "application/x-www-form-urlencoded"
                .build();

        HttpRequest request = HttpRequest.of(headers);

        AggregatedHttpResponse response = client.execute(request).aggregate().join();

        System.out.println(MediaType.FORM_DATA);
        System.out.println(MediaType.PLAIN_TEXT_UTF_8);

        assertThat(response.status()).isEqualTo(HttpStatus.OK);

        responseString = response.content().toStringUtf8();

        assertThat(responseString).isNotNull();

//        JsonNode resultJson = mapper.readTree(responseString);

        assertThatJson(responseString)
                .as("Check result field in result json")
                .node(RedisCommandExecuteResult.RESULT_KEY).isPresent()
                .node(RedisCommandExecuteResult.RESULT_KEY).isArray();

    }

}
