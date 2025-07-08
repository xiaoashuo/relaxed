package com.relaxed.common.redis.distributor.subscribe;

/**
 * SubscribeType 订阅者类型
 *
 * @author Yakir
 */
public interface SubscribeType {

	/**
	 * 获取订阅者类型渠道
	 * @author yakir
	 * @date 2021/9/11 11:07
	 * @return java.lang.String
	 */
	String getChannel();

	/**
	 * 获取订阅者类型描述
	 * @author yakir
	 * @date 2021/9/11 11:07
	 * @return java.lang.String
	 */
	String getDesc();

}
