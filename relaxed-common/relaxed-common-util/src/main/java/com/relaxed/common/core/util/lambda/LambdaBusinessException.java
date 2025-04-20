package com.relaxed.common.core.util.lambda;

/**
 * Lambda表达式处理过程中的业务异常。 用于封装在Lambda表达式的序列化、反序列化或元数据提取过程中出现的异常。
 * 继承自RuntimeException，属于非受检异常。
 *
 * @author Yakir
 * @since 1.0
 */
public class LambdaBusinessException extends RuntimeException {

	/**
	 * 使用错误消息构造异常
	 * @param message 详细的错误信息
	 */
	public LambdaBusinessException(String message) {
		super(message);
	}

	/**
	 * 使用原始异常构造异常
	 * @param cause 导致当前异常的原始异常
	 */
	public LambdaBusinessException(Throwable cause) {
		super(cause);
	}

}
