package com.relaxed.common.http.core.provider;

import com.relaxed.common.http.domain.RequestConfig;

/**
 * @author Yakir
 * @Topic RequestConfigProvider
 * @Description
 * @date 2022/5/23 10:03
 * @Version 1.0
 */
public interface RequestConfigProvider {

	/**
	 * 提供请求基础配置
	 * @author yakir
	 * @date 2022/5/23 10:09
	 * @return com.relaxed.common.http.domain.RequestConfig
	 */
	RequestConfig provide();

}
