
package com.github.armedis.http.service.string;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServerTest;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpStatus;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class RedisGetServiceTest extends AbstractRedisServerTest {
    @Test
    void testSetAndGetCommand() throws JsonParseException, JsonMappingException, IOException {
        AggregatedHttpResponse response = null;
        String responseString = null;

        // set and test.
        response = client.get("/v1/get/" + StringServiceTestSuite.TEST_KEY).aggregate().join();

        assertThat(response.status()).isEqualTo(HttpStatus.OK);

        responseString = response.content().toStringUtf8();

        assertThat(responseString).isNotNull();

        assertThatJson(responseString)
                .as("Check result field in result json")
                .node(RedisCommandExecuteResult.RESULT_KEY).isPresent()
                .node("valueTest").isAbsent();

        /**
         *  Test는 4종류 필요.COMMAND_URL_WITH_KEY, NOKEY, form-urlencoded, app/json
         *  get/post/put
         *  COMMAND_URL_WITH_KEY + "application/x-www-form-urlencoded"
         *  COMMAND_URL + "application/x-www-form-urlencoded"
         *  COMMAND_URL_WITH_KEY + "application/json"
         *  COMMAND_URL + "application/json"
         */
    }
}
