package com.relaxed.common.exception.notifier;

import com.relaxed.common.exception.domain.ExceptionNoticeResult;

import java.util.List;

/**
 * 默认通知结果决策器
 * <p>
 * 实现了 {@link NoticeResultDecision} 接口，提供了默认的通知结果决策逻辑。 只要有一个通知成功，就认为整体通知成功。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
public class DefaultNoticeResultDecision implements NoticeResultDecision {

	/**
	 * 判断通知是否成功
	 * <p>
	 * 遍历通知结果列表，只要有一个通知成功，就返回true。
	 * </p>
	 * @param noticeResults 通知结果列表
	 * @return 通知是否成功
	 */
	@Override
	public boolean decide(List<ExceptionNoticeResult> noticeResults) {
		return noticeResults.stream().filter(e -> e.isSuccess()).findFirst().isPresent();
	}

}
