package com.relaxed.autoconfigure.web.exception;

import com.relaxed.common.core.constants.GlobalConstants;
import com.relaxed.common.core.exception.BusinessException;

import com.relaxed.common.exception.handler.GlobalExceptionHandler;
import com.relaxed.common.model.result.R;
import com.relaxed.common.model.result.SysResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

/**
 * 全局异常处理器 统一处理应用程序中的各种异常，包括业务异常、参数校验异常、系统异常等 根据不同的异常类型返回相应的错误信息和状态码
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandlerResolver {

	@Value("${spring.profiles.active:prod}")
	private String profile;

	/**
	 * 生产环境错误提示信息
	 */
	public static final String PROD_ERR_MSG = "系统异常，请联系管理员";

	/**
	 * 空指针异常提示信息
	 */
	public static final String NLP_MSG = "空指针异常!";

	/**
	 * 处理全局异常 捕获所有未明确处理的异常，根据环境返回不同的错误信息
	 * @param e 异常对象
	 * @param request HTTP请求对象
	 * @return 统一响应结果
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<String> handleGlobalException(Exception e, HttpServletRequest request) {
		log.error("请求地址:{}, 全局异常信息 ex={}", request.getRequestURI(), e.getMessage(), e);
		// 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
		String errorMsg = GlobalConstants.ENV_PROD.equals(profile) ? PROD_ERR_MSG
				: (e instanceof NullPointerException ? NLP_MSG : e.getLocalizedMessage());
		return R.failed(SysResultCode.SERVER_ERROR, errorMsg);
	}

	/**
	 * 处理参数类型转换异常 当请求参数类型与目标类型不匹配时触发
	 * @param e 异常对象
	 * @param request HTTP请求对象
	 * @return 统一响应结果
	 */
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleMethodArgumentTypeMismatchException(Exception e, HttpServletRequest request) {
		log.error("请求地址:{}, 参数类型转换异常 ex={}", request.getRequestURI(), e.getMessage(), e);
		return R.failed(SysResultCode.BAD_REQUEST,
				GlobalConstants.ENV_PROD.equals(profile) ? PROD_ERR_MSG : e.getMessage());
	}

	/**
	 * 处理请求方式异常 包括不支持的媒体类型和请求方法
	 * @param e 异常对象
	 * @param request HTTP请求对象
	 * @return 统一响应结果
	 */
	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class, HttpRequestMethodNotSupportedException.class })
	public R<String> requestNotSupportedException(Exception e, HttpServletRequest request) {
		log.error("请求地址:{}, 请求方式异常 ex={}", request.getRequestURI(), e.getMessage(), e);
		return R.failed(SysResultCode.BAD_REQUEST, e.getLocalizedMessage());
	}

	/**
	 * 处理非法参数异常 主要用于处理Assert断言失败的情况
	 * @param e 异常对象
	 * @param request HTTP请求对象
	 * @return 统一响应结果
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
		log.error("请求地址:{}, 非法数据输入 ex={}", request.getRequestURI(), e.getMessage(), e);
		return R.failed(SysResultCode.BAD_REQUEST, e.getMessage());
	}

	/**
	 * 处理参数校验异常 包括方法参数校验异常和绑定异常
	 * @param exception 异常对象
	 * @param request HTTP请求对象
	 * @return 统一响应结果
	 */
	@ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleBodyValidException(Exception exception, HttpServletRequest request) {
		BindingResult bindingResult;

		if (exception instanceof BindException) {
			bindingResult = ((BindException) exception).getBindingResult();
		}
		else {
			bindingResult = ((MethodArgumentNotValidException) exception).getBindingResult();
		}

		String errorMsg = bindingResult.getErrorCount() > 0 ? bindingResult.getAllErrors().get(0).getDefaultMessage()
				: "未获取到错误信息!";
		log.error("请求地址:{}, 参数绑定异常 ex={}", request.getRequestURI(), errorMsg, exception);

		return R.failed(SysResultCode.BAD_REQUEST, errorMsg);
	}

	/**
	 * 处理单体参数校验异常 处理单个参数的校验异常
	 * @param e 异常对象
	 * @param request HTTP请求对象
	 * @return 统一响应结果
	 */
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleValidationException(Exception e, HttpServletRequest request) {
		log.error("请求地址:{}, 参数绑定异常 ex={}", request.getRequestURI(), e.getMessage(), e);
		return R.failed(SysResultCode.BAD_REQUEST, e.getLocalizedMessage());
	}

	/**
	 * 处理业务异常 业务异常响应码使用200，通过result结构中的code标识业务错误码
	 * @param e 业务异常对象
	 * @param request HTTP请求对象
	 * @return 统一响应结果
	 */
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.OK)
	public R<String> handleBallCatException(BusinessException e, HttpServletRequest request) {
		log.error("请求地址:{}, 业务异常信息 ex={}", request.getRequestURI(), e.getMessage(), e);
		return R.failed(e.getCode(), e.getMessage());
	}

}
