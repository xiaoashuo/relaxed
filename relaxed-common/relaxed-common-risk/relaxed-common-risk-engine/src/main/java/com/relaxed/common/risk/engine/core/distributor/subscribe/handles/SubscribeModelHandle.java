package com.relaxed.common.risk.engine.core.distributor.subscribe.handles;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.risk.engine.cache.CacheKey;
import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.core.distributor.subscribe.SubscribeEnum;
import com.relaxed.common.risk.engine.core.distributor.subscribe.SubscribeHandle;
import com.relaxed.common.risk.engine.model.vo.ModelVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yakir
 * @Topic SubscribeModelHandle
 * @Description 订阅model
 * @date 2021/8/29 9:34
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
public class SubscribeModelHandle implements SubscribeHandle {

	private final CacheService cacheService;

	@Override
	public SubscribeEnum type() {
		return SubscribeEnum.PUB_SUB_MODEL_CHANNEL;
	}

	@Override
	public void onMessage(String channel, String message) {
		log.info("model update message:{}", message);
		ModelVO vo = JSONUtil.toBean(message, ModelVO.class);
		// 删除本地缓存的规则模型
		cacheService.del(CacheKey.getModelCacheKey(vo.getId()));
	}

}
