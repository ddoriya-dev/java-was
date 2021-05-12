package com.ddoriya.was.server;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestProcessor implements Runnable {
	private final static Logger logger = Logger.getLogger(RequestProcessor.class.getCanonicalName());

	private String indexFileName = "index.html";
	private Socket connection;

	public RequestProcessor(String indexFileName, Socket connection) {
		if (indexFileName != null)
			this.indexFileName = indexFileName;
		this.connection = connection;
	}

	@Override
	public void run() {
		// for security checks
		String root = "/test";
		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(connection.getInputStream())
			);
			PrintWriter out = new PrintWriter(
					new BufferedWriter(
							new OutputStreamWriter(connection.getOutputStream())),
					true);

			String s;
			while ((s = in.readLine()) != null) {
				System.out.println(s);

			}

			out.write("HTTP/1.0 200 OK\r\n");
			out.write("Date: Fri, 31 Dec 1999 23:59:59 GMT\r\n");
			out.write("Server: Apache/0.8.4\r\n");
			out.write("Content-Type: text/html\r\n");
			out.write("Content-Length: 59\r\n");
			out.write("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
			out.write("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
			out.write("\r\n");
			out.write("<TITLE>Exemple</TITLE>");
			out.write("<P>Ceci est une page d'exemple.</P>");

			// on ferme les flux.
			System.err.println("Connexion avec le client terminée");
			out.close();
			in.close();
		} catch (IOException ex) {
			logger.log(Level.WARNING, "Error talking to " + connection.getRemoteSocketAddress(), ex);
		} finally {
			try {
				connection.close();
			} catch (IOException ex) {
			}
		}
	}

//	@Override
//	public void run() {
//		// for security checks
//		String root = "/test";
//		try {
//			OutputStream raw = new BufferedOutputStream(connection.getOutputStream());
//			Writer out = new OutputStreamWriter(raw);
//			Reader in = new InputStreamReader(new BufferedInputStream(connection.getInputStream()), "UTF-8");
//			StringBuilder requestLine = new StringBuilder();
//			while (true) {
//				int c = in.read();
//				if (c == '\r' || c == '\n')
//					break;
//				requestLine.append((char) c);
//			}
//
//			String get = requestLine.toString();
//			logger.info(connection.getRemoteSocketAddress() + " " + get);
//			String[] tokens = get.split("\\s+");
//			String method = tokens[0];
//			String version = "";
//			if (method.equals("GET")) {
//				String fileName = tokens[1];
//				if (fileName.endsWith("/")) fileName += indexFileName;
//				String contentType =
//						URLConnection.getFileNameMap().getContentTypeFor(fileName);
//				if (tokens.length > 2) {
//					version = tokens[2];
//				}
//				File theFile = new File("/test", fileName.substring(1, fileName.length()));
//				if (theFile.canRead()
//// Don't let clients outside the document root
//						&& theFile.getCanonicalPath().startsWith(root)) {
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
//		} catch (IOException ex) {
//			logger.log(Level.WARNING, "Error talking to " + connection.getRemoteSocketAddress(), ex);
//		} finally {
//			try {
//				connection.close();
//			} catch (IOException ex) {
//			}
//		}
//	}

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