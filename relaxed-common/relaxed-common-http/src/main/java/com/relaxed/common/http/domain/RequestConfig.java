package com.relaxed.common.http.domain;

import lombok.Data;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

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

	/**
	 * 设置域名验证器<br>
	 * 只针对HTTPS请求，如果不设置，不做验证，所有域名被信任
	 */
	private HostnameVerifier hostnameVerifier;

	/**
	 * 设置SSLSocketFactory<br>
	 * 只针对HTTPS请求，如果不设置，使用默认的SSLSocketFactory<br>
	 */
	private SSLSocketFactory sslSocketFactory;

}
