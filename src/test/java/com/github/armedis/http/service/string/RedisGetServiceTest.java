
package com.github.armedis.http.service.string;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.armedis.http.service.BaseServiceTest;
import com.google.gson.JsonObject;

public class RedisGetServiceTest extends BaseServiceTest {
    private static final String HTTP_200_STATUS_LINE = "HTTP/1.1 200 OK";
    private static final String HTTP_405_STATUS_LINE = "HTTP/1.1 405 Method Not Allowed";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

//    @Ignore
    @Test
    public void testSetAndGetCommand() {
        String key = "hello:kris";

        // TEST get, put, post

        try (CloseableHttpClient hc = HttpClients.createMinimal()) {
            String setValue = "hello world";

            UrlEncodedFormEntity bodyData = new UrlEncodedFormEntity(
                    Collections.singletonList(new BasicNameValuePair("value", setValue)),
                    StandardCharsets.UTF_8);

            // data set to redis.
            HttpPut httpPut = new HttpPut(buildTestUrl("set", key));
            httpPut.setEntity(bodyData);
            try (CloseableHttpResponse res = hc.execute(httpPut)) {
                assertThat(res.getStatusLine().toString()).isEqualTo(HTTP_200_STATUS_LINE);

                JsonObject result = parseToJson(EntityUtils.toString(res.getEntity()));

                assertThat(result).isNotNull();
                assertThat(result.get("resultCode").getAsString()).isEqualTo("200");
                assertThat(result.get("value").getAsString()).isEqualTo("OK");
            }
            catch (Exception e) {
                fail("Can not connect to host");
            }

            // data get from redis.
            HttpGet httpGet = new HttpGet(buildTestUrl("get", key));
            try (CloseableHttpResponse res = hc.execute(httpGet)) {
                assertThat(res.getStatusLine().toString()).isEqualTo(HTTP_200_STATUS_LINE);

                JsonObject result = parseToJson(EntityUtils.toString(res.getEntity()));

                assertThat(result).isNotNull();
                assertThat(result.get("result").getAsString()).isEqualTo("200");
                assertThat(result.get("value").getAsString()).isEqualTo(setValue);
            }
            catch (Exception e) {
                fail("Can not connect to host");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
