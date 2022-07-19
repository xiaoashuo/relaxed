package com.relaxed.common.http.test.ocr;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.http.test.ocr.DetectAuthRequest.Encryption;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Yakir
 * @Topic SignTest
 * @Description
 * @date 2022/6/20 16:25
 * @Version 1.0
 */
public class SignTest {

	public static final String SDK_VERSION = "SDK_JAVA_3.1.532";

	/**
	 * Endpoint means the domain which this request is sent to, such as
	 * cvm.tencentcloudapi.com.
	 */
	private String endpoint = "faceid.tencentcloudapi.com";

	private String service = "faceid";

	private String region = "";

	private String path = "/";

	private String sdkVersion = "SDK_JAVA_3.1.532";

	private String apiVersion = "2018-03-01";

	private String language = "zh-CN";

	/** HTTPS or HTTP, currently only HTTPS is valid. */
	private String protocol = "HTTPS";

	private final String SECRET_ID = "";

	private final String SECRET_KEY = "";

	public static void main(String[] args) {
		SignTest signTest = new SignTest();
		DetectAuthRequest detectAuthRequest = new DetectAuthRequest();
		detectAuthRequest.setRuleId("1");
		detectAuthRequest.setTerminalType("");
		// detectAuthRequest.setIdCard("440923199910116480");
		// detectAuthRequest.setName("赵秀云");
		detectAuthRequest.setRedirectUrl("https://www.baidu.com");
		detectAuthRequest.setExtra("");
		// String imgBase64 = Base64.encode(new File("D:\\other\\100000\\test2.jpg"));
		// detectAuthRequest.setImageBase64(imgBase64);
		detectAuthRequest.setEncryption(new Encryption());
		detectAuthRequest.setIntentionVerifyText("1234");
		String body = JSONUtil.toJsonStr(detectAuthRequest);
		String detectAuth = signTest.call("DetectAuth", body);
		System.out.println(detectAuth);
		DetectInfoEnhancedRequest detectInfoEnhancedRequest = new DetectInfoEnhancedRequest();
		detectInfoEnhancedRequest.setBizToken("6AF23F21-1671-4A85-9BFF-051B17D7AA2A");
		detectInfoEnhancedRequest.setRuleId("1");
		detectInfoEnhancedRequest.setInfoType("0");
		detectInfoEnhancedRequest.setBestFramesCount(0L);
		detectInfoEnhancedRequest.setIsCutIdCardImage(false);
		detectInfoEnhancedRequest.setIsNeedIdCardAvatar(false);
		detectInfoEnhancedRequest.setIsEncrypt(false);
		String resBody = JSONUtil.toJsonStr(detectInfoEnhancedRequest);
		String getDetectInfoEnhanced = signTest.call("GetDetectInfoEnhanced", resBody);
		System.out.println(getDetectInfoEnhanced);

	}

	public String call(String action, String jsonPayload) {
		HashMap<String, String> headers = this.getHeaders();
		headers.put("X-TC-Action", action);
		headers.put("Content-Type", "application/json; charset=utf-8");
		byte[] requestPayload = jsonPayload.getBytes(StandardCharsets.UTF_8);
		String authorization = this.getAuthorization(headers, requestPayload);
		headers.put("Authorization", authorization);
		String url = "https://" + endpoint + this.path;
		HttpRequest post = HttpUtil.createPost(url);
		post.headerMap(headers, true);
		post.body(requestPayload);
		String body = post.execute().body();
		return body;
	}

	/**
	 * payload是否参与签名过程，true表示忽略 payload 不参与，默认为false。
	 * @return
	 */
	public static boolean isUnsignedPayload() {
		return false;
	}

	public static String getLanguage() {
		return null;
	}

	private String getEndpoint() {
		return endpoint;
	}

	private String getRequestMethod() {
		return "POST";
	}

	private HashMap<String, String> getHeaders() {
		HashMap<String, String> headers = new HashMap<String, String>();
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		headers.put("X-TC-Timestamp", timestamp);
		headers.put("X-TC-Version", this.apiVersion);
		headers.put("X-TC-Region", region);
		headers.put("X-TC-RequestClient", SDK_VERSION);
		headers.put("Host", endpoint);
		String token = "";
		if (token != null && !token.isEmpty()) {
			headers.put("X-TC-Token", token);
		}
		if (isUnsignedPayload()) {
			headers.put("X-TC-Content-SHA256", "UNSIGNED-PAYLOAD");
		}
		if (null != getLanguage()) {
			headers.put("X-TC-Language", getLanguage());
		}
		return headers;
	}

	private String getAuthorization(HashMap<String, String> headers, byte[] body) {
		String endpoint = this.getEndpoint();
		// always use post tc3-hmac-sha256 signature process
		// okhttp always set charset even we don't specify it,
		// to ensure signature be correct, we have to set it here as well.
		String contentType = headers.get("Content-Type");
		byte[] requestPayload = body;
		String canonicalUri = "/";
		String canonicalQueryString = "";
		String canonicalHeaders = "content-type:" + contentType + "\nhost:" + endpoint + "\n";
		String signedHeaders = "content-type;host";

		String hashedRequestPayload = "";
		if (isUnsignedPayload()) {
			hashedRequestPayload = Sign.sha256Hex("UNSIGNED-PAYLOAD".getBytes(StandardCharsets.UTF_8));
		}
		else {
			hashedRequestPayload = Sign.sha256Hex(requestPayload);
		}
		String canonicalRequest = getRequestMethod() + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n"
				+ canonicalHeaders + "\n" + signedHeaders + "\n" + hashedRequestPayload;

		String timestamp = headers.get("X-TC-Timestamp");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String date = sdf.format(new Date(Long.valueOf(timestamp + "000")));
		String service = endpoint.split("\\.")[0];
		String credentialScope = date + "/" + service + "/" + "tc3_request";
		String hashedCanonicalRequest = Sign.sha256Hex(canonicalRequest.getBytes(StandardCharsets.UTF_8));
		String stringToSign = "TC3-HMAC-SHA256\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;
		byte[] secretDate = Sign.hmac256(("TC3" + SECRET_KEY).getBytes(StandardCharsets.UTF_8), date);
		byte[] secretService = Sign.hmac256(secretDate, service);
		byte[] secretSigning = Sign.hmac256(secretService, "tc3_request");
		String signature = DatatypeConverter.printHexBinary(Sign.hmac256(secretSigning, stringToSign)).toLowerCase();
		return "TC3-HMAC-SHA256 " + "Credential=" + SECRET_ID + "/" + credentialScope + ", " + "SignedHeaders="
				+ signedHeaders + ", " + "Signature=" + signature;
	}

}
