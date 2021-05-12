/*
 * @(#) HttpConfg.java 2021. 05. 12.
 *
 * Copyright 2021. PlayD Corp. All rights Reserved.
 */
package com.ddoriya.was.model;

/**
 * @author 이상준
 */
public class HttpConfg {
	private String serverName;
	private int serverPort;
	private String documentRoot;
	private String errorLogPath;

	public HttpConfg setServerName(String serverName) {
		this.serverName = serverName;
		return this;
	}

	public HttpConfg setServerPort(int serverPort) {
		this.serverPort = serverPort;
		return this;
	}

	public HttpConfg setDocumentRoot(String documentRoot) {
		this.documentRoot = documentRoot;
		return this;
	}

	public HttpConfg setErrorLogPath(String errorLogPath) {
		this.errorLogPath = errorLogPath;
		return this;
	}

	public String getServerName() {
		return serverName;
	}

	public int getServerPort() {
		return serverPort;
	}

	public String getDocumentRoot() {
		return documentRoot;
	}

	public String getErrorLogPath() {
		return errorLogPath;
	}

	public HttpConfg(String serverName, int serverPort, String documentRoot, String errorLogPath) {
		this.serverName = serverName;
		this.serverPort = serverPort;
		this.documentRoot = documentRoot;
		this.errorLogPath = errorLogPath;
	}

	public HttpConfg build() {
		return new HttpConfg(serverName, serverPort, documentRoot, errorLogPath);
	}


}
