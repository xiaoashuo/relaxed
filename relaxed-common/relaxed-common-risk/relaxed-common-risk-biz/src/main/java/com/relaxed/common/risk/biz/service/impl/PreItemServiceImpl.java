package com.relaxed.common.risk.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.relaxed.common.cache.annotation.CacheDel;
import com.relaxed.common.cache.annotation.Cached;
import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.biz.distributor.EventDistributor;
import com.relaxed.common.risk.biz.distributor.subscribe.SubscribeEnum;
import com.relaxed.common.risk.repository.mapper.PreItemMapper;
import com.relaxed.common.risk.biz.service.PreItemService;
import com.relaxed.common.risk.model.converter.PreItemConverter;
import com.relaxed.common.risk.model.entity.PreItem;
import com.relaxed.common.risk.model.qo.PreItemQO;
import com.relaxed.common.risk.model.vo.PreItemVO;
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
 * @since 2021-08-29T13:57:50.664
 */
@RequiredArgsConstructor
@Service
public class PreItemServiceImpl extends ExtendServiceImpl<PreItemMapper, PreItem> implements PreItemService {

	private final EventDistributor eventDistributor;

	@Override
	public PageResult<PreItemVO> selectByPage(PageParam pageParam, PreItemQO preItemQO) {
		IPage<PreItem> page = PageUtil.prodPage(pageParam);
		LambdaQueryWrapper<PreItem> wrapper = Wrappers.lambdaQuery(PreItem.class)
				.eq(ObjectUtil.isNotNull(preItemQO.getId()), PreItem::getId, preItemQO.getId());
		this.baseMapper.selectPage(page, wrapper);
		IPage<PreItemVO> voPage = page.convert(PreItemConverter.INSTANCE::poToVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	@Cached(prefix = "model-pre-items", key = "#modelId")
	@Override
	public List<PreItemVO> listByModelId(Long modelId) {
		List<PreItem> preItems = baseMapper.getByModelId(modelId);
		return preItems != null ? PreItemConverter.INSTANCE.poToVOs(preItems) : null;
	}

	@CacheDel(prefix = "model-pre-items", key = "#preItem.modelId")
	@Override
	public boolean add(PreItem preItem) {
		PreItem sqlPreItem = baseMapper.selectOne(preItem.getModelId(), preItem.getDestField());
		// 预处理字段已存在 则抛出异常
		Assert.isNull(sqlPreItem, "pre item has already exists.");
		// 保存预处理字段
		if (SqlHelper.retBool(baseMapper.insert(preItem))) {
			eventDistributor.distribute(SubscribeEnum.PUB_SUB_PRE_ITEM_CHANNEL.getChannel(),
					JSONUtil.toJsonStr(PreItemConverter.INSTANCE.poToVo(preItem)));
			return true;
		}
		return false;
	}

	@CacheDel(prefix = "model-pre-items", key = "#modelId")
	@Override
	public boolean del(Long modelId, Long id) {
		PreItem preItem = baseMapper.selectOne(modelId, id);
		Assert.notNull(preItem, "pre item not exists.");
		if (SqlHelper.retBool(baseMapper.deleteById(id))) {
			eventDistributor.distribute(SubscribeEnum.PUB_SUB_PRE_ITEM_CHANNEL.getChannel(),
					JSONUtil.toJsonStr(PreItemConverter.INSTANCE.poToVo(preItem)));
			return true;
		}
		return false;
	}

	@CacheDel(prefix = "model-pre-items", key = "#preItem.")
	@Override
	public boolean edit(PreItem preItem) {
		PreItem sqlPreItem = baseMapper.selectOne(preItem.getModelId());
		Assert.notNull(sqlPreItem, "pre item can not be null.");
		preItem.setId(sqlPreItem.getId());
		if (updateById(preItem)) {
			eventDistributor.distribute(SubscribeEnum.PUB_SUB_PRE_ITEM_CHANNEL.getChannel(),
					JSONUtil.toJsonStr(PreItemConverter.INSTANCE.poToVo(preItem)));
			return true;
		}
		return false;
	}

}
