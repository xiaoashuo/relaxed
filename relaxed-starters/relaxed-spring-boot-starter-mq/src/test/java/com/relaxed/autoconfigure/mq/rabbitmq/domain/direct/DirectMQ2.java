package com.relaxed.autoconfigure.mq.rabbitmq.domain.direct;

import cn.hutool.json.JSONUtil;
import com.relaxed.autoconfigure.mq.core.domain.AbstractMQ;
import com.relaxed.autoconfigure.mq.core.domain.MQMeta;
import com.relaxed.autoconfigure.mq.core.enums.MQSendTypeEnum;
import com.relaxed.autoconfigure.mq.utils.MQHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
public class DirectMQ2 implements AbstractMQ {

	public static final String EXCHANGE_NAME = "exchange-direct-test";

	public static final String QUEUE_NAME = "queue-direct-test1";

	/** 内置msg 消息体定义 **/
	private DirectMQ2.MsgPayload payload;

	@Override
	public MQMeta getMQMeta() {
		MQMeta mqMeta = MQMeta.builder().exchangeMeta(MQMeta.ExchangeMeta.of(MQSendTypeEnum.QUEUE, EXCHANGE_NAME))
				.queueMeta(MQMeta.QueueMeta.of(QUEUE_NAME)).binding(true).routingKey("queue-direct-test-key").build();

		return MQHelper.register(mqMeta);
	}

	@Override
	public String toMessage() {
		return JSONUtil.toJsonStr(payload);
	}

	/** 【！重要配置项！】 构造MQModel , 一般用于发送MQ时 **/
	public static DirectMQ2 build(List<Long> userIdList) {
		return new DirectMQ2(new DirectMQ2.MsgPayload(userIdList));
	}

	/** 解析MQ消息， 一般用于接收MQ消息时 **/
	public static DirectMQ2.MsgPayload parse(String msg) {
		return JSONUtil.toBean(msg, DirectMQ2.MsgPayload.class);
	}

	/** 【！重要配置项！】 定义Msg消息载体 **/
	@Data
	@AllArgsConstructor
	public static class MsgPayload {

		/** 用户ID集合 **/
		private List<Long> userIdList;

	}

	/** 定义 IMQReceiver 接口： 项目实现该接口则可接收到对应的业务消息 **/
	public interface IMQReceiver {

		void receive(DirectMQ2.MsgPayload payload);

	}

}
