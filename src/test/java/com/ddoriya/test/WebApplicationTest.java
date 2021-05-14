/*
 * @(#) WebApplicationTest.java 2021. 05. 14.
 *
 * Copyright 2021. PlayD Corp. All rights Reserved.
 */
package com.ddoriya.test;

import com.ddoriya.was.WebApplicationServer;
import com.ddoriya.was.util.FileResourcesUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author 이상준
 */
public class WebApplicationTest {
	private static final String CONFIG_FILE_NAME = "test-http-conf.json";

	private final String webApplicationUrl = "http://test1.com:8081";
	private Socket socket;
	private URL url;

	@BeforeEach
	public void setup() throws URISyntaxException {
		String configFile = FileResourcesUtils.getStrFromResource(CONFIG_FILE_NAME);
		WebApplicationServer webApplicationServer = new WebApplicationServer(configFile);
		webApplicationServer.start();
	}

	@AfterEach
	public void close() throws IOException {
		if (socket != null) {
			socket.close();
		}
	}

	@Test
	@DisplayName("서버 연결 실패 테스트")
	public void server_connect_Extension_test() {
		assertThatThrownBy(() -> socket = new Socket("123", 8081))
				.isInstanceOf(IOException.class);
	}

	@Test
	@DisplayName("서버 200코드 테스트")
	public void server_connection_response_200_test() throws IOException {
		url = new URL(webApplicationUrl + "/");
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");
		int responseCode = urlConnection.getResponseCode();

		assertThat(responseCode).isEqualTo(200);
	}

	@Test
	@DisplayName("exe파일 요청일 경우 서버 403코드 테스트")
	public void server_connection_response_403_test1() throws IOException {
		url = new URL(webApplicationUrl + "/test.exe");
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");
		int responseCode = urlConnection.getResponseCode();

		assertThat(responseCode).isEqualTo(403);
	}

	@Test
	@DisplayName("/test/test 형식 요청일 경우 서버 403코드 테스트")
	public void server_connection_response_403_test2() throws IOException {
		url = new URL(webApplicationUrl + "/test/test");
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");
		int responseCode = urlConnection.getResponseCode();

		assertThat(responseCode).isEqualTo(403);
	}

	@Test
	@DisplayName("서버에 파일이 없을 경우 서버 404코드 테스트")
	public void server_connection_response_404_test() throws IOException {
		url = new URL(webApplicationUrl + "/test");
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");
		int responseCode = urlConnection.getResponseCode();

		assertThat(responseCode).isEqualTo(404);
	}

	@Test
	@DisplayName("허가되지않은 URL로 서버연결시  500코드 테스트")
	public void server_connection_response_500_test() throws IOException {
		url = new URL("http://127.0.0.1:8081/test");
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");
		int responseCode = urlConnection.getResponseCode();

		assertThat(responseCode).isEqualTo(500);
	}

	@Test
	@DisplayName("/Hello 요청시 결과 테스트")
	public void server_connection_hello_response_test() throws IOException {
		url = new URL(webApplicationUrl + "/Hello");
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");

		String responseBody;
		try (InputStream in = urlConnection.getInputStream();
			 ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			byte[] buf = new byte[1024 * 8];
			int length = 0;
			while ((length = in.read(buf)) != -1) {
				out.write(buf, 0, length);
			}
			responseBody = new String(out.toByteArray(), "UTF-8");
		}

		assertThat(responseBody).isEqualTo("Hello, ");
	}

	@Test
	@DisplayName("/Hello 요청시 parameter 값 결과 테스트")
	public void server_connection_hello_response_parameter_test() throws IOException {
		url = new URL(webApplicationUrl + "/Hello?name=123");
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");

		String responseBody;
		try (InputStream in = urlConnection.getInputStream();
			 ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			byte[] buf = new byte[1024 * 8];
			int length = 0;
			while ((length = in.read(buf)) != -1) {
				out.write(buf, 0, length);
			}
			responseBody = new String(out.toByteArray(), "UTF-8");
		}

		assertThat(responseBody).isEqualTo("Hello, 123");
	}

	@Test
	@DisplayName("/service.Hello 요청시 parameter 값 결과 테스트")
	public void server_connection_service_hello_response_parameter_test() throws IOException {
		url = new URL(webApplicationUrl + "/service.Hello?name=123");
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");

		String responseBody;
		try (InputStream in = urlConnection.getInputStream();
			 ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			byte[] buf = new byte[1024 * 8];
			int length = 0;
			while ((length = in.read(buf)) != -1) {
				out.write(buf, 0, length);
			}
			responseBody = new String(out.toByteArray(), "UTF-8");
		}

		assertThat(responseBody).isEqualTo("Hello, 123");
	}

	@Test
	@DisplayName("/date 요청시 현재시간 결과 테스트")
	public void server_connection_service_date_response_test() throws IOException {
		url = new URL(webApplicationUrl + "/date");
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");

		String responseBody;
		try (InputStream in = urlConnection.getInputStream();
			 ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			byte[] buf = new byte[1024 * 8];
			int length = 0;
			while ((length = in.read(buf)) != -1) {
				out.write(buf, 0, length);
			}
			responseBody = new String(out.toByteArray(), "UTF-8");
		}

		//시간까지 비교한다.
		//서버시간이 늦어지면 초가 달라질수도있기때문이다.
		responseBody = responseBody.substring(0, 13);
		assertThat(responseBody).isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH")));
	}

}
