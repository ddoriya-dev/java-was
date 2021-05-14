/*
 * @(#) HttpRequest.java 2021. 05. 13.
 *
 */
package com.ddoriya.was.server.servlet;

import com.ddoriya.was.constants.WebConfigConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 이상준
 */
public class HttpRequest {
	private List<String> requestList;
	private JSONObject config;
	private Map<String, String> parametersMap;

	private static final String HOST = "Host";
	private static final String HTTP = "HTTP";

	public HttpRequest(Socket connection, JSONObject config) throws IOException {
		this.config = config;

		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		requestList = new ArrayList<>();
		String s;
		while ((s = br.readLine()) != null) {
			if (s.equals("") || s.length() < 1) {
				break;
			}

			requestList.add(s);
		}

		if (requestList.size() == 0) {
			throw new EOFException("Not Header Data..");
		}

		this.parametersMap = getParametersMap();

	}

	private Map<String, String> getParametersMap() {
		Map<String, String> parametersMap = new HashMap<>();
		if (getHttpUrl().contains("?")) {
			String[] parameters = getHttpUrl().split("&");

			for (int i = 0; i < parameters.length; i++) {
				String parameter;
				if (i == 0) {
					parameter = parameters[i].substring(parameters[i].indexOf("?") + 1);
				} else {
					parameter = parameters[i];
				}

				if (parameter.contains("=")) {
					String[] paramArr = parameter.split("=");
					parametersMap.put(paramArr[0], paramArr[1]);
				}
			}
		}

		return parametersMap;
	}

	public List<String> getRequestList() {
		return requestList;
	}

	public String getHostName() {
		String httpHost = requestList.get(1);
		if (httpHost.contains(HOST)) {
			String[] host = httpHost.split(":");
			return host[1].trim();
		}
		return null;
	}

	public int getPort() {
		return config.getInt(WebConfigConstants.PORT);
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
		JSONArray httpConfigHosts = config.getJSONArray(WebConfigConstants.VIRTUAL_HOSTS);
		for (int i = 0; i < httpConfigHosts.length(); i++) {
			JSONObject requestConfig = (JSONObject) httpConfigHosts.get(i);

			if (getHostName().equals(requestConfig.get(WebConfigConstants.SERVER_NAME))) {
				return requestConfig;
			}
		}

		return null;
	}

	private static String[] getTokens(String http) {
		if (http.contains(HTTP)) {
			String[] tokens = http.split("\\s+");
			return tokens;
		} else {
			throw new IllegalArgumentException("http text not found!");
		}

	}

	public boolean isHttpConfig() {
		return getJsonHttpConfig() != null;
	}

	public String getParameter(String key) {
		if (parametersMap.containsKey(key)) {
			return parametersMap.get(key);
		}

		return "";
	}
}
