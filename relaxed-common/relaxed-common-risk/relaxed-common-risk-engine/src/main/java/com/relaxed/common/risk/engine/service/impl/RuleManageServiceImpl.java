package com.relaxed.common.risk.engine.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.relaxed.common.risk.biz.service.RuleService;
import com.relaxed.common.risk.engine.cache.CacheKey;
import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.service.RuleManageService;
import com.relaxed.common.risk.model.vo.RuleVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Yakir
 * @Topic RuleManageServiceImpl
 * @Description
 * @date 2021/8/31 11:33
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class RuleManageServiceImpl implements RuleManageService {

	private final RuleService ruleService;

	private final CacheService cacheService;

	@Override
	public List<RuleVO> listRule(Long activationId) {
		List<RuleVO> ruleVOS = cacheService.get(getRuleListCacheKey(activationId));
		// 本地缓存取
		if (CollectionUtil.isNotEmpty(ruleVOS)) {
			return ruleVOS;
		}
		// 查询db
		List<RuleVO> ruleVOList = ruleService.listByActivationId(activationId);
		if (CollectionUtil.isNotEmpty(ruleVOList)) {
			cacheService.put(getRuleListCacheKey(activationId), ruleVOList);
		}
		return ruleVOList;
	}

	private String getRuleListCacheKey(Long activationId) {
		return CacheKey.getRuleListCacheKey(activationId);
	}

}
