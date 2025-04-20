package com.relaxed.common.core.util;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.servlet.ServletUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * IP 地址工具类，用于获取 HTTP 请求的真实客户端 IP 地址。
 *
 * 特点： 1. 支持多级代理环境下的 IP 获取 2. 支持自定义 Header 扩展 3. 优先级排序的多个 Header 检查 4. 适配 Node.js 中间层场景
 *
 * 使用示例： <pre>
 * // 获取客户端 IP
 * String ip = IpUtils.getIpAddr(request);
 *
 * // 使用自定义 Header 获取 IP
 * String ip = IpUtils.getIpAddr(request, "X-Custom-IP");
 * </pre>
 *
 * @author Hccake
 * @since 1.0
 */
public final class IpUtils {

	/**
	 * 私有构造函数，防止实例化
	 */
	private IpUtils() {
	}

	/**
	 * Node.js 中间层转发 IP 的 Header 名称 当前端请求经过 Node.js 服务处理后再转发到后端时， 需要通过此 Header 传递原始客户端 IP
	 */
	public static final String NODE_FORWARDED_IP = "Node-Forwarded-IP";

	/**
	 * 获取 HTTP 请求的客户端 IP 地址 使用默认的 Header 列表和 Node-Forwarded-IP
	 * @param request HTTP 请求对象
	 * @return 客户端 IP 地址，如果无法获取则可能返回 null
	 */
	public static String getIpAddr(HttpServletRequest request) {
		return getIpAddr(request, NODE_FORWARDED_IP);
	}

	/**
	 * 获取 HTTP 请求的客户端 IP 地址，支持自定义 Header
	 *
	 * 按以下顺序依次尝试获取 IP： 1. X-Real-IP 2. X-Forwarded-For 3. Proxy-Client-IP 4.
	 * WL-Proxy-Client-IP 5. HTTP_CLIENT_IP 6. HTTP_X_FORWARDED_FOR 7. 自定义 Header
	 * @param request HTTP 请求对象
	 * @param otherHeaderNames 自定义的 Header 名称列表
	 * @return 客户端 IP 地址，如果无法获取则可能返回 null
	 */
	public static String getIpAddr(HttpServletRequest request, String... otherHeaderNames) {
		String[] headers = { "X-Real-IP", "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
				"HTTP_X_FORWARDED_FOR" };
		if (ArrayUtil.isNotEmpty(otherHeaderNames)) {
			headers = ArrayUtil.addAll(headers, otherHeaderNames);
		}
		return ServletUtil.getClientIPByHeader(request, headers);
	}

}
