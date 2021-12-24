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
 * @Topic 记录异常信息
 * @author yakir
 */
public class ExceptionHolder {

	private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal();

	public ExceptionHolder() {
	}

	public static String getXID() {
		String xid = CONTEXT_HOLDER.get();
		return StringUtils.hasText(xid) ? xid : null;
	}

	public static String unbind(String xid) {
		CONTEXT_HOLDER.remove();
		return xid;
	}

	public static String bind(String xid) {
		CONTEXT_HOLDER.set(xid);
		return xid;
	}

	public static void remove() {
		CONTEXT_HOLDER.remove();
	}

}
