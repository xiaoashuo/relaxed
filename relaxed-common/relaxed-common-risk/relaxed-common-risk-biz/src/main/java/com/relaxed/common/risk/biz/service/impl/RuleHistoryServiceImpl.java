package com.relaxed.common.risk.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.repository.mapper.RuleHistoryMapper;
import com.relaxed.common.risk.biz.service.RuleHistoryService;
import com.relaxed.common.risk.model.converter.RuleHistoryConverter;
import com.relaxed.common.risk.model.entity.RuleHistory;
import com.relaxed.common.risk.model.qo.RuleHistoryQO;
import com.relaxed.common.risk.model.vo.RuleHistoryVO;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.relaxed.extend.mybatis.plus.toolkit.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务层实现
 * </p>
 *
 * @author Yakir
 * @since 2021-08-31T11:30:23.317
 */
@RequiredArgsConstructor
@Service
public class RuleHistoryServiceImpl extends ExtendServiceImpl<RuleHistoryMapper, RuleHistory>
		implements RuleHistoryService {

	@Override
	public PageResult<RuleHistoryVO> selectByPage(PageParam pageParam, RuleHistoryQO ruleHistoryQO) {
		IPage<RuleHistory> page = PageUtil.prodPage(pageParam);
		LambdaQueryWrapper<RuleHistory> wrapper = Wrappers.lambdaQuery(RuleHistory.class)
				.eq(ObjectUtil.isNotNull(ruleHistoryQO.getId()), RuleHistory::getId, ruleHistoryQO.getId());
		this.baseMapper.selectPage(page, wrapper);
		IPage<RuleHistoryVO> voPage = page.convert(RuleHistoryConverter.INSTANCE::poToVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

}
