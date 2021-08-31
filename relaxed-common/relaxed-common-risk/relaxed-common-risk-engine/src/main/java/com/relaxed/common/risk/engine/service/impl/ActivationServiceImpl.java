package com.relaxed.common.risk.engine.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.risk.engine.model.converter.ActivationConverter;
import com.relaxed.common.risk.engine.model.qo.ActivationQO;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;

import com.relaxed.common.risk.engine.mapper.ActivationMapper;
import com.relaxed.common.risk.engine.service.ActivationService;
import com.relaxed.common.risk.engine.model.dto.ActivationDTO;
import com.relaxed.common.risk.engine.model.entity.Activation;
import com.relaxed.common.risk.engine.model.vo.ActivationVO;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
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
 * @since 2021-08-29T18:48:19.435
 */
@RequiredArgsConstructor
@Service
public class ActivationServiceImpl extends ExtendServiceImpl<ActivationMapper, Activation>
		implements ActivationService {

	@Override
	public PageResult<ActivationVO> selectByPage(PageParam pageParam, ActivationQO activationQO) {
		IPage<Activation> page = PageUtil.prodPage(pageParam);
		LambdaQueryWrapper<Activation> wrapper = Wrappers.lambdaQuery(Activation.class)
				.eq(ObjectUtil.isNotNull(activationQO.getId()), Activation::getId, activationQO.getId());
		this.baseMapper.selectPage(page, wrapper);
		IPage<ActivationVO> voPage = page.convert(ActivationConverter.INSTANCE::poToVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	@Override
	public List<ActivationVO> listByModelId(Long modelId) {
		List<Activation> activations = baseMapper.listByModelId(modelId);
		return ActivationConverter.INSTANCE.poToVOs(activations);
	}

}
