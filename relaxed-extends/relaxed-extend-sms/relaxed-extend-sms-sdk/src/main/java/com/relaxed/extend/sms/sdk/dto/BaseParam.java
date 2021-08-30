package com.relaxed.extend.sms.sdk.dto;

import lombok.Data;

/**
 * @author Yakir
 * @Topic BaseParam
 * @Description
 * @date 2021/8/26 14:47
 * @Version 1.0
 */
@Data
public class BaseParam {

	/**
	 * 凭证key
	 */
	private String accessKeyId;

	/**
	 * 认证值 加密后
	 */
	private String encryption;

	/**
	 * 时间戳
	 */
	private Long timestamp;

	/**
	 * 发送时间 定时使用 yyyy-MM-dd HH:mm
	 */
	private String sendTime;

}
