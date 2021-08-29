package com.relaxed.common.risk.engine.manage.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.relaxed.common.risk.engine.cache.CacheKey;
import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.manage.FieldManageService;
import com.relaxed.common.risk.engine.model.entity.Field;
import com.relaxed.common.risk.engine.model.vo.FieldVO;
import com.relaxed.common.risk.engine.service.FieldService;
import com.relaxed.common.risk.engine.service.ModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

	private String getModelFieldCacheKey(Long modelId) {
		return CacheKey.getModelFieldCacheKey(modelId);
	}

}
