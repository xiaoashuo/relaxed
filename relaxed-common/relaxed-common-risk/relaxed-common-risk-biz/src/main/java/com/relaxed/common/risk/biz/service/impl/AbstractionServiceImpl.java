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
import com.relaxed.common.risk.model.converter.ModelConverter;
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
import org.springframework.util.Assert;

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

	private final EventDistributor eventDistributor;

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

	@Override
	public boolean add(Abstraction abstraction) {
		if (SqlHelper.retBool(baseMapper.insert(abstraction))) {
			eventDistributor.distribute(SubscribeEnum.PUB_SUB_ABSTRACTION_CHANNEL.getChannel(),
					JSONUtil.toJsonStr(AbstractionConverter.INSTANCE.poToVo(abstraction)));
			return true;
		}
		return false;
	}

	@Override
	public boolean edit(Abstraction abstraction) {
		Long abstractionId = abstraction.getId();
		Abstraction sqlAbstraction = baseMapper.selectById(abstractionId);
		Assert.notNull(sqlAbstraction, "abstraction can not exists.");
		if (SqlHelper.retBool(baseMapper.updateById(abstraction))) {
			eventDistributor.distribute(SubscribeEnum.PUB_SUB_ABSTRACTION_CHANNEL.getChannel(),
					JSONUtil.toJsonStr(AbstractionConverter.INSTANCE.poToVo(abstraction)));
			return true;
		}
		return false;
	}

	@Override
	public boolean del(Long id) {
		Abstraction sqlAbstraction = baseMapper.selectById(id);
		Assert.notNull(sqlAbstraction, "abstraction can not exists.");
		if (SqlHelper.retBool(baseMapper.deleteById(id))) {
			eventDistributor.distribute(SubscribeEnum.PUB_SUB_ABSTRACTION_CHANNEL.getChannel(),
					JSONUtil.toJsonStr(AbstractionConverter.INSTANCE.poToVo(sqlAbstraction)));
			return true;
		}
		return false;
	}

}
