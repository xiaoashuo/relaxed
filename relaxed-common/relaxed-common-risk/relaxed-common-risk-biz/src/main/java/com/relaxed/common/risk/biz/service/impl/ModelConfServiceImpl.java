package com.relaxed.common.risk.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.repository.mapper.ModelConfMapper;
import com.relaxed.common.risk.biz.service.ModelConfService;
import com.relaxed.common.risk.model.converter.ModelConfConverter;
import com.relaxed.common.risk.model.entity.ModelConf;
import com.relaxed.common.risk.model.qo.ModelConfQO;
import com.relaxed.common.risk.model.vo.ModelConfVO;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.relaxed.extend.mybatis.plus.toolkit.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务层实现
 * </p>
 *
 * @author Yakir
 * @since 2021-08-31T09:58:08.656
 */
@RequiredArgsConstructor
@Service
public class ModelConfServiceImpl extends ExtendServiceImpl<ModelConfMapper, ModelConf> implements ModelConfService {

	@Override
	public PageResult<ModelConfVO> selectByPage(PageParam pageParam, ModelConfQO modelConfQO) {
		IPage<ModelConf> page = PageUtil.prodPage(pageParam);
		LambdaQueryWrapper<ModelConf> wrapper = Wrappers.lambdaQuery(ModelConf.class)
				.eq(ObjectUtil.isNotNull(modelConfQO.getId()), ModelConf::getId, modelConfQO.getId());
		this.baseMapper.selectPage(page, wrapper);
		IPage<ModelConfVO> voPage = page.convert(ModelConfConverter.INSTANCE::poToVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	@Override
	public ModelConfVO getByModelId(Long modelId) {
		ModelConf modelConf = baseMapper.getByModelId(modelId);
		return ModelConfConverter.INSTANCE.poToVo(modelConf);
	}

}
