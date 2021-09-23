package com.relaxed.common.risk.engine.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.relaxed.common.risk.biz.service.FieldService;
import com.relaxed.common.risk.biz.service.PreItemService;
import com.relaxed.common.risk.engine.cache.CacheKey;
import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.service.FieldManageService;
import com.relaxed.common.risk.model.vo.FieldVO;
import com.relaxed.common.risk.model.vo.PreItemVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic FieldManageServiceImpl
 * @Description
 * @date 2021/8/29 12:19
 * @Version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FieldManageServiceImpl implements FieldManageService {

	private final FieldService fieldService;

	private final PreItemService preItemService;

	private final CacheService cacheService;

	@Override
	public List<FieldVO> getFieldVos(Long modelId) {
		String modelFieldCacheKey = getModelFieldCacheKey(modelId);
		List<FieldVO> fieldVOS = cacheService.get(modelFieldCacheKey);
		// 本地缓存取
		if (CollectionUtil.isNotEmpty(fieldVOS)) {
			return fieldVOS;
		}
		// 查询db
		List<FieldVO> fieldList = fieldService.listByModelId(modelId);
		if (CollectionUtil.isNotEmpty(fieldList)) {
			cacheService.put(getModelFieldCacheKey(modelId), fieldList);
		}
		return fieldList;
	}

	@Override
	public Map<String, String> getFieldTypeMap(Long modelId) {
		Map<String, String> fieldMap = new HashMap<>();
		List<FieldVO> fieldVos = getFieldVos(modelId);
		List<PreItemVO> preItems = getPreItems(modelId);
		fieldVos.forEach(item -> {
			fieldMap.put(item.getFieldName(), item.getFieldType());
		});
		preItems.forEach(item -> {
			fieldMap.put(item.getDestField(), item.getDestFieldType());
		});
		return fieldMap;
	}

	@Override
	public List<PreItemVO> getPreItems(Long modelId) {
		String modelFieldCacheKey = getModelPreItemCacheKey(modelId);
		List<PreItemVO> preItemVOS = cacheService.get(modelFieldCacheKey);
		// 本地缓存取
		if (CollectionUtil.isNotEmpty(preItemVOS)) {
			return preItemVOS;
		}
		// 查询db
		List<PreItemVO> preItemVOList = preItemService.listByModelId(modelId);
		if (CollectionUtil.isNotEmpty(preItemVOList)) {
			cacheService.put(modelFieldCacheKey, preItemVOList);
		}
		return preItemVOList;
	}

	private String getModelPreItemCacheKey(Long modelId) {
		return CacheKey.getModelPreItemCacheKey(modelId);
	}

	private String getModelFieldCacheKey(Long modelId) {
		return CacheKey.getModelFieldCacheKey(modelId);
	}

}
