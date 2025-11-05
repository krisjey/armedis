
package com.github.armedis.http.service.request;

import static com.linecorp.armeria.common.MediaType.PLAIN_TEXT_UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.armedis.http.service.request.string.RedisGetRequestBuilder;
import com.github.armedis.http.service.request.string.RedisSetRequestBuilder;
import com.github.armedis.redis.command.string.RedisGetRequest;
import com.github.armedis.redis.command.string.RedisSetRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.HttpMethod;

/**
 * // Redis RedisRequestBuilder는 RedisRequest 클래스를 생성한다.
        // RedisRequest 클래스는 레디스 명령을 실행할 수 있는 모든 값을 가지고 있다.
        // key, command, value들..

        // RedisRequest 클래스는 json요청으로로 부터 생성된다.

        // 최종 사용처는 redis 명령 실행 이므로 build 명령 결과는 command를 인자로 하는 factory 메서드를 통한 객체 생성.

        // 생성은 json/http, www-form-url/http 에서 생성됨.

        // http로 날라오면 명령별 VO로 넘어오도록 처리하자.

        // json으로 날라오면 command로 json을 validation하자.

        // 최종 결과물은 Redis 명령에 따른 VO로 만들자.
 * @author krisjey
 *
 */
public class RedisRequestBuilderTest {
    private static final String key = "hello";

    @Test
    public void testGet() throws IOException {
        String command = "Get";

        RedisRequestBuilder builder = RedisRequestBuilderFactory.createRedisRequestBuilder(command);
        assertThat(builder).isExactlyInstanceOf(RedisGetRequestBuilder.class);

        JsonNode jsonNode = new ObjectMapper().readTree("{\"key\":\"" + key + "\"}");

        final AggregatedHttpRequest aReq = AggregatedHttpRequest.of(
                HttpMethod.POST, "/foo", PLAIN_TEXT_UTF_8, "bar");

        RedisRequest redisRequest = builder.build(aReq, jsonNode);
        assertThat(redisRequest).isNotNull();
        assertThat(redisRequest).isExactlyInstanceOf(RedisGetRequest.class);

        assertThat(redisRequest.getCommand()).isEqualTo(command);
        assertThat(redisRequest.getKey()).isEqualTo(key);
    }

    // RedisRequestBuilderFactory가 모든 명령을 다 지원하는지.

    @Test
    public void testSet() throws IOException {
        String command = "Set";
        String value = "hello value";

        RedisRequestBuilder builder = RedisRequestBuilderFactory.createRedisRequestBuilder(command);
        assertThat(builder).isExactlyInstanceOf(RedisSetRequestBuilder.class);
        final AggregatedHttpRequest aReq = AggregatedHttpRequest.of(
                HttpMethod.POST, "/foo", PLAIN_TEXT_UTF_8, "bar");
        JsonNode jsonNode = new ObjectMapper().readTree("{\"key\":\"" + key + "\", \"value\":\"" + value + "\"}");
        RedisRequest redisRequest = builder.build(aReq, jsonNode);
        assertThat(redisRequest).isNotNull();
        assertThat(redisRequest).isExactlyInstanceOf(RedisSetRequest.class);

        assertThat(redisRequest.getCommand()).isEqualTo(command);
        assertThat(redisRequest.getKey()).isEqualTo(key);

        assertThat(((RedisSetRequest) redisRequest).getValue()).isEqualTo(value);
    }
}
