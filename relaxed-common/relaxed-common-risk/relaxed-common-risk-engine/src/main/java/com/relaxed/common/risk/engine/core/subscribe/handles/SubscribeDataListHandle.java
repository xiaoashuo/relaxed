package com.relaxed.common.risk.engine.core.subscribe.handles;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.risk.biz.distributor.subscribe.SubscribeHandle;
import com.relaxed.common.risk.biz.distributor.subscribe.SubscribeType;
import com.relaxed.common.risk.engine.cache.CacheKey;
import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.biz.distributor.subscribe.SubscribeEnum;
import com.relaxed.common.risk.model.vo.DataListsVO;
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
public class SubscribeDataListHandle implements SubscribeHandle {

	private final CacheService cacheService;

	@Override
	public SubscribeType type() {
		return SubscribeEnum.PUB_SUB_DATALIST_CHANNEL;
	}

	@Override
	public void onMessage(String channel, String message) {
		log.info("data list update message:{}", message);
		DataListsVO vo = JSONUtil.toBean(message, DataListsVO.class);
		// 删除本地缓存的黑白名单数据
		cacheService.del(CacheKey.getDataListCacheKey(vo.getModelId()));
	}

}
