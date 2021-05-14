/*
 * @(#) ErrorPageView.java 2021. 05. 13.
 *
 */
package com.ddoriya.was.server.view;

import com.ddoriya.was.WasValidator;
import com.ddoriya.was.constants.HttpResponseCode;
import com.ddoriya.was.server.servlet.HttpRequest;
import com.ddoriya.was.server.servlet.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;

/**
 * @author 이상준
 */
public class ErrorView {
	private static Logger logger = LoggerFactory.getLogger(ErrorView.class.getName());

	private HttpRequest request;
	private HttpResponse response;

	public ErrorView(HttpRequest request, HttpResponse response) {
		this.request = request;
		this.response = response;
	}

	public void errorPageView(String rootPath, HttpResponseCode httpResponseCode) throws IOException {
		logger.error("HOSTNAME : {}:{} HTTP STATUS CODE : {}", request.getHostName(), request.getPort(), httpResponseCode.getValue());
		if (rootPath == null || WasValidator.isJsonKeyNullValid(request.getJsonHttpConfig(), httpResponseCode.getDocument())) {
			notPageView(httpResponseCode);
			return;
		}

		String errorDocumentFile = request.getJsonHttpConfig().getString(httpResponseCode.getDocument());
		File errorViewFile = new File(rootPath, errorDocumentFile);
		if (WasValidator.isFileAuth(rootPath, errorViewFile)) {
			String contentType = URLConnection.getFileNameMap().getContentTypeFor(errorViewFile.getPath());
			byte[] data = Files.readAllBytes(errorViewFile.toPath());
			response.setContentType(contentType)
					.setSendHeader(request.getHttpVersion(), httpResponseCode.getValue(), data.length);
			response.getRaw().write(data);
			response.getRaw().flush();
		} else {
			notPageView(httpResponseCode);
		}
	}

	private void notPageView(HttpResponseCode httpResponseCode) throws IOException {
		String body = new StringBuilder("<HTML>\r\n")
				.append("<HEAD><TITLE>")
				.append(httpResponseCode.getValue())
				.append("</TITLE>\n")
				.append("</HEAD>\r\n")
				.append("<BODY>")
				.append("<H1>HTTP Error : ")
				.append(httpResponseCode.getValue() + "</H1>\r\n")
				.append("</BODY></HTML>\r\n")
				.toString();
		response.setSendHeader(request.getHttpVersion(), httpResponseCode.getValue(), body.length());
		response.getOuter().write(body);
		response.getOuter().flush();
	}

}
