package com.relaxed.oauth2.auth.util;

import cn.hutool.core.util.StrUtil;
import com.relaxed.oauth2.common.constant.SecurityConstant;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Yakir
 * @Topic RequestUtil
 * @Description
 * @date 2022/7/28 16:12
 * @Version 1.0
 */
public class RequestUtil {

	@SneakyThrows
	public static String getGrantType() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String grantType = request.getParameter(SecurityConstant.GRANT_TYPE_KEY);
		return grantType;
	}

	/**
	 * 获取登录认证的客户端ID
	 * <p>
	 * 兼容两种方式获取OAuth2客户端信息（client_id、client_secret） 方式一：client_id、client_secret放在请求路径中
	 * 方式二：放在请求头（Request Headers）中的Authorization字段，且经过加密，例如 Basic Y2xpZW50OnNlY3JldA==
	 * 明文等于 client:secret
	 * @return
	 */
	@SneakyThrows
	public static String getOAuth2ClientId() {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		// 从请求路径中获取
		String clientId = request.getParameter(SecurityConstant.CLIENT_ID_KEY);
		if (StrUtil.isNotBlank(clientId)) {
			return clientId;
		}

		// 从请求头获取
		String basic = request.getHeader(SecurityConstant.AUTHORIZATION_KEY);
		if (StrUtil.isNotBlank(basic) && basic.startsWith(SecurityConstant.BASIC_PREFIX)) {
			basic = basic.replace(SecurityConstant.BASIC_PREFIX, Strings.EMPTY);
			String basicPlainText = new String(Base64.getDecoder().decode(basic.getBytes(StandardCharsets.UTF_8)),
					StandardCharsets.UTF_8);
			clientId = basicPlainText.split(":")[0]; // client:secret
		}
		return clientId;
	}

}
