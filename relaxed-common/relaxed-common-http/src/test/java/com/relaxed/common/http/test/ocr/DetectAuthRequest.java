package com.relaxed.common.http.test.ocr;

import lombok.Data;

import java.util.HashMap;

/**
 * @author Yakir
 * @Topic DetectAuthRequest
 * @Description
 * @date 2022/6/20 17:37
 * @Version 1.0
 */
@Data
public class DetectAuthRequest {

	/**
	 * 用于细分客户使用场景，申请开通服务后，可以在腾讯云慧眼人脸核身控制台（https://console.cloud.tencent.com/faceid）
	 * 自助接入里面创建，审核通过后即可调用。如有疑问，请添加[腾讯云人脸核身小助手](https://cloud.tencent.com/document/product/1007/56130)进行咨询。
	 */
	private String RuleId;

	/**
	 * 本接口不需要传递此参数。
	 */
	private String TerminalType;

	/**
	 * 身份标识（未使用OCR服务时，必须传入）。 规则：a-zA-Z0-9组合。最长长度32位。
	 */
	private String IdCard;

	/**
	 * 姓名。（未使用OCR服务时，必须传入）最长长度32位。中文请使用UTF-8编码。
	 */
	private String Name;

	/**
	 * 认证结束后重定向的回调链接地址。最长长度1024位。
	 */
	private String RedirectUrl;

	/**
	 * 透传字段，在获取验证结果时返回。
	 */
	private String Extra;

	/**
	 * 用于人脸比对的照片，图片的Base64值；
	 * Base64编码后的图片数据大小不超过3M，仅支持jpg、png格式。请使用标准的Base64编码方式(带=补位)，编码规范参考RFC4648。
	 */
	private String ImageBase64;

	/**
	 * 敏感数据加密信息。对传入信息（姓名、身份证号）有加密需求的用户可使用此参数，详情请点击左侧链接。
	 */
	private Encryption Encryption;

	/**
	 * 意愿核身使用的文案，若未使用意愿核身功能，该字段无需传入。默认为空，最长可接受120的字符串长度。
	 */
	private String IntentionVerifyText;

	public static class Encryption {

		/**
		 * 有加密需求的用户，接入传入kms的CiphertextBlob，关于数据加密可查阅<a href=
		 * "https://cloud.tencent.com/document/product/1007/47180">数据加密</a> 文档。
		 */
		private String CiphertextBlob;

		/**
		 * 在使用加密服务时，填入要被加密的字段。本接口中可填入加密后的一个或多个字段
		 */
		private String[] EncryptList;

		/**
		 * 有加密需求的用户，传入CBC加密的初始向量
		 */
		private String Iv;

	}

}
