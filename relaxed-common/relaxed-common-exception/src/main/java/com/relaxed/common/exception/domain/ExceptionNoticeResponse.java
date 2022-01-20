package com.relaxed.common.exception.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 异常消息通知响应
 *
 * @author lingting 2020/6/12 19:07
 */
@Getter
@Setter
@Accessors(chain = true)
public class ExceptionNoticeResponse {

	/**
	 * 是否成功 (任意一个成功即为成功)
	 */
	private boolean success;

	/**
	 * 通知者数量
	 */
	private Integer notifierCount;

	/**
	 * 单独的通知处理器结果
	 */
	List<ExceptionNoticeResult> noticeResults;

}
