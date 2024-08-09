package com.relaxed.admin.core.thread;

import cn.hutool.core.map.MapUtil;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * @author Yakir
 * @Topic AsyncTaskDecorator
 * @Description 主要作用 将mdc 透传至子线程
 * @date 2021/9/18 9:22
 * @Version 1.0
 */
public class MdcTaskDecorator implements TaskDecorator {


	@Override
	public Runnable decorate(Runnable runnable) {
		Map<String, String> contextMap = MDC.getCopyOfContextMap();
		return () -> {
			Map<String, String> old = MDC.getCopyOfContextMap();
			try {
				// 现在：@Async线程上下文！
				// 恢复Web线程上下文的MDC数据
				if (MapUtil.isNotEmpty(contextMap)) {
					MDC.setContextMap(contextMap);
				}
				runnable.run();
			}
			finally {
				if (old == null) {
					MDC.clear();
				}
				else {
					MDC.setContextMap(old);
				}

			}
		};
	}

}
