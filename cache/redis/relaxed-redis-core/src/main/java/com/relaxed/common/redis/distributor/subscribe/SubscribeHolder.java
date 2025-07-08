package com.relaxed.common.redis.distributor.subscribe;

import cn.hutool.extra.spring.SpringUtil;

import com.relaxed.common.redis.distributor.subscribe.SubscribeHandle;
import com.relaxed.common.redis.distributor.subscribe.SubscribeType;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;

/**
 * SubscribeHolder
 *
 * @author Yakir
 */
public class SubscribeHolder implements InitializingBean {

	/**
	 * 订阅者处理器
	 */
	private static final Map<String, SubscribeHandle> SUBSCRIBE_HOLDER = new HashMap<>();

	@Override
	public void afterPropertiesSet() {
		Map<String, SubscribeHandle> subscribeHandleMap = SpringUtil.getBeansOfType(SubscribeHandle.class);
		for (SubscribeHandle value : subscribeHandleMap.values()) {
			SUBSCRIBE_HOLDER.put(value.type().getChannel(), value);
		}
	}

	/**
	 * 根据渠道获取订阅者
	 * @param subscribeType
	 * @return SubscribeHandle
	 */
	public static SubscribeHandle getByChannel(SubscribeType subscribeType) {
		return getByChannel(subscribeType.getChannel());
	}

	/**
	 * 根据渠道获取订阅者
	 * @param channel
	 * @return SubscribeHandle
	 */
	public static SubscribeHandle getByChannel(String channel) {
		return SUBSCRIBE_HOLDER.get(channel);
	}

}
