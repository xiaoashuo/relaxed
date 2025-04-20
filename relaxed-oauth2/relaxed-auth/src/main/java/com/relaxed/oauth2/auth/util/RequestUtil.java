package com.relaxed.oauth2.auth.util;

import cn.hutool.core.util.StrUtil;
import com.relaxed.common.core.util.WebUtils;
import com.relaxed.oauth2.common.constant.SecurityConstant;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 请求工具类 用于处理OAuth2认证相关的请求参数 支持从请求路径和请求头中获取认证信息
 *
 * @author Yakir
 * @since 1.0
 */
public class RequestUtil {

	/**
	 * 获取授权类型 从请求参数中获取grant_type参数值
	 * @return 授权类型，如password、refresh_token等
	 */
	@SneakyThrows
	public static String getGrantType() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String grantType = request.getParameter(SecurityConstant.GRANT_TYPE_KEY);
		return grantType;
	}

	/**
	 * 获取OAuth2客户端ID 支持两种方式获取客户端信息： 1. 从请求参数中获取client_id 2.
	 * 从请求头的Authorization字段中获取，格式为Basic base64(client_id:client_secret)
	 * @return 客户端ID，如果不存在则返回null
	 */
	@SneakyThrows
	public static String getOAuth2ClientId() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		// 从请求参数中获取
		String clientId = request.getParameter(SecurityConstant.CLIENT_ID_KEY);
		if (StrUtil.isNotBlank(clientId)) {
			return clientId;
		}

		// 从请求头中获取
		String basic = request.getHeader(SecurityConstant.AUTHORIZATION_KEY);
		if (StrUtil.isNotBlank(basic) && basic.startsWith(SecurityConstant.BASIC_PREFIX)) {
			basic = basic.replace(SecurityConstant.BASIC_PREFIX, Strings.EMPTY);
			String basicPlainText = new String(Base64.getDecoder().decode(basic.getBytes(StandardCharsets.UTF_8)),
					StandardCharsets.UTF_8);
			clientId = basicPlainText.split(":")[0]; // client:secret
		}
		return clientId;
	}

	/**
	 * 获取刷新令牌 从请求参数中获取refresh_token参数值
	 * @return 刷新令牌，如果不存在则返回null
	 */
	public static String getRequestRefreshToken() {
		String refreshToken = WebUtils.getParameter(SecurityConstant.REFRESH_TOKEN_KEY);
		return refreshToken;
	}

}
