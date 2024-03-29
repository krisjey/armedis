
package com.github.armedis.http.service.ruok;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.armedis.http.service.BaseService;
import com.github.armedis.http.service.ServiceUrl;
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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Are you ok service for server instance.
     * @param redisRequest
     * @return
     */
    @Get
    @Path(ServiceUrl.RUOK)
    public HttpResponse ruokGet() {
        logger.info("Ruok service GET");
        return HttpResponse.of(HttpStatus.OK);
    }

    @Post
    @Path(ServiceUrl.RUOK)
    public HttpResponse ruokPost(RedisRequest redisRequest) {
        logger.info("Ruok service POST");
        return HttpResponse.of(HttpStatus.OK);
    }
}