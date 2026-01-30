
package com.github.armedis.http.service.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.armedis.http.service.BaseService;
import com.github.armedis.http.service.ResponseCode;
import com.github.armedis.http.service.ServiceUrl;
import com.github.armedis.http.service.stats.RedisStatInfoBucket;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Path;

/**
 * Redis get http request endpoint service.
 * 
 * @author krisjey
 *
 */
@Component
public class RedisCommandStatsService extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisStatInfoBucket redisStatInfoBucket;

    /**
     * Redis stats service for server instance.
     * 
     * @param redisRequest
     * @return
     */
    @Get
    @Path(ServiceUrl.REDIS_COMMAND_STATS)
    public HttpResponse redisCommandStats() {
        logger.info("Armedis - redis command stats GET");

        return buildStatResponse(ResponseCode.SUCCESS, redisStatInfoBucket.getCommandStats());
    }
}