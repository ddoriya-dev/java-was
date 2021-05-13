package com.ddoriya.was.server;

import com.ddoriya.was.constants.HttpMethodCode;
import com.ddoriya.was.constants.HttpResponseCode;
import com.ddoriya.was.constants.WebConfigConstants;
import com.ddoriya.was.server.view.ErrorPageView;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;

public class RequestProcessor implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(RequestProcessor.class.getName());

	private JSONArray virtualHosts;
	private String indexFileName = "index.html";
	private Socket connection;
	private HttpRequest httpRequest;
	private HttpResponse httpResponse;

	public RequestProcessor(JSONArray virtualHosts, Socket connection) {
		if (virtualHosts != null) {
			this.virtualHosts = virtualHosts;
		} else {
			throw new IllegalArgumentException("is not virtualHosts");
		}
		this.connection = connection;
	}

	@Override
	public void run() {
		try {
			httpRequest = new HttpRequest(connection, virtualHosts);
			httpResponse = new HttpResponse(connection);

			logger.info(connection.getRemoteSocketAddress() + " " + httpRequest.getRequestList());

			if (httpRequest.isHttpConfig()) {
				getInitializedRouter();
			} else {
				new ErrorPageView().setHttpRequest(httpRequest)
						.setHttpResponse(httpResponse)
						.errorPageView(null, HttpResponseCode.SC_INTERNAL_SERVER_ERROR);
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
		switch (httpRequest.getHttpMethod()) {
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
				String rootPath = httpRequest.getJsonHttpConfig().getString(WebConfigConstants.DOCUMENT_ROOT);
				new ErrorPageView().setHttpRequest(httpRequest)
						.setHttpResponse(httpResponse)
						.errorPageView(rootPath, HttpResponseCode.SC_NOT_IMPLEMENTED);
				break;
		}
		httpResponse.writerClose();
	}

	private void get() throws IOException {
		String rootPath = httpRequest.getJsonHttpConfig().getString(WebConfigConstants.DOCUMENT_ROOT);

		try {
			String fileName = httpRequest.getHttpUrl();
			if (fileName.endsWith("/")) {
				fileName += indexFileName;
			}
			String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
			File theFile = new File(rootPath, fileName.substring(1));

			if (theFile.canRead() && theFile.getCanonicalPath().replaceAll("\\\\", "/").startsWith(rootPath)) {
				byte[] theData = Files.readAllBytes(theFile.toPath());
				httpResponse.setContentType(contentType)
						.setSendHeader(httpRequest.getHttpVersion(), HttpResponseCode.SC_OK.getValue(), theData.length);
				httpResponse.getRaw().write(theData);
				httpResponse.getRaw().flush();
			} else {
				new ErrorPageView().setHttpRequest(httpRequest)
						.setHttpResponse(httpResponse)
						.errorPageView(rootPath, HttpResponseCode.SC_NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			new ErrorPageView().setHttpRequest(httpRequest)
					.setHttpResponse(httpResponse)
					.errorPageView(rootPath, HttpResponseCode.SC_INTERNAL_SERVER_ERROR);
		}
	}

}