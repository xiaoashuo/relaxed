package com.relaxed.common.exception.holder;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.relaxed.common.exception.domain.ExceptionMessage;
import com.relaxed.common.exception.domain.ExceptionNoticeResponse;
import com.relaxed.common.exception.domain.ExceptionNoticeResult;
import com.relaxed.common.exception.notifier.ExceptionNotifier;
import com.relaxed.common.exception.notifier.NoticeResultDecision;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic ExceptionNotifierHolder
 * @Description
 * @date 2022/1/20 10:45
 * @Version 1.0
 */
@Slf4j
public class ExceptionNotifierHolder {

	private final List<ExceptionNotifier> notifiers;

	private final NoticeResultDecision decision;

	private final Integer notifierCount;

	public ExceptionNotifierHolder(List<ExceptionNotifier> notifiers, NoticeResultDecision decision) {
		Assert.notEmpty(notifiers, "At least one Notifier");
		this.notifiers = notifiers;
		this.notifierCount = notifiers.size();
		this.decision = decision;
	}

	/**
	 * 执行异常通知
	 * @author yakir
	 * @date 2022/1/20 10:59
	 * @param exceptionMessage
	 * @return com.relaxed.common.exception.domain.ExceptionNoticeResponse
	 */
	public ExceptionNoticeResponse notice(ExceptionMessage exceptionMessage) {
		ExceptionNoticeResponse noticeResponse = new ExceptionNoticeResponse();
		List<ExceptionNoticeResult> noticeResults = new ArrayList<>();
		for (ExceptionNotifier notifier : notifiers) {
			String noticeType = notifier.getChannel();
			try {
				ExceptionNoticeResult noticeResult = notifier.send(exceptionMessage);
				noticeResults.add(noticeResult);
			}
			catch (Exception e) {
				log.error("异常通知器{},通知失败", noticeType, e);
				noticeResults.add(ExceptionNoticeResult.ofFail(noticeType, ExceptionUtil.getMessage(e)));
			}
		}
		noticeResponse.setNotifierCount(notifierCount);
		noticeResponse.setNoticeResults(noticeResults);
		noticeResponse.setSuccess(decision.decide(noticeResults));
		return noticeResponse;
	}

}
