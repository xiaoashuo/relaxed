package com.relaxed.oauth2.resource.services;

import com.relaxed.common.model.result.SysResultCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 自定义远程Token服务 用于从授权服务器验证Token的有效性 支持自定义Token转换器和错误处理
 *
 * @author Yakir
 * @since 1.0
 */
public class CustomRemoteTokenServices implements ResourceServerTokenServices {

	/**
	 * 日志记录器
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * REST操作模板
	 */
	private RestOperations restTemplate;

	/**
	 * Token检查端点URL
	 */
	private String checkTokenEndpointUrl;

	/**
	 * 客户端ID
	 */
	private String clientId;

	/**
	 * 客户端密钥
	 */
	private String clientSecret;

	/**
	 * Token参数名
	 */
	private String tokenName = "token";

	/**
	 * Token转换器
	 */
	private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();

	/**
	 * 构造函数 初始化REST模板和错误处理器
	 */
	public CustomRemoteTokenServices() {
		restTemplate = new RestTemplate();
		((RestTemplate) restTemplate).setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			// 忽略400错误
			public void handleError(ClientHttpResponse response) throws IOException {
				if (response.getRawStatusCode() != 400) {
					super.handleError(response);
				}
			}
		});
	}

	/**
	 * 设置REST操作模板
	 * @param restTemplate REST操作模板
	 */
	public void setRestTemplate(RestOperations restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * 设置Token检查端点URL
	 * @param checkTokenEndpointUrl Token检查端点URL
	 */
	public void setCheckTokenEndpointUrl(String checkTokenEndpointUrl) {
		this.checkTokenEndpointUrl = checkTokenEndpointUrl;
	}

	/**
	 * 设置客户端ID
	 * @param clientId 客户端ID
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * 设置客户端密钥
	 * @param clientSecret 客户端密钥
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	/**
	 * 设置Token转换器
	 * @param accessTokenConverter Token转换器
	 */
	public void setAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
		this.tokenConverter = accessTokenConverter;
	}

	/**
	 * 设置Token参数名
	 * @param tokenName Token参数名
	 */
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	/**
	 * 加载认证信息 通过远程调用验证Token的有效性
	 * @param accessToken 访问令牌
	 * @return OAuth2认证信息
	 * @throws AuthenticationException 认证异常
	 * @throws InvalidTokenException 无效令牌异常
	 */
	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {

		// 构建请求参数
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
		formData.add(tokenName, accessToken);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", getAuthorizationHeader(clientId, clientSecret));

		// 发送请求验证Token
		Map<String, Object> map = postForMap(checkTokenEndpointUrl, formData, headers);

		// 检查响应结果
		if (map.containsKey("code") && !SysResultCode.SUCCESS.getCode().equals(map.get("code"))) {
			if (logger.isDebugEnabled()) {
				logger.debug("check_token returned error: " + map.get("code") + "," + map.get("message"));
			}
			String message = (String) map.get("message");
			String errorMsg = StringUtils.hasText(message) ? message + "," + accessToken : "";
			throw new InvalidTokenException(errorMsg);
		}

		// 检查Token是否有效
		if (!Boolean.TRUE.equals(map.get("active"))) {
			logger.debug("check_token returned active attribute: " + map.get("active"));
			throw new InvalidTokenException(accessToken);
		}

		// 转换认证信息
		return tokenConverter.extractAuthentication(map);
	}

	/**
	 * 读取访问令牌 不支持此操作
	 * @param accessToken 访问令牌
	 * @return 不支持此操作
	 */
	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		throw new UnsupportedOperationException("Not supported: read access token");
	}

	/**
	 * 获取Basic认证头
	 * @param clientId 客户端ID
	 * @param clientSecret 客户端密钥
	 * @return Basic认证头
	 */
	private String getAuthorizationHeader(String clientId, String clientSecret) {
		if (clientId == null || clientSecret == null) {
			logger.warn(
					"Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
		}

		String creds = String.format("%s:%s", clientId, clientSecret);
		try {
			return "Basic " + new String(Base64.encode(creds.getBytes("UTF-8")));
		}
		catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Could not convert String");
		}
	}

	/**
	 * 发送POST请求获取Map结果
	 * @param path 请求路径
	 * @param formData 表单数据
	 * @param headers 请求头
	 * @return 响应结果Map
	 */
	private Map<String, Object> postForMap(String path, MultiValueMap<String, String> formData, HttpHeaders headers) {
		if (headers.getContentType() == null) {
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		}
		@SuppressWarnings("rawtypes")
		Map map = restTemplate.exchange(path, HttpMethod.POST,
				new HttpEntity<MultiValueMap<String, String>>(formData, headers), Map.class).getBody();
		@SuppressWarnings("unchecked")
		Map<String, Object> result = map;
		return result;
	}

}
