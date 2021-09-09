package com.relaxed.common.risk.engine.core.distributor.subscribe.handles;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.risk.engine.cache.CacheKey;
import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.core.distributor.subscribe.SubscribeEnum;
import com.relaxed.common.risk.engine.core.distributor.subscribe.SubscribeHandle;
import com.relaxed.common.risk.engine.model.vo.FieldVO;
import com.relaxed.common.risk.engine.model.vo.ModelVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic SubscribeModelHandle
 * @Description 订阅model
 * @date 2021/8/29 9:34
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SubscribeFieldHandle implements SubscribeHandle {

	private final CacheService cacheService;

	@Override
	public SubscribeEnum type() {
		return SubscribeEnum.PUB_SUB_FIELD_CHANNEL;
	}

	@Override
	public void onMessage(String channel, String message) {
		log.info("model field update message:{}", message);
		FieldVO vo = JSONUtil.toBean(message, FieldVO.class);
		// 删除本地缓存的规则模型
		cacheService.del(CacheKey.getModelFieldCacheKey(vo.getModelId()));
	}

}
