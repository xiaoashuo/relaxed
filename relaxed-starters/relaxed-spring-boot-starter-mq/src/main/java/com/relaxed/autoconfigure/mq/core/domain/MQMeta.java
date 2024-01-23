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
 * @author Yakir
 * @Topic MQMeta
 * @Description
 * @date 2021/12/27 10:48
 * @Version 1.0
 */
@Data
@Builder
@Accessors(chain = true)
public class MQMeta {

	private QueueMeta queueMeta;

	private ExchangeMeta exchangeMeta;

	@Data
	@NoArgsConstructor
	public static class ExchangeMeta {

		/**
		 * 交换机类型
		 */
		private MQSendTypeEnum exchangeType;

		/**
		 * 交换器名称
		 */
		private String exchangeName;

		/**
		 * 交换机参数
		 */
		private Map<String, Object> args = new HashMap<>();

		public ExchangeMeta(MQSendTypeEnum exchangeType, String exchangeName, Map<String, Object> args) {
			this.exchangeType = exchangeType;
			this.exchangeName = exchangeName;
			this.args = args;
		}

		public static ExchangeMeta of(MQSendTypeEnum exchangeType) {
			return of(exchangeType, null, null);
		}

		public static ExchangeMeta of(MQSendTypeEnum exchangeType, String exchangeName) {
			return of(exchangeType, exchangeName, null);
		}

		public static ExchangeMeta of(MQSendTypeEnum exchangeType, String exchangeName, Map<String, Object> args) {
			return new ExchangeMeta(exchangeType, exchangeName, args);
		}

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class QueueMeta {

		/**
		 * 队列名称
		 */
		private String queueName;

		/**
		 * 队列参数
		 */
		private Map<String, Object> args = new HashMap<>();

		public static QueueMeta of(String queueName) {
			return of(queueName, null);
		}

		public static QueueMeta of(String queueName, Map<String, Object> args) {
			return new QueueMeta(queueName, args);
		}

	}

	/**
	 * 队列与交换器 是否绑定
	 */
	private boolean binding;

	/**
	 * 路由key
	 * @author yakir
	 * @date 2021/12/27 11:14
	 */
	private String routingKey = "";

}
