
package com.github.armedis.http.service.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisRequestBuilderTest {

    @Test
    public void testHset() throws IOException {
        String command = "hset";

        // Redis RedisRequestBuilder는 RedisRequest 클래스를 생성한다.
        // RedisRequest 클래스는 레디스 명령을 실행할 수 있는 모든 값을 가지고 있다.
        // key, command, value들..

        // RedisRequest 클래스는 json요청으로로 부터 생성된다.

        // 최종 사용처는 redis 명령 실행 이므로 build 명령 결과는 command를 인자로 하는 factory 메서드를 통한 객체 생성.

        // 생성은 json/http, www-form-url/http 에서 생성됨.

        // http로 날라오면 명령별 VO로 넘어오도록 처리하자.

        // json으로 날라오면 command로 json을 validation하자.

        // 최종 결과물은 Redis 명령에 따른 VO로 만들자.

        JsonNode jsonNode = new ObjectMapper().readTree("{}");

        // 추상이어야 함.
        RedisRequestBuilder builder = RedisRequestBuilderFactory.createRedisRequestBuilder(command);
        RedisRequest redisRequest = builder.build(jsonNode);
        assertThat(redisRequest).isNotNull();

        assertThat(redisRequest.getCommand()).isEqualTo(command);
        assertThat(redisRequest).isExactlyInstanceOf(HsetRequest.class);

    }

}
