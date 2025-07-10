
package com.github.armedis.redis.command;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRedisCommandRunner implements RedisCommandRunner {
    private final static Logger logger = LoggerFactory.getLogger(AbstractRedisCommandRunner.class);

    protected static boolean detectAnnotation(Class<?> clazz) {
        RequestRedisCommandName settedAnnotation = clazz.getAnnotation(RequestRedisCommandName.class);

        if (clazz.getSimpleName().startsWith("Redis" + StringUtils.capitalize(settedAnnotation.value().getCommand()) + "CommandRunner")) {
            logger.info(clazz.getSimpleName() + " loaded!");
            return true;
        }
        else {
            logger.info(clazz.getSimpleName() + " loading failed!");
            return false;
        }
    }
}
