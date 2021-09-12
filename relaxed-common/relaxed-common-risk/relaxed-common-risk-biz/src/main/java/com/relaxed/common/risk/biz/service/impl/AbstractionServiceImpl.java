package com.relaxed.common.risk.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.repository.mapper.AbstractionMapper;
import com.relaxed.common.risk.biz.service.AbstractionService;
import com.relaxed.common.risk.model.converter.AbstractionConverter;
import com.relaxed.common.risk.model.entity.Abstraction;
import com.relaxed.common.risk.model.qo.AbstractionQO;
import com.relaxed.common.risk.model.vo.AbstractionVO;
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
 * @since 2021-08-29T18:48:19.507
 */
@RequiredArgsConstructor
@Service
public class AbstractionServiceImpl extends ExtendServiceImpl<AbstractionMapper, Abstraction>
		implements AbstractionService {

	@Override
	public PageResult<AbstractionVO> selectByPage(PageParam pageParam, AbstractionQO abstractionQO) {
		IPage<Abstraction> page = PageUtil.prodPage(pageParam);
		LambdaQueryWrapper<Abstraction> wrapper = Wrappers.lambdaQuery(Abstraction.class)
				.eq(ObjectUtil.isNotNull(abstractionQO.getId()), Abstraction::getId, abstractionQO.getId());
		this.baseMapper.selectPage(page, wrapper);
		IPage<AbstractionVO> voPage = page.convert(AbstractionConverter.INSTANCE::poToVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	@Override
	public List<AbstractionVO> listByModelId(Long modelId) {
		List<Abstraction> list = baseMapper.listByModelId(modelId);
		return list != null ? AbstractionConverter.INSTANCE.poToVOs(list) : null;
	}

}
