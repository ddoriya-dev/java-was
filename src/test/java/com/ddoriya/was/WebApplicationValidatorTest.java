/*
 * @(#) WasValidatorTest.java 2021. 05. 14.
 *
 */
package com.ddoriya.was;

import com.ddoriya.was.util.FileResourcesUtils;
import com.ddoriya.was.util.JsonUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.*;

/**
 * @author 이상준
 */
public class WebApplicationValidatorTest {
	private static final String CONFIG_FILE_NAME = "test-http-conf.json";
	private JSONObject httpConfig;

	@BeforeEach
	public void setup() throws URISyntaxException, IOException {
		String configFile = FileResourcesUtils.getStrFromResource(CONFIG_FILE_NAME);
		this.httpConfig = JsonUtils.parseJSONFile(configFile);
	}

	@Test
	@DisplayName("부모 path가 있는지 테스트")
	public void parent_path_valid_test() {
		String url1 = "/a/b/c";
		String url2 = "/a";
		assertThat(WebApplicationValidator.isParentPathValid(url1)).isTrue();
		assertThat(WebApplicationValidator.isParentPathValid(url2)).isFalse();
	}

	@Test
	@DisplayName("파일에 exe 확장자가 존재하는지 테스트")
	public void file_exe_Extension_valid_test() {
		String fileName1 = "test.exe";
		String fileName2 = "test.html";
		assertThat(WebApplicationValidator.isExeExtensionValid(fileName1)).isTrue();
		assertThat(WebApplicationValidator.isExeExtensionValid(fileName2)).isFalse();
	}

	@Test
	@DisplayName("JSON 파일이 아닐경우 예외처리 테스트")
	public void json_file_Extension_valid_test() {
		assertThatThrownBy(() -> JsonUtils.parseJSONFile("index.html"))
				.isInstanceOf(IOException.class);
	}

	@Test
	@DisplayName("JSON 파일 정상 체크")
	public void json_file_valid_test() throws URISyntaxException {
		String configFile = FileResourcesUtils.getStrFromResource(CONFIG_FILE_NAME);
		assertThatCode(() -> JsonUtils.parseJSONFile(configFile)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("JSON 데이터 정상 체크")
	public void json_file_read_valid_test() {
		String key = "virtualServers";
		assertThat(WebApplicationValidator.isJsonKeyNullValid(httpConfig, key)).isFalse();
		assertThat(WebApplicationValidator.isJsonKeyNullValid(httpConfig, "test")).isTrue();
	}

}
