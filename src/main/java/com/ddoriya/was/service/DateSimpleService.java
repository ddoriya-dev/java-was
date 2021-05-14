/*
 * @(#) DateSimpleService.java 2021. 05. 14.
 */
package com.ddoriya.was.service;

import com.ddoriya.was.server.servlet.HttpRequest;
import com.ddoriya.was.server.servlet.HttpResponse;
import com.ddoriya.was.server.servlet.SimpleServlet;

import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 이상준
 */
public class DateSimpleService implements SimpleServlet {

	//브라우저에 현재 시간을 출력한다.
	@Override
	public void service(HttpRequest request, HttpResponse response) throws Exception {
		Writer writer = response.getOuter();
		writer.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	}
}
