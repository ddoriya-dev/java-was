/*
 * @(#) WasValidator.java 2021. 05. 12.
 *
 */
package com.ddoriya.was;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * @author 이상준
 */
public class WasValidator {
	public static boolean isFileAuth(String rootPath, File file) throws IOException {
		return file.canRead() && file.getCanonicalPath().replaceAll("\\\\", "/").startsWith(rootPath);
	}

	public static boolean isParentPathValid(String url) {
		int count = 0;
		for (int i = 0; i < url.toCharArray().length; i++) {
			if (url.charAt(i) == '/') {
				count++;

				if (count > 1) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isExeExtensionValid(String fileName) {
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1).trim();
		return "exe" .equalsIgnoreCase(ext);
	}

	public static boolean isJsonKeyNullValid(JSONObject httpConfig, String key) {
		return httpConfig.get(key) == null;
	}
}
