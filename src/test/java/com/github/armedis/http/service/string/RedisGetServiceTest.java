
package com.github.armedis.http.service.string;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.armedis.http.service.BaseServiceTest;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.Server;

@RunWith(SpringRunner.class)
@ActiveProfiles("testbed")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class RedisGetServiceTest extends BaseServiceTest {
    @Inject
    private Server server;

    private WebClient client;

    @Before
    public void setup() {
        client = WebClient.of("http://localhost:" + server.activePort().localAddress().getPort());
    }

    private static final String HTTP_200_STATUS_LINE = "HTTP/1.1 200 OK";
    private static final String HTTP_405_STATUS_LINE = "HTTP/1.1 405 Method Not Allowed";

    private static final TypeReference<Map<String, Object>> JSON_MAP = new TypeReference<Map<String, Object>>() {
    };

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.setProperty("SERVICE_PORT", HTTP_PORT);
    }

//    @Ignore
    @Test
    public void testSetAndGetCommand() throws JsonParseException, JsonMappingException, IOException {
        // TODO data 응답 크기 제한 필요.

        AggregatedHttpResponse res = null;
        String response = null;

        // set and test.
        res = client.get("/v1/get/keyname:nokey").aggregate().join();

        assertThat(res.status()).isEqualTo(HttpStatus.OK);

        response = res.content().toStringUtf8();

        assertThat(response).isNotNull();

        assertThatJson(response)
                .as("Check requestUrl of result")
                .node("result").isPresent()
                .node("valueTest").isAbsent();

        // spring actuator health 체크.
        res = client.get("/actuator").aggregate().join();
        assertThat(res.status()).isEqualTo(HttpStatus.OK);

        response = res.content().toStringUtf8();

        assertThatJson(response)
                .as("Check requestUrl of result")
                .node("_links").isPresent();

//        final Map<String, Object> values = mapper.readValue(res.content().array(), JSON_MAP);
//        assertThat(values).containsEntry("status", "UP");
    }

}
