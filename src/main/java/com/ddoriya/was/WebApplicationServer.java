/*
 * @(#) WebApplicationService.java 2021. 05. 12.
 *
 */
package com.ddoriya.was;

import com.ddoriya.was.constants.WebConfigConstants;
import com.ddoriya.was.server.HttpContainer;
import com.ddoriya.was.util.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author 이상준
 */
public class WebApplicationServer {
	private static Logger logger = LoggerFactory.getLogger(WebApplicationServer.class.getName());

	private JSONObject httpConfig;

	public WebApplicationServer(String configFile) {
		try {
			this.httpConfig = JsonUtils.parseJSONFile(configFile);
		} catch (IOException ie) {
			logger.error("not json file...", ie);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void start() {
		logger.info("WebApplicationService start..");
		JSONArray jsonVirtualServers = httpConfig.getJSONArray(WebConfigConstants.VIRTUAL_SERVERS);

		for (int i = 0; i < jsonVirtualServers.length(); i++) {
			JSONObject serverConfig = (JSONObject) jsonVirtualServers.get(i);
			try {
				new Thread(new HttpContainer(serverConfig)).start();

				//서버생성시 sleep을 주어 JVM HttpContainer Port를 안전하게 생성시킨다.
				Thread.sleep(100);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
