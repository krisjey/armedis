
package com.github.armedis.http.service.management;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.config.ArmedisConfiguration;
import com.github.armedis.http.service.BaseService;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.Path;
import com.linecorp.armeria.server.annotation.Post;

/**
 * Redis get http request endpoint service.
 * 
 * @author krisjey
 *
 */
@Component
public class ArmedisManagementLoginService extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ArmedisConfiguration armedisConfiguration;

    private static final String COMMAND_URL = "/v1/management/login";

    public ArmedisManagementLoginService(ArmedisConfiguration armedisConfiguration) {
        this.armedisConfiguration = armedisConfiguration;
    }

    /**
     * Process management command request by x-www-form-urlencoded with redis key at
     * URL.
     * 
     * @param redisRequest
     * @return
     */
    @Post
    @Path(COMMAND_URL)
    public HttpResponse urlencodedWithKey(@Param("loginId") String loginId, @Param("loginPassword") Optional<String> loginPassword) {
        logger.info("Text request for management login", loginId);

        ObjectNode result = mapper.createObjectNode();

        if (armedisConfiguration.getLoginId().equals(StringUtils.trim(loginId)) && armedisConfiguration.getLoginPassword().equals(StringUtils.trim(loginPassword.orElse(null)))) {
            result.put("result", "OK");
            return HttpResponse.of(HttpStatus.OK, MediaType.PLAIN_TEXT_UTF_8, result.toString());
        }
        else {
            result.put("result", "Fail");
            return HttpResponse.of(HttpStatus.UNAUTHORIZED, MediaType.PLAIN_TEXT_UTF_8, result.toString());
        }
    }
}
