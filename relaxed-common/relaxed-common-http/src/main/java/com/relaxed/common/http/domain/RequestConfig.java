package com.relaxed.common.http.domain;

import lombok.Data;

/**
 * @author Yakir
 * @Topic RequestConfig
 * @Description 请求配置
 * @date 2022/5/23 10:04
 * @Version 1.0
 */
@Data
public class RequestConfig {

	/**
	 * 链接超时时间
	 */
	private int connectionTimeout;

	/**
	 * 读取超时时间
	 */
	private int readTimeout;

}
