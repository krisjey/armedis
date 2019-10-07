
package com.github.armedis.http.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.annotation.Decorator;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Path;
import com.linecorp.armeria.server.annotation.Post;

@Component
public class DummyService extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(DummyService.class);

    @Decorator(TransactionSeqDecoratorImpl.class)
    @Get
    @Post
    @Path("/v1")
    // 명령 처리 Object
    public HttpResponse getBadge(HttpRequest request, RedisRequest redisRequest) {
        logger.info(request.path());
        logger.info(redisRequest.toString());
        return HttpResponse.of(HttpStatus.OK);
    }
}
