/*
 * @(#) HttpResponse.java 2021. 05. 13.
 *
 */
package com.ddoriya.was.server.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Date;

/**
 * @author 이상준
 */
public class HttpResponse {
	private Writer writer;
	private OutputStream outputStream;

	private String contentType = "text/html; charset=utf-8";

	public HttpResponse(Socket connection) throws IOException {
		outputStream = new BufferedOutputStream(connection.getOutputStream());
		writer = new OutputStreamWriter(outputStream);
	}

	public Writer getWriter() {
		return writer;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void writerClose() throws IOException {
		writer.close();
	}

	public HttpResponse setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public void setSendHeader(String version, String responseCode, int length)
			throws IOException {
		if (version.startsWith("HTTP/")) {
			writer.write("HTTP/1.1 " + responseCode + "\r\n");
			Date now = new Date();
			writer.write("Date: " + now + "\r\n");
			writer.write("Server: JHTTP 2.0\r\n");
			writer.write("Content-length: " + length + "\r\n");
			writer.write("Content-type: " + contentType + "\r\n\r\n");
			writer.flush();
		}
	}

	public void setSendHeader(String responseCode) throws IOException {
		writer.write("HTTP/1.1 " + responseCode + "\r\n");
		Date now = new Date();
		writer.write("Date: " + now + "\r\n");
		writer.write("Server: JHTTP 2.0\r\n");
		writer.write("Content-type: " + contentType + "\r\n\r\n");
		writer.flush();
	}
}
