/*
 * @(#) WebApplication.java 2021. 05. 12.
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
public class WebApplication {
	private static Logger logger = LoggerFactory.getLogger(WebApplication.class.getName());

	public static void main(String[] args) throws URISyntaxException, IOException {
		logger.debug("시작!!");
		WebApplicationService webApplicationService = new WebApplicationService();
		webApplicationService.start();
	}
}
