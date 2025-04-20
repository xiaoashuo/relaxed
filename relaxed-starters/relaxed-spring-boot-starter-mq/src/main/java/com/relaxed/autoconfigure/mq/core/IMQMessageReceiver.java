package com.relaxed.autoconfigure.mq.core;

/**
 * 消息队列接收者接口。 定义消息接收的基本操作，用于处理从消息队列接收到的消息。 实现该接口的类需要提供具体的消息处理逻辑。
 *
 * @author Yakir
 * @since 1.0
 */
public interface IMQMessageReceiver {

	/**
	 * 接收并处理消息。 当消息队列中有新消息到达时，该方法会被调用。 实现类需要提供具体的消息处理逻辑。
	 * @param msg 接收到的消息内容
	 */
	void receiveMsg(String msg);

}
