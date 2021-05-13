package com.ddoriya.was.server;

import com.ddoriya.was.constants.HttpMethodCode;
import com.ddoriya.was.constants.WebConfigConstants;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;

public class RequestProcessor implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(RequestProcessor.class.getName());

	private JSONArray virtualHosts;
	private String indexFileName = "index.html";
	private Socket connection;
	private HttpRequest httpRequest;
	private HttpResponse httpResponse;

	public RequestProcessor(JSONArray virtualHosts, Socket connection) throws Exception {
		if (virtualHosts != null) {
			this.virtualHosts = virtualHosts;
		} else {
			throw new Exception("11");
		}
		this.connection = connection;
	}

	@Override
	public void run() {
		// for security checks
		try {
			httpRequest = new HttpRequest(connection, virtualHosts);
			httpResponse = new HttpResponse(connection);

			logger.info(connection.getRemoteSocketAddress() + " " + httpRequest.getRequestList());
			getInitializedRouter();
//			if (method.equals("GET")) {
//				String fileName = HttpUtils.getHttpUrl(requestList.get(0));
//				if (fileName.endsWith("/")) {
//					fileName += indexFileName;
//				}
//				String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
//				version = HttpUtils.getHttpVersion(requestList.get(0));
//				File theFile = new File("/test", fileName.substring(1));
//
//				if (theFile.canRead() && theFile.getCanonicalPath().startsWith(root)) {
//					byte[] theData = Files.readAllBytes(theFile.toPath());
//					if (version.startsWith("HTTP/")) { // send a MIME header
//						sendHeader(out, "HTTP/1.0 200 OK", contentType, theData.length);
//					}
//					// send the file; it may be an image or other binary data
//					// so use the underlying output stream
//					// instead of the writer
//					raw.write(theData);
//					raw.flush();
//				} else {
//					// can't find the file
//					String body = new StringBuilder("<HTML>\r\n")
//							.append("<HEAD><TITLE>File Not Found</TITLE>\r\n")
//							.append("</HEAD>\r\n")
//							.append("<BODY>")
//							.append("<H1>HTTP Error 404: File Not Found</H1>\r\n")
//							.append("</BODY></HTML>\r\n")
//							.toString();
//					if (version.startsWith("HTTP/")) { // send a MIME header
//						sendHeader(out, "HTTP/1.0 404 File Not Found", "text/html; charset=utf-8", body.length());
//					}
//					out.write(body);
//					out.flush();
//				}
//			} else {
//				// method does not equal "GET"
//				String body = new StringBuilder("<HTML>\r\n").append("<HEAD><TITLE>Not Implemented</TITLE>\r\n").append("</HEAD>\r\n")
//						.append("<BODY>")
//						.append("<H1>HTTP Error 501: Not Implemented</H1>\r\n")
//						.append("</BODY></HTML>\r\n").toString();
//				if (version.startsWith("HTTP/")) { // send a MIME header
//					sendHeader(out, "HTTP/1.0 501 Not Implemented",
//							"text/html; charset=utf-8", body.length());
//				}
//				out.write(body);
//				out.flush();
//			}
		} catch (IOException ex) {
			logger.error("Error talking to " + connection.getRemoteSocketAddress(), ex);
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
				String body = new StringBuilder("<HTML>\r\n").append("<HEAD><TITLE>Not Implemented</TITLE>\r\n").append("</HEAD>\r\n")
						.append("<BODY>")
						.append("<H1>HTTP Error 501: Not Implemented</H1>\r\n")
						.append("</BODY></HTML>\r\n").toString();
				if (httpRequest.getHttpVersion().startsWith("HTTP/")) { // send a MIME header
					sendHeader(httpResponse.getOut(), "HTTP/1.0 501 Not Implemented",
							"text/html; charset=utf-8", body.length());
				}
				httpResponse.getOut().write(body);
				break;
		}
		httpResponse.writerClose();
	}

	private void get() throws IOException {
		String rootPath = httpRequest.getJsonHttpConfig().getString(WebConfigConstants.DOCUMENT_ROOT);
		String fileName = httpRequest.getHttpUrl();
		if (fileName.endsWith("/")) {
			fileName += indexFileName;
		}
		String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
		String version = httpRequest.getHttpVersion();
		File theFile = new File(rootPath, fileName.substring(1));

		if (theFile.canRead() && theFile.getCanonicalPath().startsWith(rootPath)) {
			byte[] theData = Files.readAllBytes(theFile.toPath());
			if (version.startsWith("HTTP/")) { // send a MIME header
				sendHeader(httpResponse.getOut(), "HTTP/1.0 200 OK", contentType, theData.length);
			}
			httpResponse.getRaw().write(theData);
			httpResponse.getRaw().flush();
		} else {
			// can't find the file
			String body = new StringBuilder("<HTML>\r\n")
					.append("<HEAD><TITLE>File Not Found</TITLE>\r\n")
					.append("</HEAD>\r\n")
					.append("<BODY>")
					.append("<H1>HTTP Error 404: File Not Found</H1>\r\n")
					.append("</BODY></HTML>\r\n")
					.toString();
			if (version.startsWith("HTTP/")) { // send a MIME header
				sendHeader(httpResponse.getOut(), "HTTP/1.0 404 File Not Found", "text/html; charset=utf-8", body.length());
			}
			httpResponse.getOut().write(body);
			httpResponse.getOut().flush();
		}
	}

	private void sendHeader(Writer out, String responseCode, String contentType, int length)
			throws IOException {
		out.write(responseCode + "\r\n");
		Date now = new Date();
		out.write("Date: " + now + "\r\n");
		out.write("Server: JHTTP 2.0\r\n");
		out.write("Content-length: " + length + "\r\n");
		out.write("Content-type: " + contentType + "\r\n\r\n");
		out.flush();
	}

}