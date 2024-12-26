package com.relaxed.test.utils.request;

import cn.hutool.http.HttpGlobalConfig;

/**
 * @author Yakir
 * @Topic RequestConfig
 * @Description
 * @date 2024/3/5 16:28
 * @Version 1.0
 */
public class RequestConfig {

	int connectionTimeout = HttpGlobalConfig.getTimeout();

	int readTimeout = HttpGlobalConfig.getTimeout();

	/**
	 * 是否忽略请求打印
	 */
	boolean isPrintLog = true;

	/**
	 * 忽略响应日志打印
	 */
	boolean ignoreResponseLog = false;

	private RequestFactory factory;

	public RequestConfig(RequestFactory factory) {
		this.factory = factory;
	}

	public RequestFactory end() {
		return this.factory;
	}

	public RequestConfig connectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
		return this;
	}

	public RequestConfig isPrintLog(boolean isPrintLog) {
		this.isPrintLog = isPrintLog;
		return this;
	}

	public RequestConfig ignoreResponseLog(boolean ignoreResponseLog) {
		this.ignoreResponseLog = ignoreResponseLog;
		return this;
	}

	public RequestConfig readTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}

}
