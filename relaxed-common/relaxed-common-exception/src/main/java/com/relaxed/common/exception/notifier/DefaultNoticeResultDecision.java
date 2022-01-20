package com.relaxed.common.exception.notifier;

import com.relaxed.common.exception.domain.ExceptionNoticeResult;

import java.util.List;

/**
 * @author Yakir
 * @Topic DefaultNoticeResultDecision
 * @Description
 * @date 2022/1/20 11:12
 * @Version 1.0
 */
public class DefaultNoticeResultDecision implements NoticeResultDecision {

	@Override
	public boolean decide(List<ExceptionNoticeResult> noticeResults) {
		return noticeResults.stream().filter(e -> e.isSuccess()).findFirst().isPresent();
	}

}
