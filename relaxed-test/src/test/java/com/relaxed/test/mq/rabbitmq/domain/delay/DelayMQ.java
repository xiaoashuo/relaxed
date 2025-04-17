package com.relaxed.test.mq.rabbitmq.domain.delay;

import cn.hutool.json.JSONUtil;
import com.relaxed.autoconfigure.mq.core.domain.AbstractMQ;
import com.relaxed.autoconfigure.mq.core.domain.MQMeta;
import com.relaxed.autoconfigure.mq.core.enums.MQSendTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic DirectMQ
 * @Description
 * @date 2021/12/27 11:23
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelayMQ implements AbstractMQ {

	/** 内置msg 消息体定义 **/
	private DelayMQ.MsgPayload payload;

	@Override
	public MQMeta getMQMeta() {
		Map<String, Object> args = new HashMap<>();
		args.put("x-dead-letter-exchange", DelayConstants.EXCHANGE_NAME);
		args.put("x-dead-letter-routing-key", DelayConstants.PROCESS_QUEUE_ROUTE_KEY);
		MQMeta.QueueMeta queueMeta = MQMeta.QueueMeta.of(DelayConstants.QUEUE_NAME, args);
		return MQMeta.builder().queueMeta(queueMeta).exchangeMeta(MQMeta.ExchangeMeta.of(MQSendTypeEnum.DELAY)).build();
	}

	@Override
	public String toMessage() {
		return JSONUtil.toJsonStr(payload);
	}

	/** 【！重要配置项！】 构造MQModel , 一般用于发送MQ时 **/
	public static DelayMQ build(List<Long> userIdList) {
		return new DelayMQ(new DelayMQ.MsgPayload(userIdList));
	}

	/** 【！重要配置项！】 定义Msg消息载体 **/
	@Data
	@AllArgsConstructor
	public static class MsgPayload {

		/** 用户ID集合 **/
		private List<Long> userIdList;

	}

}
