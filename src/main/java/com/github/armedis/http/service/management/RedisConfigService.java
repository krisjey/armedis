
package com.github.armedis.http.service.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.armedis.http.service.BaseService;
import com.github.armedis.http.service.ResponseCode;
import com.github.armedis.http.service.request.RedisRequest;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisConfigRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Consumes;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.Path;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.Put;

/**
 * Redis get http request endpoint service.
 * 
 * @author krisjey
 *
 */
@Component
public class RedisConfigService extends BaseService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String REDIS_COMMAND = "config";

	private static final String COMMAND_URL = "/v1/management/" + REDIS_COMMAND;

	private static final String COMMAND_URL_WITH_KEY = COMMAND_URL + "/:key";

	/**
	 * Process management command request by x-www-form-urlencoded with redis key at URL.
	 * 
	 * @param redisRequest
	 * @return
	 */
	@Get
	@Put
	@Post
	@Path(COMMAND_URL_WITH_KEY)
	@Consumes("application/x-www-form-urlencoded")
	public HttpResponse urlencodedWithKey(RedisConfigRequest redisRequest) {
		logger.info("Text request " + REDIS_COMMAND + " command without key at URL " + redisRequest.toString());

		// execute redis command by http request params.
		RedisCommandExecuteResult result = null;
		try {
			result = executeCommand(redisRequest);
		} catch (Exception e) {
			logger.error("Can not execute redis command ", e);
			return buildResponse(ResponseCode.UNKNOWN_ERROR, redisRequest);
		}

		return buildResponse(redisRequest, result);
	}

	/**
	 * Process set command request by application json with redis key at URL.
	 * 
	 * When request body is absent then JacksonRequestConverterFunction not working.
	 * <br/>
	 * So, just use AggregatedHttpRequest.contentUtf8() method and convert to
	 * JsonNode.
	 * 
	 * @param httpRequest
	 * @param key
	 * @return HttpResponse
	 */
	@Get
	@Put
	@Post
	@Path(COMMAND_URL_WITH_KEY)
	@Consumes("application/json")
	public HttpResponse jsonWithKey(AggregatedHttpRequest httpRequest, @Param("key") String key) {
		JsonNode jsonBody = getAsJsonBody(httpRequest);

		// TODO redisRequest에 get/put 넣어주기.
		RedisRequest redisRequest = buildRedisRequest(REDIS_COMMAND, key, httpRequest, jsonBody);

		logger.info("Json request " + REDIS_COMMAND + " command with key at URL " + redisRequest.toString());

		RedisCommandExecuteResult result = null;
		try {
			result = executeCommand(redisRequest);
		} catch (Exception e) {
			logger.error("Can not execute redis command ", e);
			return buildResponse(ResponseCode.UNKNOWN_ERROR, redisRequest);
		}

		return buildResponse(redisRequest, result);
	}
}
