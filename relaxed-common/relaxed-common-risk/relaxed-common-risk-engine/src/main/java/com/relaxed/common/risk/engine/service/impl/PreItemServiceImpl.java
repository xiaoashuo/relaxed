package com.relaxed.common.risk.engine.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.cache.annotation.Cached;
import com.relaxed.common.risk.engine.model.converter.PreItemConverter;
import com.relaxed.common.risk.engine.model.qo.PreItemQO;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;

import com.relaxed.common.risk.engine.mapper.PreItemMapper;
import com.relaxed.common.risk.engine.service.PreItemService;
import com.relaxed.common.risk.engine.model.dto.PreItemDTO;
import com.relaxed.common.risk.engine.model.entity.PreItem;
import com.relaxed.common.risk.engine.model.vo.PreItemVO;

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
	public List<PreItemVO> getByModelId(Long modelId) {
		List<PreItem> preItems = baseMapper.getByModelId(modelId);
		return preItems != null ? PreItemConverter.INSTANCE.poToVOs(preItems) : null;
	}

}
