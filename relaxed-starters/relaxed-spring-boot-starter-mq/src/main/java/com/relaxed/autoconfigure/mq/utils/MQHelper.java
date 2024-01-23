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
 * @author Yakir
 * @Topic MQUtil
 * @Description
 * @date 2024/1/22 10:56
 * @Version 1.0
 */

public class MQHelper {

	private static Environment environment;

	public static void setEnvironment(Environment environment) {
		if (MQHelper.environment == null) {
			MQHelper.environment = environment;
		}
	}

	public static Environment getEnvironment() {
		return environment;
	}

	private static Map<String, MQMeta> globalMQMeta = new HashMap<>();

	public static Map<String, MQMeta> globalMQMeta() {
		return globalMQMeta;
	}

	public static MQMeta getMQMeta(String exchangeName, String queueName, String routeKey) {
		String mqKey = MD5.create().digestHex16(exchangeName + queueName + routeKey);
		return globalMQMeta.get(mqKey);
	}

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
