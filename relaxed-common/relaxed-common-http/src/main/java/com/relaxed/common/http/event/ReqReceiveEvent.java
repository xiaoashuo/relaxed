package com.relaxed.common.http.event;

import com.relaxed.common.http.core.request.IRequest;
import com.relaxed.common.http.core.response.IResponse;
import com.relaxed.common.http.domain.RequestForm;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @author Yakir
 * @Topic ReqReceiveEvent
 * @Description
 * @date 2022/2/8 11:24
 * @Version 1.0
 */
@Setter
@Getter
@ToString
public class ReqReceiveEvent extends ApplicationEvent {

	/**
	 * 渠道
	 */
	private final String channel;

	/**
	 * 请求地址
	 */
	private final String url;

	/**
	 * 原始请求带参数
	 */
	private final IRequest request;

	/**
	 * 转换后的请求参数
	 */
	private final RequestForm requestForm;

	/**
	 * 转换后的响应
	 */
	private final IResponse response;

	/**
	 * 异常信息
	 */
	private Throwable throwable;

	/**
	 * 开始时间
	 */
	private Long startTime;

	/**
	 * 结束时间
	 */
	private Long endTime;

	public ReqReceiveEvent(String channel, String url, IRequest request, RequestForm requestForm, IResponse response,
			Throwable throwable, Long startTime, Long endTime) {
		super(url);
		this.channel = channel;
		this.url = url;
		this.request = request;
		this.requestForm = requestForm;
		this.response = response;
		this.throwable = throwable;
		this.startTime = startTime;
		this.endTime = endTime;
	}

}
