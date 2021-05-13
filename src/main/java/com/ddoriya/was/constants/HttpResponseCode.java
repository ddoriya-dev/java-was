/*
 * @(#) HttpSesponseCode.java 2021. 05. 13.
 *
 */
package com.ddoriya.was.constants;

/**
 * @author 이상준
 */
public enum HttpResponseCode {
	SC_OK(200, "200", null),
	SC_FORBIDDEN(403, "403 Forbidden", "errorDocument_403"),
	SC_NOT_FOUND(404, "404 File Not Found", "errorDocument_404"),
	SC_INTERNAL_SERVER_ERROR(500, "500 Internal Server Error", "errorDocument_500"),
	SC_NOT_IMPLEMENTED(501, "501 Not Implemented", null);

	private int code;
	private String value;
	private String document;

	HttpResponseCode(int code, String value, String document) {
		this.code = code;
		this.value = value;
		this.document = document;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public String getDocument() {
		return document;
	}

	public static HttpResponseCode getHttpResponseCode(int code) {
		for (HttpResponseCode items : HttpResponseCode.values()) {
			if (code == items.getCode()) {
				return items;
			}
		}

		return null;
	}

	public static String getCodeByValue(int code) {
		for (HttpResponseCode items : HttpResponseCode.values()) {
			if (code == items.getCode()) {
				return items.getValue();
			}
		}

		return "";
	}

	public static String getCodeByDocument(int code) {
		for (HttpResponseCode items : HttpResponseCode.values()) {
			if (code == items.getCode()) {
				return items.getDocument();
			}
		}

		return "";
	}


}
