/*
 * @(#) HttpResponse.java 2021. 05. 13.
 *
 * Copyright 2021. PlayD Corp. All rights Reserved.
 */
package com.ddoriya.was.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

/**
 * @author 이상준
 */
public class HttpResponse {
	private Socket connection;
	private Writer out;
	private OutputStream raw;

	public HttpResponse(Socket connection) throws IOException {
		this.connection = connection;

		raw = new BufferedOutputStream(connection.getOutputStream());
		out = new OutputStreamWriter(raw);
	}

	public Writer getOut() {
		return out;
	}

	public OutputStream getRaw() {
		return raw;
	}

	public void writerClose() throws IOException {
		out.close();
	}
}
