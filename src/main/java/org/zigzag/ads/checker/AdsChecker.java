package org.zigzag.ads.checker;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zigzag.ads.checker.model.CaptchaResponse;
import org.zigzag.ads.checker.model.ListResponse;
import org.zigzag.ads.checker.model.TokenResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class AdsChecker {
	private static final Logger log = LoggerFactory.getLogger(AdsChecker.class);

	private static final ObjectMapper MAPPER = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	public static void main(String args[]) throws InterruptedException, ExecutionException, TimeoutException, IOException, Base64DecodingException {
		if (args.length > 0 && "auth".equals(args[0])) {
			String phone = encodePhone(args[1]);
			log.info("Phone: " + phone);
			String captchaUrl = API.Auth.captchaUrl(phone);
			log.info("Getting captcha...");
			String response = HttpClientFactory.getDefaultClient().GET(captchaUrl).getContentAsString();
			CaptchaResponse captchaResponse = MAPPER.readValue(response, CaptchaResponse.class);
			log.info("Captcha status: " + captchaResponse.getStatus());
			File captchaFile = new File("c:\\git\\zigzag\\captcha.bmp");
			captchaFile.createNewFile();
			try (OutputStream os = new FileOutputStream(captchaFile)) {
				os.write(Base64.decode(captchaResponse.getData().getCaptcha()));
			}
		} else if (args.length > 0 && "code".equals(args[0])) {
			String phone = encodePhone(args[1]);
			String code = args[2];
			log.info("Phone: " + phone);
			log.info("Code: " + code);
			String tokenUrl = API.Auth.tokenUrl(phone, code);
			log.info("Getting token...");
			String response = HttpClientFactory.getDefaultClient().GET(tokenUrl).getContentAsString();
			TokenResponse tokenResponse = MAPPER.readValue(response, TokenResponse.class);
			log.info("Token status: " + tokenResponse.getStatus());
			File tokenFile = new File("c:\\git\\zigzag\\token.txt");
			tokenFile.createNewFile();
			try (OutputStream os = new FileOutputStream(tokenFile)) {
				os.write(tokenResponse.getData().getToken().getBytes(StandardCharsets.UTF_8));
			}
		} else {
			String token = Files.readAllLines(Paths.get("c:\\git\\zigzag\\token.txt")).get(0);
			String listUrl = API.Advs.getListUrl(token, 1);
			log.info("Getting advs list...");
			String response = HttpClientFactory.getDefaultClient().GET(listUrl).getContentAsString();
			ListResponse listResponse = MAPPER.readValue(response, ListResponse.class);
			log.info("Advs list status: " + listResponse.getStatus());
			List<ListResponse.Ads> list = listResponse.getData().getList()
					.stream()
					.filter(ads -> ads.getIsPaid() && !ads.getIsPaidUp())
					.collect(Collectors.toList());
			if (list.isEmpty()) {
				log.info("Not found paid advs");
				return;
			}
			log.info("Found {} paid advs", list.size());
		}
	}

	public static String encodePhone(String phone) {
		return phone.replace("+", "%2B");
	}
}
