package com.relaxed.extend.wechat.request;

import cn.hutool.core.convert.Convert;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author Yakir
 * @Topic WechatResponse
 * @Description
 * @date 2022/6/15 13:41
 * @Version 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class WechatResponse {

	public static final Long SUCCESS_CODE = 0L;

	@SneakyThrows
	public WechatResponse(String res) {
		Map resMap = new ObjectMapper().readValue(res.getBytes(), Map.class);
		this.response = res;
		this.code = Convert.toLong(resMap.get("errcode"));
		this.message = Convert.toStr(resMap.get("errmsg"));
		this.success = SUCCESS_CODE.equals(this.code);
	}

	public static WechatResponse of(String res) {
		return new WechatResponse(res);
	}

	private Long code;

	/**
	 * 值为ok表示无异常
	 */
	private String message;

	/**
	 * 钉钉返回信息
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
