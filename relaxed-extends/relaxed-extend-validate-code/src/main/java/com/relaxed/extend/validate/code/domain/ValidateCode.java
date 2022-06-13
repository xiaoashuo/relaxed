package com.relaxed.extend.validate.code.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Yakir
 * @Topic ValidateCode
 * @Description
 * @date 2022/6/12 16:00
 * @Version 1.0
 */
@NoArgsConstructor
@Data
public class ValidateCode {

	private String code;

	private long expireInSeconds;

	private LocalDateTime expireTime;

	public ValidateCode(String code, LocalDateTime expireTime) {
		this.code = code;
		this.expireTime = expireTime;
	}

	public ValidateCode(String code, long expireInSeconds) {
		this.code = code;
		this.expireInSeconds = expireInSeconds;
		this.expireTime = LocalDateTime.now().plusSeconds(expireInSeconds);
	}

	/**
	 * 判断是否过期
	 * @return
	 */
	public boolean isExpired() {
		return LocalDateTime.now().isAfter(expireTime);
	}

	/**
	 * 转换成分钟
	 * @return
	 */
	public long minute() {
		return this.expireInSeconds / 60;
	}

}
