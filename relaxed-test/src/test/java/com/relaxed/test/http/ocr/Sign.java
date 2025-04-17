package com.relaxed.test.http.ocr;

import lombok.SneakyThrows;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

/**
 * @author Yakir
 * @Topic Sign
 * @Description
 * @date 2022/6/20 16:26
 * @Version 1.0
 */
public class Sign {

	private static final Charset UTF8 = StandardCharsets.UTF_8;

	@SneakyThrows
	public static String sign(String secretKey, String sigStr, String sigMethod) {
		String sig = null;

		Mac mac = Mac.getInstance(sigMethod);
		byte[] hash;
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(UTF8), mac.getAlgorithm());

		mac.init(secretKeySpec);
		hash = mac.doFinal(sigStr.getBytes(UTF8));
		sig = DatatypeConverter.printBase64Binary(hash);

		return sig;
	}

	public static String makeSignPlainText(TreeMap<String, String> requestParams, String reqMethod, String host,
			String path) {

		String retStr = "";
		retStr += reqMethod;
		retStr += host;
		retStr += path;
		retStr += buildParamStr(requestParams, reqMethod);
		return retStr;
	}

	protected static String buildParamStr(TreeMap<String, String> requestParams, String requestMethod) {

		String retStr = "";
		for (String key : requestParams.keySet()) {
			String value = requestParams.get(key).toString();
			if (retStr.length() == 0) {
				retStr += '?';
			}
			else {
				retStr += '&';
			}
			retStr += key.replace("_", ".") + '=' + value;
		}
		return retStr;
	}

	@SneakyThrows
	public static String sha256Hex(String s) {
		MessageDigest md;

		md = MessageDigest.getInstance("SHA-256");

		byte[] d = md.digest(s.getBytes(UTF8));
		return DatatypeConverter.printHexBinary(d).toLowerCase();
	}

	@SneakyThrows
	public static String sha256Hex(byte[] b) {
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-256");

		byte[] d = md.digest(b);
		return DatatypeConverter.printHexBinary(d).toLowerCase();
	}

	@SneakyThrows
	public static byte[] hmac256(byte[] key, String msg) {
		Mac mac = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
		mac.init(secretKeySpec);
		return mac.doFinal(msg.getBytes(UTF8));
	}

}
