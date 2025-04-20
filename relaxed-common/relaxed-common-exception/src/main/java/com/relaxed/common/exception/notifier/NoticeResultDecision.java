package com.relaxed.common.exception.notifier;

import com.relaxed.common.exception.domain.ExceptionNoticeResult;

import java.util.List;

/**
 * 通知结果决策器接口
 * <p>
 * 定义了判断通知是否成功的决策逻辑。 实现类可以根据具体需求定义不同的决策规则。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
public interface NoticeResultDecision {

	/**
	 * 判断通知是否成功
	 * <p>
	 * 根据通知结果列表，判断整体通知是否成功。 不同的实现可以定义不同的成功标准，如： 1. 任意一个通知成功即为成功 2. 所有通知都成功才算成功 3.
	 * 特定渠道的通知成功才算成功
	 * </p>
	 * @param noticeResults 通知结果列表
	 * @return 通知是否成功
	 */
	boolean decide(List<ExceptionNoticeResult> noticeResults);

}
