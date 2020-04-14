
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
        
        AggregatedHttpResponse res;

        res = client.get("/pual/v1/index").aggregate().join();

        assertThat(res.status()).isEqualTo(HttpStatus.OK);
        assertThatJson(res.content().toStringUtf8())
                .as("Check requestUrl of result")
                .node("requestUrl").isStringEqualTo("/pual/v1/index")
                .node("value").isStringEqualTo("hello world");
        
        // spring actuator health 체크.
//        final AggregatedHttpResponse res = client.get("/internal/actuator/health").aggregate().join();
//        assertThat(res.status()).isEqualTo(HttpStatus.OK);
//
//        final Map<String, Object> values = mapper.readValue(res.content().array(), JSON_MAP);
//        assertThat(values).containsEntry("status", "UP");
//
//        String key = "hello:kris";

        // TEST get, put, post
//
//        try (CloseableHttpClient hc = HttpClients.createMinimal()) {
//            String setValue = "hello world";
//
//            UrlEncodedFormEntity bodyData = new UrlEncodedFormEntity(
//                    Collections.singletonList(new BasicNameValuePair("value", setValue)),
//                    StandardCharsets.UTF_8);
//
//            // data set to redis.
//            HttpPut httpPut = new HttpPut(buildTestUrl("set", key));
//            httpPut.setEntity(bodyData);
//            try (CloseableHttpResponse res = hc.execute(httpPut)) {
//                assertThat(res.getStatusLine().toString()).isEqualTo(HTTP_200_STATUS_LINE);
//
//                JsonObject result = parseToJson(EntityUtils.toString(res.getEntity()));
//
//                assertThat(result).isNotNull();
//                assertThat(result.get("resultCode").getAsString()).isEqualTo("200");
//                assertThat(result.get("value").getAsString()).isEqualTo("OK");
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//                fail("Can not connect to host");
//            }
//
//            // data get from redis.
//            HttpGet httpGet = new HttpGet(buildTestUrl("get", key));
//            try (CloseableHttpResponse res = hc.execute(httpGet)) {
//                assertThat(res.getStatusLine().toString()).isEqualTo(HTTP_200_STATUS_LINE);
//
//                JsonObject result = parseToJson(EntityUtils.toString(res.getEntity()));
//
//                assertThat(result).isNotNull();
//                assertThat(result.get("result").getAsString()).isEqualTo("200");
//                assertThat(result.get("value").getAsString()).isEqualTo(setValue);
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//                fail("Can not connect to host");
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
