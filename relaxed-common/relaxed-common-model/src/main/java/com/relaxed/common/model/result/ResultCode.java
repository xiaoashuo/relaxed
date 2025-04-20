package com.relaxed.common.model.result;

/**
 * 结果码接口
 * <p>
 * 定义统一的业务结果码规范，用于规范系统中各类业务操作的结果状态。 实现此接口的类需要提供状态码和对应的描述信息。
 * </p>
 *
 * @author Hccake
 * @since 1.0.0
 */
public interface ResultCode {

	/**
	 * 获取业务状态码
	 * <p>
	 * 状态码用于标识业务操作的结果状态，通常0表示成功，非0表示失败。
	 * </p>
	 * @return 业务状态码
	 */
	Integer getCode();

	/**
	 * 获取业务状态描述信息
	 * <p>
	 * 描述信息用于说明业务操作的结果，通常包含成功或失败的具体原因。
	 * </p>
	 * @return 业务状态描述信息
	 */
	String getMessage();

}
