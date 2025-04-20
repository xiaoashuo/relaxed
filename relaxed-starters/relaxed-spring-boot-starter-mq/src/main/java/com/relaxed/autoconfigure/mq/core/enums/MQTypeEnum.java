package com.relaxed.autoconfigure.mq.core.enums;

/**
 * 消息队列类型枚举。 定义系统支持的消息队列类型，包括RabbitMQ、RocketMQ等。
 *
 * @author Yakir
 * @since 1.0
 */
public enum MQTypeEnum {

	/**
	 * RabbitMQ消息队列 基于AMQP协议的消息队列，支持多种消息模式
	 */
	RABBIT_MQ,

	/**
	 * RocketMQ消息队列 阿里巴巴开源的消息队列，支持高吞吐量和高可用性
	 */
	ROCKET_MQ,

	/**
	 * 其他类型消息队列 用于支持自定义或第三方消息队列实现
	 */
	OTHER,

}
