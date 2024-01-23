package com.relaxed.autoconfigure.mq.core;

import com.relaxed.autoconfigure.mq.core.domain.AbstractMQ;

/**
 * @author Yakir
 * @Topic MQSender
 * @Description
 * @date 2021/12/23 15:58
 * @Version 1.0
 */
public interface IMQSender {

	/**
	 * 获取客户端
	 * @author yakir
	 * @date 2021/12/28 9:18
	 * @return C
	 */
	<C> C client();

	/**
	 * 推送MQ消息， 实时
	 * @param mqModel
	 */
	void send(AbstractMQ mqModel);

	/**
	 * 推送MQ消息， 延迟接收，单位：s
	 * @param mqModel
	 * @param delay
	 */
	void send(AbstractMQ mqModel, int delay);

}
