/*
 * @(#) SimpleServlet.java 2021. 05. 13.
 *
 */
package com.ddoriya.was.server.servlet;

/**
 * @author 이상준
 */
public interface SimpleServlet {
	public void service(HttpRequest req, HttpResponse res) throws Exception;
}
