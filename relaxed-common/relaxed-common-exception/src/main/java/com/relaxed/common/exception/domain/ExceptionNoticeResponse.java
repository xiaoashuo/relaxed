package com.relaxed.common.exception.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 异常通知响应
 * <p>
 * 用于封装异常通知的整体响应结果，包括通知是否成功、通知者数量等信息。 支持链式调用设置属性。
 * </p>
 *
 * @author lingting
 * @since 1.0.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class ExceptionNoticeResponse {

	/**
	 * 通知是否成功
	 * <p>
	 * 表示异常通知的整体执行结果，只要有一个通知成功即为成功。
	 * </p>
	 */
	private boolean success;

	/**
	 * 通知者数量
	 * <p>
	 * 记录参与异常通知的处理者数量。
	 * </p>
	 */
	private Integer notifierCount;

	/**
	 * 通知处理结果列表
	 * <p>
	 * 记录每个通知处理器的具体执行结果。
	 * </p>
	 */
	List<ExceptionNoticeResult> noticeResults;

}
