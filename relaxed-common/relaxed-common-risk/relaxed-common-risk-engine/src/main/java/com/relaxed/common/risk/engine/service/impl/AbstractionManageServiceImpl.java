package com.relaxed.common.risk.engine.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.relaxed.common.risk.biz.service.AbstractionService;
import com.relaxed.common.risk.engine.cache.CacheKey;
import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.service.AbstractionManageService;
import com.relaxed.common.risk.model.vo.AbstractionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Yakir
 * @Topic AbstractionManageServiceImpl
 * @Description
 * @date 2021/8/29 18:53
 * @Version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AbstractionManageServiceImpl implements AbstractionManageService {

	private final AbstractionService abstractionService;

	private final CacheService cacheService;

	@Override
	public List<AbstractionVO> getByModelId(Long modelId) {
		String modelFieldCacheKey = getModelAbstractionCacheKey(modelId);
		List<AbstractionVO> abstractionVOS = cacheService.get(modelFieldCacheKey);
		// 本地缓存取
		if (CollectionUtil.isNotEmpty(abstractionVOS)) {
			return abstractionVOS;
		}
		// 查询db
		List<AbstractionVO> abstractionVOList = abstractionService.listByModelId(modelId);
		if (CollectionUtil.isNotEmpty(abstractionVOList)) {
			cacheService.put(getModelAbstractionCacheKey(modelId), abstractionVOList);
		}
		return abstractionVOList;
	}

	private String getModelAbstractionCacheKey(Long modelId) {
		return CacheKey.getModelAbstractionCacheKey(modelId);
	}

}
