
package com.github.armedis.http.service.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.armedis.config.ArmedisConfiguration;
import com.github.armedis.http.service.BaseService;
import com.github.armedis.http.service.ResponseCode;
import com.github.armedis.http.service.ServiceUrl;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Path;

/**
 * Service activity api.
 * 
 * @author krisjey
 *
 */
@Component
public class RedisStatsService extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisStatInfoBucket redisStatInfoBucket;

    @Autowired
    private ArmedisConfiguration armedisConfiguration;

    /**
     * Are you ok service for server instance.
     * 
     * @param redisRequest
     * @return
     */
    @Get
    @Path(ServiceUrl.REDIS_STATS)
    public HttpResponse redisStats() {
        logger.info("Armedis - redis stats GET");

        return buildStatResponse(ResponseCode.SUCCESS, redisStatInfoBucket.getStats());
    }
}