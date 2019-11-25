
package com.github.armedis.redis.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRedisGetCommandRunner implements RedisCommandRunner {
    private final static Logger logger = LoggerFactory.getLogger(AbstractRedisGetCommandRunner.class);

    protected static boolean detectAnnotation(Class<?> clazz) {
        RequestRedisCommandName settedAnnotation = clazz.getAnnotation(RequestRedisCommandName.class);
        if (clazz.getSimpleName().toLowerCase().startsWith("redis" + settedAnnotation.value().getCommand() + "commandrunner")) {
            logger.info(clazz.getSimpleName() + " loaded!");
            return true;
        }
        else {
            logger.info(clazz.getSimpleName() + " loading failed!");
            return false;
        }
    }
}
