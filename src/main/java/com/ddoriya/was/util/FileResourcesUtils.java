/*
 * @(#) FileResourcesUtils.java 2021. 05. 12.
 *
 * Copyright 2021. PlayD Corp. All rights Reserved.
 */
package com.ddoriya.was.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author 이상준
 */
public class FileResourcesUtils {
	public static URL getResource(String resource) {
		URL url;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader != null) {
			url = classLoader.getResource(resource);
			if (url != null) {
				return url;
			}
		}

		return ClassLoader.getSystemResource(resource);
	}

	public static File getFileFromResource(String fileName) throws IOException {
		InputStream resourcePath = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(resourcePath));
		StringBuffer sb = new StringBuffer();
		String str;
		while ((str = reader.readLine()) != null) {
			sb.append(str);
		}
		return new File(resourcePath.toString());
	}
}
