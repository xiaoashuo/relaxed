package com.relaxed.autoconfigure.mq.core.domain;

import com.relaxed.autoconfigure.mq.core.enums.MQSendTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息队列元数据类。 包含消息队列的配置信息，如队列、交换器、路由键等。 支持链式调用和构建器模式。
 *
 * @author Yakir
 * @since 1.0
 */
@Data
@Builder
@Accessors(chain = true)
public class MQMeta {

	/**
	 * 队列元数据 包含队列名称和参数配置
	 */
	private QueueMeta queueMeta;

	/**
	 * 交换器元数据 包含交换器类型、名称和参数配置
	 */
	private ExchangeMeta exchangeMeta;

	/**
	 * 交换器元数据类。 定义消息队列交换器的配置信息。
	 */
	@Data
	@NoArgsConstructor
	public static class ExchangeMeta {

		/**
		 * 交换器类型 定义消息的路由方式，如点对点、广播等
		 */
		private MQSendTypeEnum exchangeType;

		/**
		 * 交换器名称 用于标识和路由消息
		 */
		private String exchangeName;

		/**
		 * 交换器参数 用于配置交换器的特殊属性
		 */
		private Map<String, Object> args = new HashMap<>();

		/**
		 * 创建交换器元数据实例
		 * @param exchangeType 交换器类型
		 * @param exchangeName 交换器名称
		 * @param args 交换器参数
		 */
		public ExchangeMeta(MQSendTypeEnum exchangeType, String exchangeName, Map<String, Object> args) {
			this.exchangeType = exchangeType;
			this.exchangeName = exchangeName;
			this.args = args;
		}

		/**
		 * 创建交换器元数据实例，使用默认参数
		 * @param exchangeType 交换器类型
		 * @return 交换器元数据实例
		 */
		public static ExchangeMeta of(MQSendTypeEnum exchangeType) {
			return of(exchangeType, null, null);
		}

		/**
		 * 创建交换器元数据实例，使用默认参数
		 * @param exchangeType 交换器类型
		 * @param exchangeName 交换器名称
		 * @return 交换器元数据实例
		 */
		public static ExchangeMeta of(MQSendTypeEnum exchangeType, String exchangeName) {
			return of(exchangeType, exchangeName, null);
		}

		/**
		 * 创建交换器元数据实例
		 * @param exchangeType 交换器类型
		 * @param exchangeName 交换器名称
		 * @param args 交换器参数
		 * @return 交换器元数据实例
		 */
		public static ExchangeMeta of(MQSendTypeEnum exchangeType, String exchangeName, Map<String, Object> args) {
			return new ExchangeMeta(exchangeType, exchangeName, args);
		}

	}

	/**
	 * 队列元数据类。 定义消息队列的配置信息。
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class QueueMeta {

		/**
		 * 队列名称 用于标识和路由消息
		 */
		private String queueName;

		/**
		 * 队列参数 用于配置队列的特殊属性
		 */
		private Map<String, Object> args = new HashMap<>();

		/**
		 * 创建队列元数据实例，使用默认参数
		 * @param queueName 队列名称
		 * @return 队列元数据实例
		 */
		public static QueueMeta of(String queueName) {
			return of(queueName, null);
		}

		/**
		 * 创建队列元数据实例
		 * @param queueName 队列名称
		 * @param args 队列参数
		 * @return 队列元数据实例
		 */
		public static QueueMeta of(String queueName, Map<String, Object> args) {
			return new QueueMeta(queueName, args);
		}

	}

	/**
	 * 队列与交换器是否绑定 true表示已绑定，false表示未绑定
	 */
	private boolean binding;

	/**
	 * 路由键 用于消息路由，默认为空字符串
	 */
	private String routingKey = "";

}
