package com.relaxed.autoconfigure.exception;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 异常处理配置属性类 用于配置异常通知的相关属性，如通知渠道、忽略异常、通知间隔等
 *
 * @author lingting
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "relaxed.exception")
public class ExceptionHandleProperties {

	/**
	 * 通知渠道配置 key为渠道名称，value为是否启用该渠道
	 */
	private Map<String, Boolean> channels = new HashMap<>();

	/**
	 * 需要忽略的异常类集合 注意：只会忽略指定的异常类，不会忽略其子类
	 */
	private Set<Class<? extends Throwable>> ignoreExceptions = new HashSet<>();

	/**
	 * 通知间隔时间，单位为秒 默认值为5分钟
	 */
	private long time = TimeUnit.MINUTES.toSeconds(5);

	/**
	 * 异常通知阈值 当异常数量达到此阈值时，即使未达到通知间隔时间，也会立即发送通知
	 */
	private int max = 5;

	/**
	 * 异常堆栈信息转换为字符串时的最大长度 默认值为3000字符
	 */
	private int length = 3000;

	/**
	 * 接收异常通知的邮箱地址集合
	 */
	private Set<String> receiveEmails = new HashSet<>();

}
