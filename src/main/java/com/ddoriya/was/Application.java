/*
 * @(#) Application.java 2021. 05. 12.
 *
 */
package com.ddoriya.was;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author 이상준
 */
public class Application {
	private static Logger logger = LoggerFactory.getLogger(Application.class.getName());

	public static void main(String[] args) throws URISyntaxException, IOException {
		logger.debug("WebApplication Start");
		WebApplicationServer webApplicationServer = new WebApplicationServer();
		webApplicationServer.start();
	}
}
