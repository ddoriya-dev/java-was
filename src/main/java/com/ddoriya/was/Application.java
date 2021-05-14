/*
 * @(#) Application.java 2021. 05. 12.
 *
 */
package com.ddoriya.was;

import com.ddoriya.was.util.FileResourcesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author 이상준
 */
public class Application {
	private static Logger logger = LoggerFactory.getLogger(Application.class.getName());
	private static final String CONFIG_FILE_NAME = "http-conf.json";

	public static void main(String[] args) throws URISyntaxException {
		//resources 파일의 http-conf.json 파일을 설정으로 진행한다.
		String configFile = FileResourcesUtils.getStrFromResource(CONFIG_FILE_NAME);

		logger.debug("WebApplication Start");
		WebApplicationServer webApplicationServer = new WebApplicationServer(configFile);
		webApplicationServer.start();
	}
}
