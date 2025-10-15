
package com.github.armedis.http.service.string;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServerTest;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpStatus;

@ActiveProfiles("testbed")
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class RedisGetServiceTest extends AbstractRedisServerTest {
    @Test
    void testSetAndGetCommand() throws JsonParseException, JsonMappingException, IOException {
        AggregatedHttpResponse res = null;
        String response = null;

        // set and test.
        res = client.get("/v1/get/keyname:nokey").aggregate().join();

        assertThat(res.status()).isEqualTo(HttpStatus.OK);

        response = res.content().toStringUtf8();

        assertThat(response).isNotNull();

        assertThatJson(response)
                .as("Check result field in result json")
                .node("result").isPresent()
                .node("valueTest").isAbsent();
        
        /**
         *  Test는 4종류 필요.
         *  COMMAND_URL_WITH_KEY + "application/x-www-form-urlencoded"
         *  COMMAND_URL + "application/json"
         *  COMMAND_URL
         */
        
    }
}
