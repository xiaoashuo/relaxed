package com.relaxed.common.exception.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 异常通知消息
 * <p>
 * 用于封装异常通知的详细信息，包括异常标识、消息内容、发生次数等。 支持链式调用设置属性。
 * </p>
 *
 * @author lingting
 * @since 1.0.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class ExceptionMessage {

	/**
	 * 异常标识键
	 * <p>
	 * 用于唯一标识和筛选重复的异常。
	 * </p>
	 */
	private String key;

	/**
	 * 异常消息内容
	 * <p>
	 * 描述异常的具体信息。
	 * </p>
	 */
	private String message;

	/**
	 * 异常发生次数
	 * <p>
	 * 记录相同异常发生的次数。
	 * </p>
	 */
	private int number;

	/**
	 * 异常堆栈信息
	 * <p>
	 * 记录异常的完整堆栈跟踪信息。
	 * </p>
	 */
	private String stack;

	/**
	 * 异常最新触发时间
	 * <p>
	 * 记录异常最近一次发生的时间。
	 * </p>
	 */
	private String time;

	/**
	 * 机器MAC地址
	 * <p>
	 * 记录发生异常的机器MAC地址。
	 * </p>
	 */
	private String mac;

	/**
	 * 线程ID
	 * <p>
	 * 记录发生异常的线程ID。
	 * </p>
	 */
	private long threadId;

	/**
	 * 应用名称
	 * <p>
	 * 记录发生异常的应用名称。
	 * </p>
	 */
	private String applicationName;

	/**
	 * 主机名
	 * <p>
	 * 记录发生异常的主机名。
	 * </p>
	 */
	private String hostname;

	/**
	 * IP地址
	 * <p>
	 * 记录发生异常的机器IP地址。
	 * </p>
	 */
	private String ip;

	/**
	 * 增加异常发生次数
	 * <p>
	 * 将异常发生次数加1，并返回当前对象。
	 * </p>
	 * @return 当前异常消息对象
	 */
	public ExceptionMessage increment() {
		number++;
		return this;
	}

	/**
	 * 转换为字符串表示
	 * <p>
	 * 将异常消息的所有信息格式化为易读的字符串。
	 * </p>
	 * @return 格式化的异常消息字符串
	 */
	@Override
	public String toString() {
		return "服务名称：" + applicationName + "\nip：" + ip + "\nhostname：" + hostname + "\n机器地址：" + mac + "\n触发时间：" + time
				+ "\n线程id：" + threadId + "\n数量：" + number + "\n堆栈：" + stack;
	}

}
