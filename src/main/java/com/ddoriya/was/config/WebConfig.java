/*
 * @(#) WebConfig.java 2021. 05. 13.
 *
 * Copyright 2021. leeSangJun. All rights Reserved.
 */
package com.ddoriya.was.config;

import com.ddoriya.was.util.FileResourcesUtils;
import com.ddoriya.was.util.JsonUtils;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;
import org.json.JSONPointer;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author 이상준
 */
public class WebConfigParser {
	private static final String CONFIG_FILE_NAME = "http-conf.json";

	private JSONObject httpConfig;

	public JSONObject getHttpConfig() throws URISyntaxException, IOException {
		String fileName = FileResourcesUtils.getStrFromResource(CONFIG_FILE_NAME);
		return JsonUtils.parseJSONFile(fileName);
	}

	public JSONObject getHttpConfig(String serverName) throws URISyntaxException, IOException {
		String fileName = FileResourcesUtils.getStrFromResource(CONFIG_FILE_NAME);
		return JsonUtils.parseJSONFile(fileName);
	}


}
