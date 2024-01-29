package com.github.armedis.http.service.stats;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;

import com.github.armedis.redis.RedisInfo;
import com.google.common.base.CaseFormat;

public class RedisInfoResultConverter {

	/**
	 * extract key set
	 */
	private static final Map<String, String> extractTargets = new HashMap<>();
	static {
		extractTargets.put("redis_version", null);
		extractTargets.put("redis_mode", null);
		extractTargets.put("", null);
		extractTargets.put("", null);
		extractTargets.put("", null);
		extractTargets.put("", null);
		extractTargets.put("", null);
	}

	public static RedisInfo convert(String info) {
		RedisInfo redisInfo = new RedisInfo();
//		redisInfo.server().
		List<String> redisInfoStrings = null;
		try {
			redisInfoStrings = IOUtils.readLines(new StringReader(info));

			String key = null;
			String value = null;
			for (String line : redisInfoStrings) {
				// extract Key name from line
				String[] keyValue = StringUtils.split(line, ':');
				if (keyValue.length > 1) {
					key = keyValue[0];
					value = keyValue[1];
					key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);
					
					// set 할 때 reflection 사용.
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return redisInfo;
	}

}
