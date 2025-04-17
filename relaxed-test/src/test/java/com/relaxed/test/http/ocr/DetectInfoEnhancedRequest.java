package com.relaxed.test.http.ocr;

import lombok.Data;

/**
 * @author Yakir
 * @Topic GetDetectInfoEnhancedRequest
 * @Description
 * @date 2022/6/23 16:59
 * @Version 1.0
 */
@Data
public class DetectInfoEnhancedRequest {

	/**
	 * 人脸核身流程的标识，调用DetectAuth接口时生成。
	 */
	private String BizToken;

	/**
	 * 用于细分客户使用场景，由腾讯侧在线下对接时分配。
	 */
	private String RuleId;

	/**
	 * 指定拉取的结果信息，取值（0：全部；1：文本类；2：身份证信息；3：视频最佳截图信息）。 如 13表示拉取文本类、视频最佳截图信息。 默认值：0
	 */
	private String InfoType;

	/**
	 * 从活体视频中截取一定张数的最佳帧（仅部分服务支持，若需使用请与慧眼小助手沟通）。默认为0，最大为10，超出10的最多只给10张。（InfoType需要包含3）
	 */
	private Long BestFramesCount;

	/**
	 * 是否对身份证照片进行裁边。默认为false。（InfoType需要包含2）
	 */
	private Boolean IsCutIdCardImage;

	/**
	 * 是否需要从身份证中抠出头像。默认为false。（InfoType需要包含2）
	 */
	private Boolean IsNeedIdCardAvatar;

	/**
	 * 是否需要对返回中的敏感信息进行加密。其中敏感信息包括：Response.Text.IdCard、Response.Text.Name、Response.Text.OcrIdCard、Response.Text.OcrName
	 */
	private Boolean IsEncrypt;

}
