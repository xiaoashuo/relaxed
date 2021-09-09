package com.relaxed.common.risk.engine.core.distributor.subscribe.handles;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.risk.engine.cache.CacheKey;
import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.core.distributor.subscribe.SubscribeEnum;
import com.relaxed.common.risk.engine.core.distributor.subscribe.SubscribeHandle;
import com.relaxed.common.risk.engine.model.dto.DataListsDTO;
import com.relaxed.common.risk.engine.model.vo.DataListsVO;
import com.relaxed.common.risk.engine.model.vo.RuleVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

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
	public SubscribeEnum type() {
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
