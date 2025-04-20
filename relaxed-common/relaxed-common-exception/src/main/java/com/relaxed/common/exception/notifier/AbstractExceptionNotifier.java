package com.relaxed.common.exception.notifier;

/**
 * 异常通知器抽象类
 * <p>
 * 实现了 {@link ExceptionNotifier} 接口，提供了通知器的基础功能。 包含通知渠道和应用名称等通用属性。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
public abstract class AbstractExceptionNotifier implements ExceptionNotifier {

	/**
	 * 通知渠道
	 * <p>
	 * 标识通知的具体实现类型，如MAIL、DING_TALK、WECHAT等。
	 * </p>
	 */
	protected final String channel;

	/**
	 * 应用名称
	 * <p>
	 * 标识发送通知的应用名称。
	 * </p>
	 */
	protected final String applicationName;

	/**
	 * 构造函数
	 * <p>
	 * 初始化通知渠道和应用名称。
	 * </p>
	 * @param channel 通知渠道
	 * @param applicationName 应用名称
	 */
	public AbstractExceptionNotifier(String channel, String applicationName) {
		this.channel = channel;
		this.applicationName = applicationName;
	}

	/**
	 * 获取通知渠道
	 * @return 通知渠道
	 */
	@Override
	public String getChannel() {
		return channel;
	}

}
