package com.relaxed.common.risk.engine.manage.impl;

import com.relaxed.common.risk.engine.cache.CacheKey;
import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.core.distributor.EventDistributor;
import com.relaxed.common.risk.engine.enums.ModelEnums;
import com.relaxed.common.risk.engine.manage.ModelManageService;
import com.relaxed.common.risk.engine.model.converter.ModelConverter;
import com.relaxed.common.risk.engine.model.entity.Model;
import com.relaxed.common.risk.engine.model.vo.ModelVO;
import com.relaxed.common.risk.engine.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

	/**
	 * 维护GUID到modelId的映射 TODO 存在问题 guid 不能是实时同步
	 */
	private Map<String, Long> guidMap;

	@PostConstruct
	public void init() {

		guidMap = modelService.listByStatus(ModelEnums.StatusEnum.ENABLE.getStatus()).stream()
				.collect(Collectors.toMap(ModelVO::getGuid, ModelVO::getId));

	}

	/**
	 * 从本地缓存直接读取数据
	 * @author yakir
	 * @date 2021/8/29 10:51
	 * @param id
	 * @return com.relaxed.common.risk.engine.model.vo.ModelVO
	 */
	@Override
	public ModelVO getById(Long id) {
		return cacheService.get(getModelCacheKey(id));
	}

	@Override
	public ModelVO getByGuid(String guid) {
		Long modelId = guidMap.get(guid);
		ModelVO modelVO = null;
		if (modelId != null) {
			modelVO = getById(modelId);
		}
		if (modelVO == null) {
			modelVO = modelService.getByGuid(guid);
			if (modelVO != null) {
				// 维护guid -> modelId映射
				guidMap.put(modelVO.getGuid(), modelVO.getId());
				cacheService.put(getModelCacheKey(modelVO.getId()), modelVO);
			}
		}
		return modelVO;
	}

	private String getModelCacheKey(Long id) {
		return CacheKey.getModelCacheKey(id);
	}

}
