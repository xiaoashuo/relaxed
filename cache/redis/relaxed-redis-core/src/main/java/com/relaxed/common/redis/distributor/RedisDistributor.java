package com.relaxed.common.redis.distributor;

import com.relaxed.common.redis.distributor.subscribe.SubscribeHandle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * RedisDistributor
 *
 * @author Yakir
 */
@RequiredArgsConstructor
@Slf4j
public class RedisDistributor implements EventDistributor {

	private final StringRedisTemplate stringRedisTemplate;

	@Override
	public void distribute(String channel, String message) {
		log.info("distribute channel={},message={}", channel, message);
		stringRedisTemplate.convertAndSend(channel, message);
	}

	@Override
	public void subscribe(String channel, SubscribeHandle subscribeHandle) {
		log.info("subscribe channel={}", channel);
		stringRedisTemplate.getConnectionFactory().getConnection().subscribe((msg, pat) -> {
			byte[] bytes = msg.getBody();
			subscribeHandle.onMessage(channel, new String(bytes));
		}, channel.getBytes());
		log.info("subscribe success channel={}", channel);
	}

}
