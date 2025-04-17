package com.relaxed.test.http.cloud;

import cn.hutool.crypto.asymmetric.Sign;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.TreeMap;

/**
 * @author Yakir
 * @Topic CloudTest
 * @Description
 * @date 2022/6/19 15:15
 * @Version 1.0
 */
public class CloudTest {

	private final static Charset UTF8 = StandardCharsets.UTF_8;

	private final static String SECRET_ID = "AKIDz8krbsJ5yKBZQpn74WFkmLPx3*******";

	private final static String SECRET_KEY = "Gu5t9xGARNpq86cd98joQYCN3*******";

	private final static String CT_JSON = "application/json; charset=utf-8";

	public static byte[] hmac256(byte[] key, String msg) throws Exception {
		Mac mac = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
		mac.init(secretKeySpec);
		return mac.doFinal(msg.getBytes(UTF8));
	}

	public static String sha256Hex(String s) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] d = md.digest(s.getBytes(UTF8));
		return DatatypeConverter.printHexBinary(d).toLowerCase();
	}

	public static void main(String[] args) throws Exception {
		String service = "cvm";
		String host = "cvm.tencentcloudapi.com";
		String region = "ap-guangzhou";
		String action = "DescribeInstances";
		String version = "2017-03-12";
		String algorithm = "TC3-HMAC-SHA256";
		String timestamp = "1551113065";
		// String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 注意时区，否则容易出错
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String date = sdf.format(new Date(Long.valueOf(timestamp + "000")));

		// ************* 步骤 1：拼接规范请求串 *************
		String httpRequestMethod = "POST";
		String canonicalUri = "/";
		String canonicalQueryString = "";
		String canonicalHeaders = "content-type:application/json; charset=utf-8\n" + "host:" + host + "\n";
		String signedHeaders = "content-type;host";

		String payload = "{\"Limit\": 1, \"Filters\": [{\"Values\": [\"\\u672a\\u547d\\u540d\"], \"Name\": \"instance-name\"}]}";
		String hashedRequestPayload = sha256Hex(payload);
		String canonicalRequest = httpRequestMethod + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n"
				+ canonicalHeaders + "\n" + signedHeaders + "\n" + hashedRequestPayload;
		System.out.println(canonicalRequest);

		// ************* 步骤 2：拼接待签名字符串 *************
		String credentialScope = date + "/" + service + "/" + "tc3_request";
		String hashedCanonicalRequest = sha256Hex(canonicalRequest);
		String stringToSign = algorithm + "\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;
		System.out.println(stringToSign);

		// ************* 步骤 3：计算签名 *************
		byte[] secretDate = hmac256(("TC3" + SECRET_KEY).getBytes(UTF8), date);
		byte[] secretService = hmac256(secretDate, service);
		byte[] secretSigning = hmac256(secretService, "tc3_request");
		String signature = DatatypeConverter.printHexBinary(hmac256(secretSigning, stringToSign)).toLowerCase();
		System.out.println(signature);

		// ************* 步骤 4：拼接 Authorization *************
		String authorization = algorithm + " " + "Credential=" + SECRET_ID + "/" + credentialScope + ", "
				+ "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature;
		System.out.println(authorization);

		TreeMap<String, String> headers = new TreeMap<String, String>();
		headers.put("Authorization", authorization);
		headers.put("Content-Type", CT_JSON);
		headers.put("Host", host);
		headers.put("X-TC-Action", action);
		headers.put("X-TC-Timestamp", timestamp);
		headers.put("X-TC-Version", version);
		headers.put("X-TC-Region", region);

		StringBuilder sb = new StringBuilder();
		sb.append("curl -X POST https://").append(host).append(" -H \"Authorization: ").append(authorization)
				.append("\"").append(" -H \"Content-Type: application/json; charset=utf-8\"").append(" -H \"Host: ")
				.append(host).append("\"").append(" -H \"X-TC-Action: ").append(action).append("\"")
				.append(" -H \"X-TC-Timestamp: ").append(timestamp).append("\"").append(" -H \"X-TC-Version: ")
				.append(version).append("\"").append(" -H \"X-TC-Region: ").append(region).append("\"").append(" -d '")
				.append(payload).append("'");
		System.out.println(sb.toString());
		HashMap<String, String> describeInstances = call("LivenessRecognition", "{}");
		System.out.println(describeInstances);
	}

	public void t() {

	}

	public static HashMap<String, String> call(String action, String jsonPayload) throws Exception {
		HashMap<String, String> headers = getHeaders();
		headers.put("X-TC-Action", action);
		headers.put("Content-Type", "application/json; charset=utf-8");

		String authorization = getAuthorization(headers, jsonPayload);
		headers.put("Authorization", authorization);

		System.out.println(headers);
		return headers;
	}

	private static String getEndpoint() {
		return "faceid.tencentcloudapi.com";
	}

	private static String getSdkVersion() {
		return "2017-03-12";
	}

	private static String getRegion() {
		return "ap-guangzhou";
	}

	private static String getApiVersion() {
		return "2019-02-25";
	}

	private static String getToken() {
		return "";
	}

	private static boolean isUnsignedPayload() {
		return false;
	}

	private static String getLanguage() {
		return null;
	}

	private static String getMethod() {
		return "POST";
	}

	private static String getTimestamp() {
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		return "1551113065";
	}

	private static HashMap<String, String> getHeaders() {
		HashMap<String, String> headers = new HashMap<String, String>();

		headers.put("X-TC-Timestamp", getTimestamp());
		headers.put("X-TC-Version", getApiVersion());
		headers.put("X-TC-Region", getRegion());
		// headers.put("X-TC-RequestClient", getSdkVersion());
		headers.put("Host", getEndpoint());
		String token = getToken();
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

	private static String getAuthorization(HashMap<String, String> headers, String body) throws Exception {
		String endpoint = getEndpoint();
		// always use post tc3-hmac-sha256 signature process
		// okhttp always set charset even we don't specify it,
		// to ensure signature be correct, we have to set it here as well.
		String contentType = headers.get("Content-Type");
		String canonicalUri = "/";
		String canonicalQueryString = "";
		String canonicalHeaders = "content-type:" + contentType + "\nhost:" + endpoint + "\n";
		String signedHeaders = "content-type;host";

		String hashedRequestPayload = "";
		if (isUnsignedPayload()) {
			hashedRequestPayload = sha256Hex("UNSIGNED-PAYLOAD");
		}
		else {
			hashedRequestPayload = sha256Hex(body);
		}
		String canonicalRequest = getMethod() + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n"
				+ canonicalHeaders + "\n" + signedHeaders + "\n" + hashedRequestPayload;

		String timestamp = headers.get("X-TC-Timestamp");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String date = sdf.format(new Date(Long.valueOf(timestamp + "000")));
		String service = endpoint.split("\\.")[0];
		String credentialScope = date + "/" + service + "/" + "tc3_request";
		String hashedCanonicalRequest = sha256Hex(canonicalRequest);
		String stringToSign = "TC3-HMAC-SHA256\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;

		String secretId = SECRET_ID;
		String secretKey = SECRET_KEY;
		byte[] secretDate = hmac256(("TC3" + secretKey).getBytes(StandardCharsets.UTF_8), date);
		byte[] secretService = hmac256(secretDate, service);
		byte[] secretSigning = hmac256(secretService, "tc3_request");
		String signature = DatatypeConverter.printHexBinary(hmac256(secretSigning, stringToSign)).toLowerCase();
		return "TC3-HMAC-SHA256 " + "Credential=" + secretId + "/" + credentialScope + ", " + "SignedHeaders="
				+ signedHeaders + ", " + "Signature=" + signature;
	}

}
