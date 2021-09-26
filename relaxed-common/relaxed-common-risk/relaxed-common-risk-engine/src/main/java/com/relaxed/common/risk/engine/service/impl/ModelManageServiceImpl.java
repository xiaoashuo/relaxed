package com.relaxed.common.risk.engine.service.impl;

import com.relaxed.common.risk.biz.distributor.event.EventDistributor;
import com.relaxed.common.risk.biz.service.ModelService;
import com.relaxed.common.risk.engine.cache.CacheKey;
import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.service.ModelManageService;
import com.relaxed.common.risk.model.enums.ModelEnums;
import com.relaxed.common.risk.model.vo.ModelVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yakir
 * @Topic ModelManageServiceImpl
 * @Description
 * @date 2021/8/29 10:48
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class ModelManageServiceImpl implements ModelManageService {

	/**
	 * 本地缓存
	 */
	private final CacheService cacheService;

	private final EventDistributor eventDistributor;

	private final ModelService modelService;

	@Override
	public Map<String, Long> listByStatus(Integer status) {
		return modelService.listByStatus(ModelEnums.StatusEnum.ENABLE.getStatus()).stream()
				.collect(Collectors.toMap(ModelVO::getGuid, ModelVO::getId));
	}

	/**
	 * 从本地缓存直接读取数据
	 * @author yakir
	 * @date 2021/8/29 10:51
	 * @param id
	 * @return com.relaxed.common.risk.model.vo.ModelVO
	 */
	@Override
	public ModelVO getById(Long id) {
		return cacheService.get(getModelCacheKey(id));
	}

	@Override
	public ModelVO getByGuid(String guid) {
		Long modelId = cacheService.get(getModelGuidCacheKey(guid));
		ModelVO modelVO = null;
		if (modelId != null) {
			modelVO = getById(modelId);
		}
		if (modelVO == null) {
			modelVO = modelService.getByGuid(guid);
			if (modelVO != null) {
				// 维护guid -> modelId映射
				cacheService.put(getModelGuidCacheKey(guid), modelVO.getId());
				cacheService.put(getModelCacheKey(modelVO.getId()), modelVO);
			}
		}
		return modelVO;
	}

	private String getModelGuidCacheKey(String guid) {
		return CacheKey.getModelGuidCacheKey(guid);
	}

	private String getModelCacheKey(Long id) {
		return CacheKey.getModelCacheKey(id);
	}

}
