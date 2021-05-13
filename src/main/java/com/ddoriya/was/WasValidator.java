/*
 * @(#) WasValidator.java 2021. 05. 12.
 *
 * Copyright 2021. PlayD Corp. All rights Reserved.
 */
package com.ddoriya.was;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * @author 이상준
 */
public class WasValidator {
	public static boolean isFileAuth(String rootPath, String fileName) throws IOException {
		File file = new File(rootPath, fileName);
		return file.canRead() && file.getCanonicalPath().startsWith(rootPath);
	}

	public static boolean isJsonKeyNullValid(JSONObject httpConfig, String key) {
		return httpConfig.get(key) == null;
	}
}
