package com.relaxed.extend.dingtalk.request;

import cn.hutool.core.convert.Convert;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 钉钉消息发送响应对象。 封装了钉钉机器人消息发送的响应信息，包括状态码、消息内容和处理结果。
 *
 * @author lingting
 * @since 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class DingTalkResponse {

	/**
	 * 钉钉接口成功响应码
	 */
	public static final Long SUCCESS_CODE = 0L;

	@SneakyThrows
	public DingTalkResponse(String res) {
		Map resMap = new ObjectMapper().readValue(res.getBytes(), Map.class);
		this.response = res;
		this.code = Convert.toLong(resMap.get("errcode"));
		this.message = Convert.toStr(resMap.get("errmsg"));
		this.success = SUCCESS_CODE.equals(this.code);
	}

	public static DingTalkResponse of(String res) {
		return new DingTalkResponse(res);
	}

	/**
	 * 钉钉接口响应码
	 */
	private Long code;

	/**
	 * 钉钉接口响应消息 当值为 "ok" 时表示请求成功
	 */
	private String message;

	/**
	 * 钉钉接口原始响应内容
	 */
	private String response;

	/**
	 * 消息发送是否成功 true 表示发送成功，false 表示发送失败
	 */
	private boolean success;

	@Override
	public String toString() {
		return response;
	}

}
