package com.relaxed.common.risk.engine.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.risk.engine.model.converter.ModelConfParamConverter;
import com.relaxed.common.risk.engine.model.qo.ModelConfParamQO;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;

import com.relaxed.common.risk.engine.mapper.ModelConfParamMapper;
import com.relaxed.common.risk.engine.service.ModelConfParamService;
import com.relaxed.common.risk.engine.model.dto.ModelConfParamDTO;
import com.relaxed.common.risk.engine.model.entity.ModelConfParam;
import com.relaxed.common.risk.engine.model.vo.ModelConfParamVO;

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
 * @since 2021-08-31T09:58:08.892
 */
@RequiredArgsConstructor
@Service
public class ModelConfParamServiceImpl extends ExtendServiceImpl<ModelConfParamMapper, ModelConfParam>
		implements ModelConfParamService {

	@Override
	public PageResult<ModelConfParamVO> selectByPage(PageParam pageParam, ModelConfParamQO modelConfParamQO) {
		IPage<ModelConfParam> page = PageUtil.prodPage(pageParam);
		LambdaQueryWrapper<ModelConfParam> wrapper = Wrappers.lambdaQuery(ModelConfParam.class)
				.eq(ObjectUtil.isNotNull(modelConfParamQO.getId()), ModelConfParam::getId, modelConfParamQO.getId());
		this.baseMapper.selectPage(page, wrapper);
		IPage<ModelConfParamVO> voPage = page.convert(ModelConfParamConverter.INSTANCE::poToVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	@Override
	public List<ModelConfParamVO> listByModelConfId(Long modelConfId) {
		List<ModelConfParam> modelConfParamList = baseMapper.listByModelConfId(modelConfId);
		return ModelConfParamConverter.INSTANCE.poToVOs(modelConfParamList);
	}

}
