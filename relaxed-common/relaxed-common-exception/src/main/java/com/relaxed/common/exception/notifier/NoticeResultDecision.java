package com.relaxed.common.exception.notifier;

import com.relaxed.common.exception.domain.ExceptionNoticeResult;

import java.util.List;

/**
 * @author Yakir
 * @Topic NoticeSuccessDecision
 * @Description
 * @date 2022/1/20 11:09
 * @Version 1.0
 */
public interface NoticeResultDecision {

	/**
	 * 通知决策
	 * @author yakir
	 * @date 2022/1/20 11:13
	 * @param noticeResults
	 * @return boolean
	 */
	boolean decide(List<ExceptionNoticeResult> noticeResults);

}
