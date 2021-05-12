/*
 * @(#) WebApplication.java 2021. 05. 12.
 *
 */
package com.ddoriya.was;

import com.ddoriya.was.server.HttpServer;
import com.ddoriya.was.util.FileResourcesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author 이상준
 */
public class WebApplication {
	private static Logger logger = LoggerFactory.getLogger(WebApplication.class.getName());

	private static final String CONFIG_FILE_NAME = "http-conf.json";

	public static void main(String[] args) throws IOException {
		System.out.println("Start!!");
		File docroot = new File(FileResourcesUtils.getResource(CONFIG_FILE_NAME).toString());
		System.out.println("!");
		HttpServer webserver = new HttpServer(docroot, 80);
		webserver.start();
	}
}
