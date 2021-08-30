package com.relaxed.extend.sms.sdk.handler;

/**
 * @author Yakir
 * @Topic IEncryprionHandler
 * @Description
 * @date 2021/8/26 14:59
 * @Version 1.0
 */
public interface IEncryptionHandler {

	/**
	 * 私钥计算
	 * @author yakir
	 * @date 2021/8/26 14:59
	 * @param timestamp
	 * @param accessKeyId
	 * @param accessKeySecret
	 * @return java.lang.String
	 */
	String encode(String timestamp, String accessKeyId, String accessKeySecret);

}
