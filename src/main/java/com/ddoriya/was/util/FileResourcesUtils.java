/*
 * @(#) FileResourcesUtils.java 2021. 05. 12.
 *
 */
package com.ddoriya.was.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author 이상준
 */
public class FileResourcesUtils {
	public static String getStrFromResource(String fileName) throws URISyntaxException {
		URL resource = Thread.currentThread().getContextClassLoader().getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file not found!");
		} else {
			return new File(resource.toURI()).getPath();
		}
	}
}
