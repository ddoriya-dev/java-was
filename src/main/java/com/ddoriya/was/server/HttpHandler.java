/*
 * @(#) HttpHandler.java 2021. 05. 13.
 *
 */
package com.ddoriya.was.server;

import com.ddoriya.was.WasValidator;
import com.ddoriya.was.constants.HttpMethodCode;
import com.ddoriya.was.constants.HttpResponseCode;
import com.ddoriya.was.constants.WebConfigConstants;
import com.ddoriya.was.server.servlet.HttpRequest;
import com.ddoriya.was.server.servlet.HttpResponse;
import com.ddoriya.was.server.servlet.URLMapper;
import com.ddoriya.was.server.view.ErrorView;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;

/**
 * @author 이상준
 */
public class HttpHandler implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(HttpHandler.class.getName());

	private JSONObject config;
	private String indexFileName = "index.html";
	private Socket connection;
	private URLMapper urlMapper;
	private HttpRequest request;
	private HttpResponse response;
	private String rootPath;

	public HttpHandler(JSONObject config, Socket connection, URLMapper urlMapper) {
		if (config != null) {
			this.config = config;
		} else {
			throw new IllegalArgumentException("is not virtualHosts");
		}
		this.connection = connection;
		this.urlMapper = urlMapper;
	}

	@Override
	public void run() {
		try {
			request = new HttpRequest(connection, config);
			response = new HttpResponse(connection);

			logger.info("{} : {}", connection.getRemoteSocketAddress(), request.getRequestList().toString());

			if (request.isHttpConfig()) {
				this.rootPath = request.getJsonHttpConfig().getString(WebConfigConstants.DOCUMENT_ROOT);
				getInitializedRouter();
			} else {
				new ErrorView(request, response).errorPageView(null, HttpResponseCode.SC_INTERNAL_SERVER_ERROR);
			}

		} catch (IOException ex) {
			logger.error("Error talking to " + connection.getRemoteSocketAddress(), ex);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				connection.close();
			} catch (IOException ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
	}

	private void getInitializedRouter() throws IOException {
		switch (request.getHttpMethod()) {
			case HttpMethodCode.GET:
				get();
				break;
			case HttpMethodCode.POST:
			case HttpMethodCode.HEAD:
			case HttpMethodCode.PUT:
			case HttpMethodCode.PATCH:
			case HttpMethodCode.DELETE:
			case HttpMethodCode.OPTIONS:
			case HttpMethodCode.TRACE:
				String rootPath = request.getJsonHttpConfig().getString(WebConfigConstants.DOCUMENT_ROOT);
				new ErrorView(request, response).errorPageView(rootPath, HttpResponseCode.SC_NOT_IMPLEMENTED);
				break;
		}

		response.writerClose();
	}

	private void get() throws IOException {
		try {
			String url = request.getHttpUrl();
			if (url.endsWith("/")) {
				url += indexFileName;
			}

			if (urlMapper.checkMappingUrl(url)) {
				urlMapper.call(url, request, response);
			} else {
				File file = new File(rootPath, url.substring(1));
				if (WasValidator.isExeExtensionValid(file.getName()) || WasValidator.isParentPathValid(url)) {
					new ErrorView(request, response).errorPageView(rootPath, HttpResponseCode.SC_FORBIDDEN);
					return;
				} else if (!WasValidator.isFileAuth(rootPath, file)) {
					new ErrorView(request, response).errorPageView(rootPath, HttpResponseCode.SC_NOT_FOUND);
					return;
				}

				String contentType = URLConnection.getFileNameMap().getContentTypeFor(url);
				byte[] theData = Files.readAllBytes(file.toPath());
				response.setContentType(contentType)
						.setSendHeader(request.getHttpVersion(), HttpResponseCode.SC_OK.getValue(), theData.length);
				response.getRaw().write(theData);
				response.getRaw().flush();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			new ErrorView(request, response).errorPageView(rootPath, HttpResponseCode.SC_INTERNAL_SERVER_ERROR);
		}
	}

}