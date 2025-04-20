/**
 * Copyright © 2018 organization baomidou
 * <pre>
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
 * <pre/>
 */
package com.relaxed.common.exception.holder;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常信息持有者
 * <p>
 * 使用ThreadLocal存储异常上下文信息，用于跟踪和传递异常处理过程中的状态。 支持在多线程环境下安全地存储和获取异常信息。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
public class ExceptionHolder {

	/**
	 * 线程本地变量，用于存储异常上下文ID
	 */
	private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal();

	/**
	 * 私有构造函数，防止实例化
	 */
	private ExceptionHolder() {
	}

	/**
	 * 获取当前线程的异常上下文ID
	 * @return 异常上下文ID，如果不存在则返回null
	 */
	public static String getXID() {
		String xid = CONTEXT_HOLDER.get();
		return StringUtils.hasText(xid) ? xid : null;
	}

	/**
	 * 解除当前线程的异常上下文绑定
	 * @param xid 要解除绑定的异常上下文ID
	 * @return 解除绑定的异常上下文ID
	 */
	public static String unbind(String xid) {
		CONTEXT_HOLDER.remove();
		return xid;
	}

	/**
	 * 绑定异常上下文ID到当前线程
	 * @param xid 要绑定的异常上下文ID
	 * @return 绑定的异常上下文ID
	 */
	public static String bind(String xid) {
		CONTEXT_HOLDER.set(xid);
		return xid;
	}

	/**
	 * 移除当前线程的异常上下文
	 */
	public static void remove() {
		CONTEXT_HOLDER.remove();
	}

}
