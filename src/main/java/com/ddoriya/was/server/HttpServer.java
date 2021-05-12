package com.ddoriya.was.server;


import com.ddoriya.was.WebApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class HttpServer {
	private static Logger logger = LoggerFactory.getLogger(WebApplication.class.getName());
	private static final int NUM_THREADS = 50;
	private static final String INDEX_FILE = "index.html";
	private final File rootDirectory;
	private final int port;

	public HttpServer(File rootDirectory, int port) throws IOException {
		if (!rootDirectory.isDirectory()) {
			throw new IOException(rootDirectory
					+ " does not exist as a directory");
		}
		this.rootDirectory = rootDirectory;
		this.port = port;
	}

	public void start() throws IOException {
		ForkJoinPool pool = new ForkJoinPool(NUM_THREADS);
		try (ServerSocket server = new ServerSocket(port)) {
			logger.info("Accepting connections on port " + server.getLocalPort());
			logger.info("Document Root: " + rootDirectory);
			while (true) {
				try {
					Socket request = server.accept();
					Runnable r = new RequestProcessor(rootDirectory, INDEX_FILE, request);
					pool.submit(r);
				} catch (IOException ex) {
					logger.error("Error accepting connection", ex);
				}
			}
		}
	}

}