/*
 * @(#) ClassMapper.java 2021. 05. 14.
 */
package com.ddoriya.was.server.servlet;

import com.ddoriya.was.constants.HttpResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 이상준
 */
public class URLMapper {
	private static Logger logger = LoggerFactory.getLogger(URLMapper.class.getName());
	private Map<String, String> mappingUrlMap;

	public URLMapper() {
		setMapper();
	}

	public void call(String url, HttpRequest request, HttpResponse response) throws Exception {
		invoke(url, request, response);
	}

	public boolean checkMappingUrl(String url) {
		return mappingUrlMap.containsKey(getRemoveParameterUrl(url));
	}

	//TODO 추후 파일로 매핑할수있도록 변경진행한다. 2021-05-14 이상준
	public void setMapper() {
		mappingUrlMap = new HashMap<>();
		mappingUrlMap.put("/Hello", "Hello");
		mappingUrlMap.put("/service.Hello", "service.Hello");
		mappingUrlMap.put("/date", "service.DateSimpleService");
	}

	private void invoke(String url, HttpRequest request, HttpResponse response) throws Exception {
		response.setSendHeader(HttpResponseCode.SC_OK.getValue());

		String className = mappingUrlMap.get(getRemoveParameterUrl(url));
		Class<?> cls = Class.forName(className);
		Object obj = cls.newInstance();
		Method method = cls.getMethod("service", HttpRequest.class, HttpResponse.class);
		method.invoke(obj, request, response);
	}

	private String getRemoveParameterUrl(String url) {
		if (url.contains("?")) {
			url = url.substring(0, url.indexOf("?"));
		}

		return url;
	}


}
