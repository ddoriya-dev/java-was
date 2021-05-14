/*
 * @(#) HelloView.java 2021. 05. 13.
 *
 */
package service;

import com.ddoriya.was.server.servlet.HttpRequest;
import com.ddoriya.was.server.servlet.HttpResponse;
import com.ddoriya.was.server.servlet.SimpleServlet;

import java.io.Writer;

/**
 * @author 이상준
 */
public class Hello implements SimpleServlet {

	@Override
	public void service(HttpRequest request, HttpResponse response) throws Exception {
		Writer writer = response.getWriter();
		writer.write("Hello, ");
		writer.write(request.getParameter("name"));
	}
}
