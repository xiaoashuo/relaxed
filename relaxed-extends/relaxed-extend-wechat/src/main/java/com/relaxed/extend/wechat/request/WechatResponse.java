package com.relaxed.extend.wechat.request;

import cn.hutool.core.convert.Convert;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 企业微信消息响应类。 用于封装企业微信机器人接口的响应结果。
 *
 * @author Yakir
 * @since 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class WechatResponse {

	/**
	 * 成功响应码
	 */
	public static final Long SUCCESS_CODE = 0L;

	/**
	 * 构造函数
	 * @param res 原始响应字符串
	 */
	@SneakyThrows
	public WechatResponse(String res) {
		Map resMap = new ObjectMapper().readValue(res.getBytes(), Map.class);
		this.response = res;
		this.code = Convert.toLong(resMap.get("errcode"));
		this.message = Convert.toStr(resMap.get("errmsg"));
		this.success = SUCCESS_CODE.equals(this.code);
	}

	/**
	 * 创建响应对象
	 * @param res 原始响应字符串
	 * @return 响应对象
	 */
	public static WechatResponse of(String res) {
		return new WechatResponse(res);
	}

	/**
	 * 响应码
	 */
	private Long code;

	/**
	 * 响应消息，值为"ok"表示无异常
	 */
	private String message;

	/**
	 * 原始响应内容
	 */
	private String response;

	/**
	 * 是否发送成功
	 */
	private boolean success;

	@Override
	public String toString() {
		return response;
	}

}
