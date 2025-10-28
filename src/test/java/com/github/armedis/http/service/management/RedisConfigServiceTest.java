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

@ActiveProfiles("testbed")
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
class RedisConfigServiceTest extends AbstractRedisServerTest {

    private static final String MAXMEMORY_VALUE = "100MB";

    /**
     * activedefrag
     * maxmemory-policy
     * maxmemory-samples
     * maxmemory
     * timeout
     * maxclients
     * save
     * appendonly
     * lazyfree-lazy-expire
     * lazyfree-lazy-eviction
     * lazyfree-lazy-server-del
     */
    @Test
    void testTimeoutGet() {
        String responseString = null;

        RequestHeaders headers = RequestHeaders.builder()
                .method(HttpMethod.GET)
                .path("/v1/management/settings/config/timeout")
                .contentType(MediaType.FORM_DATA) // = "application/x-www-form-urlencoded"
                .build();

        HttpRequest request = HttpRequest.of(headers);

        AggregatedHttpResponse response = client.execute(request).aggregate().join();

        assertThat(response.status()).isEqualTo(HttpStatus.OK);

        responseString = response.content().toStringUtf8();

        assertThat(responseString).isNotNull();

        assertThatJson(responseString)
                .as("Check result field in result json")
                .node(RedisCommandExecuteResult.RESULT_KEY).isPresent()
                .node("result.timeout").isPresent();
    }

    @Test
    void testMaxmemoryGet() {
        String responseString = null;

        RequestHeaders headers = RequestHeaders.builder()
                .method(HttpMethod.GET)
                .path("/v1/management/settings/config/maxmemory")
                .contentType(MediaType.FORM_DATA) // = "application/x-www-form-urlencoded"
                .build();

        HttpRequest request = HttpRequest.of(headers);

        AggregatedHttpResponse response = client.execute(request).aggregate().join();

        assertThat(response.status()).isEqualTo(HttpStatus.OK);

        responseString = response.content().toStringUtf8();

        assertThat(responseString).isNotNull();

        assertThatJson(responseString)
                .as("Check result field in result json")
                .node(RedisCommandExecuteResult.RESULT_KEY).isPresent()
                .node("result.maxmemory").isPresent();
    }

    @Test
    void testMaxmemorySet() {
        String responseString = null;

        RequestHeaders headers = RequestHeaders.builder()
                .method(HttpMethod.POST)
                .path("/v1/management/settings/config/maxmemory")
                .contentType(MediaType.FORM_DATA) // = "application/x-www-form-urlencoded"
                .build();

        String formBody = "value=" + RedisConfigServiceTest.MAXMEMORY_VALUE;

        HttpRequest request = HttpRequest.of(headers, HttpData.ofUtf8(formBody));

        AggregatedHttpResponse response = client.execute(request).aggregate().join();

        assertThat(response.status()).isEqualTo(HttpStatus.OK);

        responseString = response.content().toStringUtf8();

        assertThat(responseString).isNotNull();

        assertThatJson(responseString)
                .as("Check result field in result json")
                .node(RedisCommandExecuteResult.RESULT_KEY).isPresent()
                .node(RedisCommandExecuteResult.RESULT_KEY).equals("OK");
    }
}
