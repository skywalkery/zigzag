package org.zigzag.ads.checker.model;

public class TokenResponse {
	private String status;
	private Token data;

	public static final class Token {
		private String token;

		public String getToken() {
			return token;
		}
	}

	public String getStatus() {
		return status;
	}

	public Token getData() {
		return data;
	}
}
