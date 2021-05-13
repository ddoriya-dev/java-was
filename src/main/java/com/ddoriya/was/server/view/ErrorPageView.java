/*
 * @(#) ErrorPageView.java 2021. 05. 13.
 */
package com.ddoriya.was.server.view;

import com.ddoriya.was.WasValidator;
import com.ddoriya.was.constants.HttpResponseCode;
import com.ddoriya.was.server.HttpRequest;
import com.ddoriya.was.server.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;

/**
 * @author 이상준
 */
public class ErrorPageView {
	private HttpRequest httpRequest;
	private HttpResponse httpResponse;

	public ErrorPageView() {
	}

	public ErrorPageView setHttpRequest(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
		return this;
	}

	public ErrorPageView setHttpResponse(HttpResponse httpResponse) {
		this.httpResponse = httpResponse;
		return this;
	}

	public void errorPageView(String rootPath, HttpResponseCode httpResponseCode) throws IOException {
		if (rootPath == null || WasValidator.isJsonKeyNullValid(httpRequest.getJsonHttpConfig(), httpResponseCode.getDocument())) {
			notPageView(httpResponseCode);
			return;
		}

		String errorDocumentFile = httpRequest.getJsonHttpConfig().getString(httpResponseCode.getDocument());
		File errorViewFile = new File(errorDocumentFile);
		if (WasValidator.isFileAuth(rootPath, errorDocumentFile)) {
			String contentType = URLConnection.getFileNameMap().getContentTypeFor(errorViewFile.getPath());
			byte[] data = Files.readAllBytes(errorViewFile.toPath());
			httpResponse.setContentType(contentType)
					.setSendHeader(httpRequest.getHttpVersion(), httpResponseCode.getValue(), data.length);
			httpResponse.getRaw().write(data);
			httpResponse.getRaw().flush();
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
		httpResponse.setSendHeader(httpRequest.getHttpVersion(), httpResponseCode.getValue(), body.length());
		httpResponse.getOut().write(body);
		httpResponse.getOut().flush();
	}

}
