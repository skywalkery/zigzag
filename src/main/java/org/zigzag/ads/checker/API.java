package org.zigzag.ads.checker;

public class API {
	public static final String HOST = "http://m.api.zigzag-mobile.com";

	public static class Auth {
		public static String captchaUrl(String phone) {
			return HOST + "/auth/getCaptcha?phone=" + phone;
		}

		public static String tokenUrl(String phone, String code) {
			return HOST + "/auth/getToken?phone=" + phone + "&code=" + code;
		}
	}

	public static class Advs {
		public static String getListUrl(String token, int page) {
			return HOST + "/advs/getList?token=" + token + "&rubric_id=0&page=" + page;
		}
	}
}
