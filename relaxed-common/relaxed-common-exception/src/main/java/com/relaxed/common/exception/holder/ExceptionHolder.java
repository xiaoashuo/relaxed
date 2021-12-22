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

	private static final ThreadLocal<Map<String, String>> CONTEXT_HOLDER = ThreadLocal
			.withInitial(() -> new HashMap<>());

	private static final String XID = "LOCAL_EXCEPTION_XID";

	/**
	 * Gets xid.
	 * @return the xid
	 */
	public static String getXID() {
		String xid = CONTEXT_HOLDER.get().get(XID);
		return StringUtils.hasText(xid) ? xid : null;
	}

	/**
	 * Unbind string.
	 * @return the string
	 */
	public static String unbind(String xid) {
		CONTEXT_HOLDER.get().remove(xid);
		return xid;
	}

	/**
	 * bind string.
	 * @return the string
	 */
	public static String bind(String xid) {
		CONTEXT_HOLDER.get().put(XID, xid);
		return xid;
	}

	/**
	 * remove
	 */
	public static void remove() {
		CONTEXT_HOLDER.remove();
	}

}
