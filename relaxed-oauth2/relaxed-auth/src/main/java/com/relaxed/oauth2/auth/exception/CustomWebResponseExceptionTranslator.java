package com.relaxed.oauth2.auth.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.io.IOException;

/**
 * 自定义Web响应异常转换器 用于统一处理OAuth2认证过程中的异常 将不同类型的异常转换为统一的OAuth2异常格式
 *
 * @author Yakir
 * @since 1.0
 */
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

	/**
	 * 异常分析器 用于分析异常链，提取特定类型的异常
	 */
	private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

	/**
	 * 转换异常为OAuth2异常响应 根据异常类型转换为对应的OAuth2异常
	 * @param e 原始异常
	 * @return OAuth2异常响应实体
	 * @throws Exception 当转换过程中发生错误时抛出
	 */
	@Override
	public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
		// Try to extract a SpringSecurityException from the stacktrace
		Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
		Exception ase = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);

		if (ase != null) {
			return handleOAuth2Exception((OAuth2Exception) ase);
		}

		ase = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class,
				causeChain);
		if (ase != null) {
			return handleOAuth2Exception(new UnauthorizedException(e.getMessage(), e));
		}

		ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class,
				causeChain);
		if (ase instanceof AccessDeniedException) {
			return handleOAuth2Exception(new ForbiddenException(ase.getMessage(), ase));
		}

		ase = (HttpRequestMethodNotSupportedException) throwableAnalyzer
				.getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain);
		if (ase instanceof HttpRequestMethodNotSupportedException) {
			return handleOAuth2Exception(new MethodNotAllowed(ase.getMessage(), ase));
		}

		ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class,
				causeChain);
		if (ase != null) {
			return handleOAuth2Exception(
					new CustomWebResponseExceptionTranslator.ForbiddenException(ase.getMessage(), ase));
		}

		String reasonPhrase = StringUtils.hasText(e.getMessage()) ? e.getMessage()
				: HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
		return handleOAuth2Exception(new ServerErrorException(reasonPhrase, e));
	}

	/**
	 * 处理OAuth2异常 设置响应头和状态码，并返回异常响应实体
	 * @param e OAuth2异常
	 * @return 异常响应实体
	 * @throws IOException 当处理过程中发生IO错误时抛出
	 */
	private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) throws IOException {
		int status = e.getHttpErrorCode();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Cache-Control", "no-store");
		headers.set("Pragma", "no-cache");
		if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
			headers.set("WWW-Authenticate", String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
		}
		return new ResponseEntity<>(new CustomOAuth2Exception(e.getMessage(), e), headers, HttpStatus.valueOf(status));
	}

	/**
	 * 设置异常分析器
	 * @param throwableAnalyzer 异常分析器
	 */
	public void setThrowableAnalyzer(ThrowableAnalyzer throwableAnalyzer) {
		this.throwableAnalyzer = throwableAnalyzer;
	}

	/**
	 * 禁止访问异常 表示用户没有足够的权限访问资源
	 */
	@SuppressWarnings("serial")
	private static class ForbiddenException extends OAuth2Exception {

		/**
		 * 构造函数
		 * @param msg 异常消息
		 * @param t 原始异常
		 */
		public ForbiddenException(String msg, Throwable t) {
			super(msg, t);
		}

		@Override
		public String getOAuth2ErrorCode() {
			return "access_denied";
		}

		@Override
		public int getHttpErrorCode() {
			return 403;
		}

	}

	/**
	 * 服务器错误异常 表示服务器内部发生错误
	 */
	@SuppressWarnings("serial")
	private static class ServerErrorException extends OAuth2Exception {

		/**
		 * 构造函数
		 * @param msg 异常消息
		 * @param t 原始异常
		 */
		public ServerErrorException(String msg, Throwable t) {
			super(msg, t);
		}

		@Override
		public String getOAuth2ErrorCode() {
			return "server_error";
		}

		@Override
		public int getHttpErrorCode() {
			return 500;
		}

	}

	/**
	 * 未授权异常 表示用户未通过认证
	 */
	@SuppressWarnings("serial")
	private static class UnauthorizedException extends OAuth2Exception {

		/**
		 * 构造函数
		 * @param msg 异常消息
		 * @param t 原始异常
		 */
		public UnauthorizedException(String msg, Throwable t) {
			super(msg, t);
		}

		@Override
		public String getOAuth2ErrorCode() {
			return "unauthorized";
		}

		@Override
		public int getHttpErrorCode() {
			return 401;
		}

	}

	/**
	 * 方法不允许异常 表示请求的HTTP方法不被允许
	 */
	@SuppressWarnings("serial")
	private static class MethodNotAllowed extends OAuth2Exception {

		/**
		 * 构造函数
		 * @param msg 异常消息
		 * @param t 原始异常
		 */
		public MethodNotAllowed(String msg, Throwable t) {
			super(msg, t);
		}

		@Override
		public String getOAuth2ErrorCode() {
			return "method_not_allowed";
		}

		@Override
		public int getHttpErrorCode() {
			return 405;
		}

	}

}
