package com.relaxed.common.risk.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.cache.annotation.Cached;
import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.repository.mapper.RuleMapper;
import com.relaxed.common.risk.biz.service.RuleService;
import com.relaxed.common.risk.model.converter.RuleConverter;
import com.relaxed.common.risk.model.entity.Rule;
import com.relaxed.common.risk.model.qo.RuleQO;
import com.relaxed.common.risk.model.vo.RuleVO;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.relaxed.extend.mybatis.plus.toolkit.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 业务层实现
 * </p>
 *
 * @author Yakir
 * @since 2021-08-31T11:30:23.273
 */
@RequiredArgsConstructor
@Service
public class RuleServiceImpl extends ExtendServiceImpl<RuleMapper, Rule> implements RuleService {

	@Override
	public PageResult<RuleVO> selectByPage(PageParam pageParam, RuleQO ruleQO) {
		IPage<Rule> page = PageUtil.prodPage(pageParam);
		LambdaQueryWrapper<Rule> wrapper = Wrappers.lambdaQuery(Rule.class).eq(ObjectUtil.isNotNull(ruleQO.getId()),
				Rule::getId, ruleQO.getId());
		this.baseMapper.selectPage(page, wrapper);
		IPage<RuleVO> voPage = page.convert(RuleConverter.INSTANCE::poToVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	@Override
	public List<RuleVO> listByActivationId(Long activationId) {
		List<Rule> list = baseMapper.listByActivationId(activationId);
		return RuleConverter.INSTANCE.poToVOs(list);
	}

	@Override
	public Rule getByName(String ruleName) {
		return baseMapper.selectOne(ruleName);
	}

}
