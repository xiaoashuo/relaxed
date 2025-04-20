package com.relaxed.autoconfigure.mq.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.relaxed.autoconfigure.mq.core.domain.MQMeta;
import com.relaxed.autoconfigure.mq.core.enums.MQSendTypeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * MQ工具类
 * <p>
 * 提供消息队列相关的工具方法，包括环境配置管理、MQ元数据注册和获取等功能。 支持从环境配置中解析参数，管理全局MQ元数据。
 * </p>
 *
 * @author Yakir
 * @since 1.0
 */
public class MQHelper {

	private static Environment environment;

	/**
	 * 设置环境配置
	 * @param environment Spring环境配置
	 */
	public static void setEnvironment(Environment environment) {
		if (MQHelper.environment == null) {
			MQHelper.environment = environment;
		}
	}

	/**
	 * 获取环境配置
	 * @return Spring环境配置
	 */
	public static Environment getEnvironment() {
		return environment;
	}

	private static Map<String, MQMeta> globalMQMeta = new HashMap<>();

	/**
	 * 获取全局MQ元数据映射
	 * @return 全局MQ元数据映射
	 */
	public static Map<String, MQMeta> globalMQMeta() {
		return globalMQMeta;
	}

	/**
	 * 根据交换器名称、队列名称和路由键获取MQ元数据
	 * @param exchangeName 交换器名称
	 * @param queueName 队列名称
	 * @param routeKey 路由键
	 * @return MQ元数据，如果不存在则返回null
	 */
	public static MQMeta getMQMeta(String exchangeName, String queueName, String routeKey) {
		String mqKey = MD5.create().digestHex16(exchangeName + queueName + routeKey);
		return globalMQMeta.get(mqKey);
	}

	/**
	 * 注册MQ元数据
	 * <p>
	 * 根据交换器名称、队列名称和路由键创建并注册MQ元数据。 如果已存在相同键的元数据，则返回已存在的元数据。
	 * </p>
	 * @param exchangeName 交换器名称
	 * @param queueName 队列名称
	 * @param routeKey 路由键
	 * @return 注册的MQ元数据
	 */
	public static MQMeta register(String exchangeName, String queueName, String routeKey) {
		String mqKey = MD5.create().digestHex16(exchangeName + queueName + routeKey);
		if (globalMQMeta.containsKey(mqKey)) {
			return globalMQMeta.get(mqKey);
		}
		String exchangeNamePlain = resolveParams(exchangeName);
		String queueNamePlain = resolveParams(queueName);
		String routeKeyPlain = resolveParams(routeKey);

		MQMeta mqMeta = MQMeta.builder().exchangeMeta(MQMeta.ExchangeMeta.of(MQSendTypeEnum.QUEUE, exchangeNamePlain))
				.queueMeta(MQMeta.QueueMeta.of(queueNamePlain)).binding(true).routingKey(routeKeyPlain).build();
		globalMQMeta.put(mqKey, mqMeta);
		return mqMeta;

	}

	/**
	 * 注册MQ元数据
	 * <p>
	 * 直接注册已创建的MQ元数据。 如果已存在相同键的元数据，则返回已存在的元数据。
	 * </p>
	 * @param mqMeta 要注册的MQ元数据
	 * @return 注册的MQ元数据
	 */
	public static MQMeta register(MQMeta mqMeta) {
		String exchangeNamePlain = mqMeta.getExchangeMeta().getExchangeName();
		String queueNamePlain = mqMeta.getQueueMeta().getQueueName();
		String routeKeyPlain = mqMeta.getRoutingKey();
		String mqKey = MD5.create().digestHex16(exchangeNamePlain + queueNamePlain + routeKeyPlain);
		if (globalMQMeta.containsKey(mqKey)) {
			return globalMQMeta.get(mqKey);
		}
		globalMQMeta.put(mqKey, mqMeta);
		return mqMeta;
	}

	/**
	 * 解析参数
	 * <p>
	 * 如果参数是Spring环境变量占位符（以${开头，以}结尾），则解析为实际值。 否则返回原值。
	 * </p>
	 * @param paramName 参数名称
	 * @return 解析后的参数值
	 */
	private static String resolveParams(String paramName) {
		String paramNamePlain;
		if (StrUtil.startWith(paramName, "${") && StrUtil.endWith(paramName, "}")) {
			paramNamePlain = environment.resolveRequiredPlaceholders(paramName);
		}
		else {
			paramNamePlain = paramName;
		}
		return paramNamePlain;
	}

}
