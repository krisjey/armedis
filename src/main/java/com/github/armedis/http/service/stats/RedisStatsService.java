
package com.github.armedis.http.service.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.armedis.http.service.BaseService;
import com.github.armedis.http.service.ServiceUrl;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Path;

/**
 * Service activity api.
 * @author krisjey
 *
 */
@Component
public class RedisStatsService extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(RedisStatsService.class);

    /**
     * Are you ok service for server instance.
     * @param redisRequest
     * @return
     */
    @Get
    @Path(ServiceUrl.REDIS_STATS)
    public HttpResponse redisStats() {
        logger.info("Armedis stat GET");
        // TODO get info data list from stats info.
//        buildResponse
//        buildresponse
        return HttpResponse.of(HttpStatus.OK);
    }
}