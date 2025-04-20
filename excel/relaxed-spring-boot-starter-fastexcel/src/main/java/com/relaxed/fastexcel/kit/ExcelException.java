package com.relaxed.fastexcel.kit;

/**
 * Excel操作异常类 用于处理Excel操作过程中的异常情况 主要场景: 1. Excel配置异常 2. 数据格式异常 3. 模板文件异常 4. 写入操作异常
 *
 * @author lengleng
 * @since 1.0.0
 */
public class ExcelException extends RuntimeException {

	/**
	 * 序列化版本号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造方法 使用指定的错误信息创建异常对象
	 * @param message 错误信息
	 */
	public ExcelException(String message) {
		super(message);
	}

}
