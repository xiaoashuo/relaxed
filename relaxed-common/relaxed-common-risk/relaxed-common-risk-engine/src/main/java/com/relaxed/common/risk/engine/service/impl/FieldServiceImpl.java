package com.relaxed.common.risk.engine.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.cache.annotation.Cached;
import com.relaxed.common.risk.engine.model.converter.FieldConverter;
import com.relaxed.common.risk.engine.model.qo.FieldQO;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;

import com.relaxed.common.risk.engine.mapper.FieldMapper;
import com.relaxed.common.risk.engine.service.FieldService;
import com.relaxed.common.risk.engine.model.entity.Field;
import com.relaxed.common.risk.engine.model.vo.FieldVO;

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
 * @since 2021-08-29T12:14:38.328
 */
@RequiredArgsConstructor
@Service
public class FieldServiceImpl extends ExtendServiceImpl<FieldMapper, Field> implements FieldService {

	@Override
	public PageResult<FieldVO> selectByPage(PageParam pageParam, FieldQO fieldQO) {
		IPage<Field> page = PageUtil.prodPage(pageParam);
		LambdaQueryWrapper<Field> wrapper = Wrappers.lambdaQuery(Field.class).eq(ObjectUtil.isNotNull(fieldQO.getId()),
				Field::getId, fieldQO.getId());
		this.baseMapper.selectPage(page, wrapper);
		IPage<FieldVO> voPage = page.convert(FieldConverter.INSTANCE::poToVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	@Cached(prefix = "model-fields", key = "#modelId")
	@Override
	public List<FieldVO> listByModelId(Long modelId) {
		List<Field> list = baseMapper.listByModelId(modelId);
		return list != null ? FieldConverter.INSTANCE.poToVOs(list) : null;
	}

}
