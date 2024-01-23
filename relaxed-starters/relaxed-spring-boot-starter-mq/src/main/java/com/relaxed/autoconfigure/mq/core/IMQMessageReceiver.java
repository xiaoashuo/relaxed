package com.relaxed.autoconfigure.mq.core;

/**
 * @author Yakir
 * @Topic MQMessageReceive
 * @Description
 * @date 2021/12/23 16:02
 * @Version 1.0
 */
public interface IMQMessageReceiver {

	/**
	 * 接收消息
	 * @author yakir
	 * @date 2021/12/23 16:03
	 * @param msg
	 */
	void receiveMsg(String msg);

}
