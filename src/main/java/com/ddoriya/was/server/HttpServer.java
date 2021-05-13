/*
 * @(#) HttpServer.java 2021. 05. 13.
 */
package com.ddoriya.was.server;


import com.ddoriya.was.constants.WebConfigConstants;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 이상준
 */
public class HttpServer implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(HttpServer.class.getName());
	private static final int NUM_THREADS = 50;

	private static JSONObject config;

	public HttpServer(JSONObject config) {
		this.config = config;
	}

	@Override
	public void run() {
		try {
			ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
			int port = config.getInt(WebConfigConstants.PORT);
			try (ServerSocket server = new ServerSocket(port)) {
				logger.info("Accepting connections on port : {}", server.getLocalPort());
				while (true) {
					try {
						Socket request = server.accept();
						pool.submit(new RequestProcessor(config, request));
					} catch (IOException ex) {
						logger.error("Error accepting connection", ex);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
}