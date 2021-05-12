/*
 * @(#) WebApplicationService.java 2021. 05. 12.
 *
 * Copyright 2021. leeSangJun. All rights Reserved.
 */
package com.ddoriya.was;

import com.ddoriya.was.config.WebConfig;
import com.ddoriya.was.constants.WebConfigConstants;
import com.ddoriya.was.server.HttpServer;
import com.ddoriya.was.server.RequestProcessor;
import com.ddoriya.was.util.FileResourcesUtils;
import com.ddoriya.was.util.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

/**
 * @author 이상준
 */
public class WebApplicationService {
	private static Logger logger = LoggerFactory.getLogger(WebApplicationService.class.getName());

	private static final String CONFIG_FILE_NAME = "http-conf.json";
	private static final int NUM_THREADS = 20;
	private JSONObject httpConfig;

	public WebApplicationService() throws URISyntaxException, IOException {
		this.httpConfig = new WebConfig().getHttpConfig();
	}

	public void start() {
		logger.info("WebApplicationService start..");
		JSONArray jsonVirtualServers = httpConfig.getJSONArray(WebConfigConstants.VIRTUAL_SERVERS);
		ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);

		for (int i = 0; i < jsonVirtualServers.length(); i++) {
			JSONObject serverConfig = (JSONObject) jsonVirtualServers.get(i);
			pool.submit(new HttpServer(serverConfig));
		}
	}
}
