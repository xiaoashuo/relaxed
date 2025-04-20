/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.relaxed.common.datasource.processor;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 最后一个参数数据源处理器 用于从方法最后一个参数中获取数据源名称 支持通过@DS("#last")注解使用方法的最后一个参数作为数据源名称
 *
 * @author lengleng
 */
public class LastParamDsProcessor extends DsProcessor {

	/**
	 * 最后一个参数前缀 用于标识使用方法的最后一个参数作为数据源名称
	 */
	private static final String LAST_PREFIX = "#last";

	/**
	 * 判断是否匹配当前处理器 检查数据源名称是否以#last开头
	 * @param key 数据源名称
	 * @return 是否匹配
	 */
	@Override
	public boolean matches(String key) {
		if (key.startsWith(LAST_PREFIX)) {
			return true;
		}
		return false;
	}

	/**
	 * 从方法最后一个参数中获取数据源名称 将方法的最后一个参数转换为字符串作为数据源名称
	 * @param invocation 方法调用信息
	 * @param key 数据源名称，格式：#last
	 * @return 数据源名称
	 */
	@Override
	public String doDetermineDatasource(MethodInvocation invocation, String key) {
		Object[] arguments = invocation.getArguments();
		return String.valueOf(arguments[arguments.length - 1]);
	}

}
