
package com.github.armedis.http.service.management;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServerTest;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpStatus;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class ArmedisActuatorTest extends AbstractRedisServerTest {
    @Test
    void testActuator() throws JsonParseException, JsonMappingException, IOException {
        AggregatedHttpResponse res = null;
        String response = null;

        // spring actuator health 체크.
        res = client.get("/actuator").aggregate().join();
        assertThat(res.status()).isEqualTo(HttpStatus.OK);

        response = res.content().toStringUtf8();

        assertThatJson(response)
                .as("Check requestUrl of result")
                .node("_links").isPresent();

        assertThatJson(response)
                .as("Check requestUrl of result")
                .node("_links.self.href").isPresent();

        assertThatJson(response)
                .as("Check requestUrl of result")
                .node("_links.self.href").isEqualTo("/actuator");
    }
}
