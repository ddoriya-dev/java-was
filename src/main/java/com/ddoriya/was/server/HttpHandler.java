/*
 * @(#) HttpHandler.java 2021. 05. 13.
 *
 */
package com.ddoriya.was.server;

import com.ddoriya.was.WebApplicationValidator;
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

	private final String indexFileName = "index.html";
	private JSONObject config;
	private Socket connection;
	private URLMapper urlMapper;
	private HttpRequest request;
	private HttpResponse response;
	private String rootPath;

	public HttpHandler(JSONObject config, Socket connection, URLMapper urlMapper) {
		if (config == null) {
			throw new NullPointerException("is not virtualHosts");
		}

		this.config = config;
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
				initializedRouter();
			} else {
				new ErrorView(request, response).errorPageView(null, HttpResponseCode.SC_INTERNAL_SERVER_ERROR);
			}

		} catch (IOException ex) {
			logger.error("Error talking to " + connection.getRemoteSocketAddress(), ex);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (response.getWriter() != null) {
					response.writerClose();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (IOException ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
	}

	private void initializedRouter() throws IOException {
		switch (request.getHttpMethod()) {
			case HttpMethodCode.GET:
				get();
				break;
			case HttpMethodCode.POST: //TODO 추후 method code 에 따른 분기를 위해 명시.
			case HttpMethodCode.HEAD:
			case HttpMethodCode.PUT:
			case HttpMethodCode.PATCH:
			case HttpMethodCode.DELETE:
			case HttpMethodCode.OPTIONS:
			case HttpMethodCode.TRACE:
			default:
				new ErrorView(request, response).errorPageView(rootPath, HttpResponseCode.SC_NOT_IMPLEMENTED);
				break;
		}
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
				if (WebApplicationValidator.isExeExtensionValid(file.getName()) || WebApplicationValidator.isParentPathValid(url)) {
					new ErrorView(request, response).errorPageView(rootPath, HttpResponseCode.SC_FORBIDDEN);
					return;
				} else if (!WebApplicationValidator.isFileAuth(rootPath, file)) {
					new ErrorView(request, response).errorPageView(rootPath, HttpResponseCode.SC_NOT_FOUND);
					return;
				}

				String contentType = URLConnection.getFileNameMap().getContentTypeFor(url);
				byte[] theData = Files.readAllBytes(file.toPath());
				response.setContentType(contentType)
						.setSendHeader(request.getHttpVersion(), HttpResponseCode.SC_OK.getValue(), theData.length);
				response.getOutputStream().write(theData);
				response.getOutputStream().flush();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			new ErrorView(request, response).errorPageView(rootPath, HttpResponseCode.SC_INTERNAL_SERVER_ERROR);
		}
	}

}