
package com.github.armedis.http.service;

import java.util.Optional;

/**
 * Json to redis command object
 *     // http/command with key 
 *     // http/command
 *     // json/command with key
 *     // json/command
 *     
 *     4가지 조합
 *     
 *     RedisRequest는 Request로 부터 받은 param을 redis 명령이 실행 가능한 상태의 객체다.
 *     RedisRequest 클래스는 json요청/url-form-encode로 생성할 수 있다.      
        // 최종 사용처는 redis 명령 실행 이므로 build 명령 결과는 command를 인자로 하는 factory 메서드를 통한 객체 생성. 
        
        // 생성은 json/http, www-form-url/http 에서 생성됨.
        
        // http로 날라오면 명령별 VO로 넘어오도록 처리하자.
        
        // json으로 날라오면 command로 json을 validation하자.
        
        // 최종 결과물은 Redis 명령에 따른 VO로 만들자. 
 * @author kris
 *
 */
public class RedisRequest extends AbstractRedisParam {
    public Optional<String> getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "RedisRequest [key=" + key + ", command=" + command + "]";
    }
}
