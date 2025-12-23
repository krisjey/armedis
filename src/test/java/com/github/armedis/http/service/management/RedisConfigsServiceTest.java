/**
 * 
 */
package com.github.armedis.http.service.management;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServerTest;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.common.RequestHeaders;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
class RedisConfigsServiceTest extends AbstractRedisServerTest {

    @Test
    void testConfigsGet() {
        // TODO data 응답 크기 제한 필요.

        String responseString = null;

        RequestHeaders headers = RequestHeaders.builder()
                .method(HttpMethod.GET)
                .path("/v1/management/settings/configs")
                .contentType(MediaType.FORM_DATA) // = "application/x-www-form-urlencoded"
                .build();

        HttpRequest request = HttpRequest.of(headers);

        AggregatedHttpResponse response = client.execute(request).aggregate().join();

        assertThat(response.status()).isEqualTo(HttpStatus.OK);

        responseString = response.content().toStringUtf8();

        assertThat(responseString).isNotNull();

        assertThatJson(responseString)
                .as("Check result field in result json")
                .node("configKeys").isPresent()
                .node("configKeys").isArray();
        
        // TODO currentValue exist
    }
}
