package com.relaxed.common.datasource.processor;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 请求参数数据源处理器 用于从HTTP请求参数中获取数据源名称 支持通过@DS("#request.paramName")注解指定请求参数作为数据源名称
 *
 * @author Hccake
 */
public class DsRequestProcessor extends DsProcessor {

	/**
	 * 请求参数前缀 用于标识从请求参数中获取数据源名称 格式：#request.paramName
	 */
	private static final String REQUEST_PREFIX = "#request";

	/**
	 * 判断是否匹配当前处理器 检查数据源名称是否以#request开头
	 * @param key 数据源名称
	 * @return 是否匹配
	 */
	@Override
	public boolean matches(String key) {
		return key.startsWith(REQUEST_PREFIX);
	}

	/**
	 * 从请求参数中获取数据源名称 如果请求参数不存在，则返回默认的master数据源
	 * @param invocation 方法调用信息
	 * @param key 数据源名称，格式：#request.paramName
	 * @return 数据源名称
	 */
	@Override
	public String doDetermineDatasource(MethodInvocation invocation, String key) {
		HttpServletRequest request = ((ServletRequestAttributes) Objects
				.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		return request.getParameter(key.substring(9));
	}

}
