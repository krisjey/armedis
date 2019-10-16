
package com.github.armedis.http.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Path;
import com.linecorp.armeria.server.annotation.Post;

/**
 * Service activity api.
 * @author krisjey
 *
 */
@Component
public class AreYouOkService extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(AreYouOkService.class);

    /**
     * Are you ok service for server instance.
     * @param redisRequest
     * @return
     */
    @Get
    @Path(ServiceUrl.RUOK)
    public HttpResponse ruokGet(RedisRequest redisRequest) {
        return HttpResponse.of(HttpStatus.OK);
    }

    @Post
    @Path(ServiceUrl.RUOK)
    public HttpResponse ruokPost(RedisRequest redisRequest) {
        return HttpResponse.of(HttpStatus.OK);
    }
}