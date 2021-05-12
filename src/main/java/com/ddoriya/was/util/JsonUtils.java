/*
 * @(#) JsonUtil.java 2021. 05. 12.
 *
 * Copyright 2021. leeSangJun. All rights Reserved.
 */
package com.ddoriya.was.util;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author 이상준
 */
public class JsonUtils {
	public static JSONObject parseJSONFile(String fileName) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get(fileName)));
		return new JSONObject(content);
	}

}
