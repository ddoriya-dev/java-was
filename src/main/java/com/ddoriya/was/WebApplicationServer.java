/*
 * @(#) WebApplicationService.java 2021. 05. 12.
 *
 */
package com.ddoriya.was;

import com.ddoriya.was.constants.WebConfigConstants;
import com.ddoriya.was.server.HttpServer;
import com.ddoriya.was.util.FileResourcesUtils;
import com.ddoriya.was.util.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author 이상준
 */
public class WebApplicationServer {
	private static Logger logger = LoggerFactory.getLogger(WebApplicationServer.class.getName());

	private static final String CONFIG_FILE_NAME = "http-conf.json";
	private JSONObject httpConfig;

	public WebApplicationServer() throws URISyntaxException, IOException {
		String fileName = FileResourcesUtils.getStrFromResource(CONFIG_FILE_NAME);
		this.httpConfig = JsonUtils.parseJSONFile(fileName);
	}

	public void start() {
		logger.info("WebApplicationService start..");
		JSONArray jsonVirtualServers = httpConfig.getJSONArray(WebConfigConstants.VIRTUAL_SERVERS);

		for (int i = 0; i < jsonVirtualServers.length(); i++) {
			JSONObject serverConfig = (JSONObject) jsonVirtualServers.get(i);
			try {
				new Thread(new HttpServer(serverConfig)).start();
				//서버생성시 sleep을 주어 thread safe하게 진행한다.
				Thread.sleep(100);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
