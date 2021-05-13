/*
 * @(#) HttpRequest.java 2021. 05. 13.
 *
 * Copyright 2021. PlayD Corp. All rights Reserved.
 */
package com.ddoriya.was.server;

import com.ddoriya.was.constants.WebConfigConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 이상준
 */
public class HttpRequest {
	private List<String> requestList;
	private JSONArray httpConfigs;

	public HttpRequest(Socket connection, JSONArray httpConfigs) throws IOException {
		this.httpConfigs = httpConfigs;

		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		requestList = new ArrayList<>();
		String s;
		while ((s = br.readLine()) != null) {
			if (s.equals("")) {
				break;
			}

			requestList.add(s);
		}
	}

	public List<String> getRequestList() {
		return requestList;
	}

	public String getHostName() {
		String httpHost = requestList.get(1);
		if (httpHost.contains("Host")) {
			String[] host = httpHost.split(":");
			return host[1].trim();
		}
		return null;
	}

	public String getHttpMethod() {
		String http = requestList.get(0);
		return getTokens(http)[0];
	}

	public String getHttpUrl() {
		String http = requestList.get(0);
		return getTokens(http)[1];
	}

	public String getHttpVersion() {
		String http = requestList.get(0);
		return getTokens(http)[2];
	}

	public JSONObject getJsonHttpConfig() {
		for (int i = 0; i < httpConfigs.length(); i++) {
			JSONObject requestConfig = (JSONObject) httpConfigs.get(i);

			if (getHostName().equals(requestConfig.get(WebConfigConstants.SERVER_NAME))) {
				return requestConfig;
			}
		}

		return null;
	}

	private static String[] getTokens(String http) {
		if (http.contains("HTTP")) {
			String[] tokens = http.split("\\s+");
			return tokens;
		} else {
			throw new IllegalArgumentException("http text not found!");
		}

	}

	public boolean isHttpConfig() {
		return getJsonHttpConfig() != null;
	}
}
