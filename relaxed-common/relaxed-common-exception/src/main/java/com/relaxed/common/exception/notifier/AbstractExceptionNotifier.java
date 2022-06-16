package com.relaxed.common.exception.notifier;

/**
 * @author Yakir
 * @Topic AbstractExceptionNotifier
 * @Description
 * @date 2022/6/16 10:42
 * @Version 1.0
 */
public abstract class AbstractExceptionNotifier implements ExceptionNotifier {

	/**
	 * 发送渠道
	 */
	protected final String channel;

	/**
	 * 应用名称
	 */
	protected final String applicationName;

	public AbstractExceptionNotifier(String channel, String applicationName) {
		this.channel = channel;
		this.applicationName = applicationName;
	}

	@Override
	public String getChannel() {
		return channel;
	}

}
