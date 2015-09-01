package org.zigzag.ads.checker.model;

public class CaptchaResponse {
	private String status;
	private Captcha data;

	public static final class Captcha {
		private String captcha;

		public String getCaptcha() {
			return captcha;
		}
	}

	public String getStatus() {
		return status;
	}

	public Captcha getData() {
		return data;
	}
}
