/**
 * 
 */
package com.github.armedis.redis.command.management;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.armedis.ArmedisServer;
import com.github.armedis.config.RedisMultiNodeCommander;
import com.github.armedis.http.service.management.configs.AllowedConfigCommands;
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
        .node("maxmemory").isPresent();
    }

    @Test
    void maxmemorySetCachesCanonicalRedisValue() {
        final AggregatedHttpRequest aggregatedHttpRequest = AggregatedHttpRequest.of(HttpMethod.PUT, "/maxmemory");
        RedisConfigRequest redisRequest = new RedisConfigRequest(aggregatedHttpRequest);
        redisRequest.setKey(Optional.of("maxmemory"));
        redisRequest.setValue(Optional.of("1gb"));

        RedisMultiNodeCommander commander = mock(RedisMultiNodeCommander.class);
        when(commander.setConfigValue("maxmemory", "1gb")).thenReturn(true);
        when(commander.getConfigValue("maxmemory")).thenReturn("1073741824");

        RedisConfigCommandRunner commandRunner = new RedisConfigCommandRunner(redisRequest, redisTemplate);
        ReflectionTestUtils.setField(commandRunner, "redisMultiNodeCommander", commander);

        RedisCommandExecuteResult result = commandRunner.executeAndGet();

        assertThatJson(result.toResponseString()).node(RedisCommandExecuteResult.RESULT_KEY).isEqualTo("OK");
        assertThat(AllowedConfigCommands.get("maxmemory").getCurrentValue()).isEqualTo("1073741824");
        verify(commander).getConfigValue("maxmemory");
    }

    @Test
    void maxmemorySetRejectsUnsupportedUnit() {
        final AggregatedHttpRequest aggregatedHttpRequest = AggregatedHttpRequest.of(HttpMethod.PUT, "/maxmemory");
        RedisConfigRequest redisRequest = new RedisConfigRequest(aggregatedHttpRequest);
        redisRequest.setKey(Optional.of("maxmemory"));
        redisRequest.setValue(Optional.of("1tb"));

        RedisMultiNodeCommander commander = mock(RedisMultiNodeCommander.class);
        RedisConfigCommandRunner commandRunner = new RedisConfigCommandRunner(redisRequest, redisTemplate);
        ReflectionTestUtils.setField(commandRunner, "redisMultiNodeCommander", commander);

        assertThatThrownBy(commandRunner::executeAndGet)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("maxmemory");
    }

}
