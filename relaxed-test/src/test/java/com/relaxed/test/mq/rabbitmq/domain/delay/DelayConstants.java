package com.relaxed.test.mq.rabbitmq.domain.delay;

/**
 * @author Yakir
 * @Topic DelayConstants
 * @Description
 * @date 2021/12/27 15:42
 * @Version 1.0
 */
public class DelayConstants {

	/**
	 * 延迟交换机
	 */
	public static final String EXCHANGE_NAME = "exchange-delay-test";

	/**
	 * 延迟队列
	 */
	public static final String QUEUE_NAME = "queue-delay-test";

	/**
	 * 真实处理队列
	 */
	public static final String PROCESS_QUEUE_NAME = "queue-delay-real-test";

	/**
	 * 真实处理队列路由key
	 */
	public static final String PROCESS_QUEUE_ROUTE_KEY = "queue-delay-real-test";

}
