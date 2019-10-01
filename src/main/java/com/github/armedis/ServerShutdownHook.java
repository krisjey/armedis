package com.github.armedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author krisjey
 */
public class ServerShutdownHook extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(ServerShutdownHook.class);

	private String nodePath;

	public ServerShutdownHook(String nodePath) {
		logger.info("Added to shutdown hook.");
		this.nodePath = nodePath;
	}

	/**
	 * When the server goes down, delete a zookeeper node.
	 */
	@Override
	public void run() {
		try {
			logger.info("Shutdown server");
		} catch (Exception e) {
			logger.error("Cannot delete node " + nodePath, e);
		}
	}
}
