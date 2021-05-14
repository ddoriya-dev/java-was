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
	private Writer outer;
	private OutputStream raw;

	private String contentType = "text/html; charset=utf-8";

	public HttpResponse(Socket connection) throws IOException {
		raw = new BufferedOutputStream(connection.getOutputStream());
		outer = new OutputStreamWriter(raw);
	}

	public Writer getOuter() {
		return outer;
	}

	public OutputStream getRaw() {
		return raw;
	}

	public void writerClose() throws IOException {
		outer.close();
	}

	public HttpResponse setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public void setSendHeader(String version, String responseCode, int length)
			throws IOException {
		if (version.startsWith("HTTP/")) {
			outer.write("HTTP/1.1 " + responseCode + "\r\n");
			Date now = new Date();
			outer.write("Date: " + now + "\r\n");
			outer.write("Server: JHTTP 2.0\r\n");
			outer.write("Content-length: " + length + "\r\n");
			outer.write("Content-type: " + contentType + "\r\n\r\n");
			outer.flush();
		}
	}

	public void setSendHeader(String responseCode) throws IOException {
		outer.write("HTTP/1.1 " + responseCode + "\r\n");
		Date now = new Date();
		outer.write("Date: " + now + "\r\n");
		outer.write("Server: JHTTP 2.0\r\n");
		outer.write("Content-type: " + contentType + "\r\n\r\n");
		outer.flush();
	}
}
