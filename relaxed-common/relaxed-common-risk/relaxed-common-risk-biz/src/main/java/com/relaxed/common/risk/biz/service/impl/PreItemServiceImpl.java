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

	@Override
	public PreItem getOne(Long modelId, String destField) {
		return baseMapper.selectOne(modelId, destField);
	}

	@Override
	public PreItem getOne(Long modelId, Long preItemId) {
		return baseMapper.selectOne(modelId, preItemId);
	}

}
