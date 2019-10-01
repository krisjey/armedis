
package com.github.armedis.http.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.annotation.Decorator;
import com.linecorp.armeria.server.annotation.Post;

@Component
public class DummyService extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(DummyService.class);

    // Json을 받기 post로
    // application/json

    @Decorator(TransactionSeqDecoratorImpl.class)
    @Post(ServiceUrl.TEST_COMMAND)
    // 명령 처리 Object
    public HttpResponse getBadge(RedisRequest redisRequest) {
        logger.info(redisRequest.toString());
        return HttpResponse.of(HttpStatus.OK);
    }
}
