/**
 * 
 */
package com.github.armedis.redis.command.management;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.armedis.ArmedisServer;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisCommandRunner;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.HttpMethod;

/**
 * 
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
class RedisConfigCommandRunnerTest {
    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void test() throws JsonMappingException, JsonProcessingException {
        final AggregatedHttpRequest aggregatedHttpRequest = AggregatedHttpRequest.of(HttpMethod.GET, "/maxmemory");

        RedisConfigRequest redisRequest = new RedisConfigRequest(aggregatedHttpRequest);
        redisRequest.setKey(Optional.of("maxmemory"));

        Object commandRunner = this.beanFactory.getBean("redisConfigCommandRunner", redisRequest, redisTemplate);
        assertThat(commandRunner).isNotNull();

//        ((RedisCommandRunner) commandRunner);
        RedisCommandExecuteResult result = ((RedisCommandRunner) commandRunner).executeAndGet();

        assertThat(result).isNotNull();
        String resultString = result.toResponseString();
        assertThat(resultString).isNotNull();
        
        assertThat(resultString).isNotNull();
        
        
        assertThatJson(resultString)
        .as("Check result field in result json")
        .node(RedisCommandExecuteResult.RESULT_KEY).isPresent()
        .node("result.maxmemory").isPresent();
    }

}
