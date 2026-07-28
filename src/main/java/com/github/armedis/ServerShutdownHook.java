
package com.github.armedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author krisjey
 * @TODO Change class name to ArmedisShutdownHook
 */
public class ServerShutdownHook extends Thread {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String nodePath;

    public ServerShutdownHook(String nodePath) {
        logger.info("Added to shutdown hook.");
        this.nodePath = nodePath;
    }

    /**
     * When the server goes down running jobs
     */
    @Override
    public void run() {
        try {
            // if use zookeeper delete zookeeper node.
            logger.info("Shutdown server");
        }
        catch (Exception e) {
            logger.error("Cannot execute ServerShutdownHook " + nodePath, e);
        }
    }
}
