package com.relaxed.common.risk.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.repository.mapper.ActivationMapper;
import com.relaxed.common.risk.biz.service.ActivationService;
import com.relaxed.common.risk.model.converter.ActivationConverter;
import com.relaxed.common.risk.model.entity.Activation;
import com.relaxed.common.risk.model.qo.ActivationQO;
import com.relaxed.common.risk.model.vo.ActivationVO;
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
