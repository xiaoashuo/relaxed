package com.relaxed.common.risk.engine.manage.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.risk.engine.cache.CacheKey;
import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.core.plugins.PluginManager;
import com.relaxed.common.risk.engine.enums.PreItemEnum;
import com.relaxed.common.risk.engine.manage.PreItemManageService;
import com.relaxed.common.risk.engine.model.vo.PreItemVO;
import com.relaxed.common.risk.engine.service.PreItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic PreItemManageServiceImpl
 * @Description
 * @date 2021/8/29 14:01
 * @Version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PreItemManageServiceImpl implements PreItemManageService {

	private final PreItemService preItemService;

	private final CacheService cacheService;

	@Override
	public Map<String, Object> prepare(Long modelId, Map<String, Object> jsonInfo) {
		List<PreItemVO> preItemVOList = getByModelId(modelId);
		Map<String, Object> result = new HashMap<>();
		for (PreItemVO preItemVO : preItemVOList) {
			if (!PreItemEnum.StatusEnum.ENABLE.getStatus().equals(preItemVO.getStatus())) {
				continue;
			}
			String pluginName = preItemVO.getPlugin();
			if (StrUtil.isEmpty(pluginName)) {
				continue;
			}
			String sourceField = preItemVO.getSourceField();
			if (StrUtil.isEmpty(sourceField)) {
				continue;
			}
			String[] sourceFields = sourceField.split(StrPool.COMMA);
			// 转换后的结果
			Object transfer = PluginManager.of(pluginName).handle(preItemVO, jsonInfo, sourceFields);
			result.put(preItemVO.getDestField(), transfer);
		}
		return result;
	}

	@Override
	public List<PreItemVO> getByModelId(Long modelId) {
		List<PreItemVO> preItemVOS = cacheService.get(getModelPreItemCacheKey(modelId));
		if (CollectionUtil.isNotEmpty(preItemVOS)) {
			return preItemVOS;
		}
		List<PreItemVO> preItemVOList = preItemService.getByModelId(modelId);
		if (CollectionUtil.isNotEmpty(preItemVOList)) {
			cacheService.put(getModelPreItemCacheKey(modelId), preItemVOList);
		}
		return preItemVOList;
	}

	private String getModelPreItemCacheKey(Long modelId) {
		return CacheKey.getModelPreItemCacheKey(modelId);
	}

}
