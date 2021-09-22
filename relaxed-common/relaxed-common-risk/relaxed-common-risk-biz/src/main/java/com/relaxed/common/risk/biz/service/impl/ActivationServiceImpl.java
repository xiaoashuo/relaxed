package com.relaxed.common.risk.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.biz.distributor.EventDistributor;
import com.relaxed.common.risk.biz.distributor.subscribe.SubscribeEnum;
import com.relaxed.common.risk.model.converter.AbstractionConverter;
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
import org.springframework.util.Assert;

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

	private final EventDistributor eventDistributor;

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

	@Override
	public boolean add(Activation activation) {
		if (SqlHelper.retBool(baseMapper.insert(activation))) {
			eventDistributor.distribute(SubscribeEnum.PUB_SUB_ACTIVATION_CHANNEL.getChannel(),
					JSONUtil.toJsonStr(ActivationConverter.INSTANCE.poToVo(activation)));
			return true;
		}
		return false;
	}

	@Override
	public boolean edit(Activation activation) {
		Long activationId = activation.getId();
		Activation sqlActivation = baseMapper.selectById(activationId);
		Assert.notNull(sqlActivation, "activation can not exists.");
		if (updateById(activation)) {
			eventDistributor.distribute(SubscribeEnum.PUB_SUB_ACTIVATION_CHANNEL.getChannel(),
					JSONUtil.toJsonStr(ActivationConverter.INSTANCE.poToVo(activation)));
			return true;
		}
		return false;
	}

	@Override
	public boolean del(Long id) {
		Activation sqlActivation = baseMapper.selectById(id);
		Assert.notNull(sqlActivation, "activation can not exists.");
		if (removeById(id)) {
			eventDistributor.distribute(SubscribeEnum.PUB_SUB_ACTIVATION_CHANNEL.getChannel(),
					JSONUtil.toJsonStr(ActivationConverter.INSTANCE.poToVo(sqlActivation)));
			return true;
		}
		return false;
	}

}
