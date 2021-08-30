package com.relaxed.common.risk.engine.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.risk.engine.model.converter.DataListMetaConverter;
import com.relaxed.common.risk.engine.model.qo.DataListMetaQO;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;

import com.relaxed.common.risk.engine.mapper.DataListMetaMapper;
import com.relaxed.common.risk.engine.service.DataListMetaService;
import com.relaxed.common.risk.engine.model.dto.DataListMetaDTO;
import com.relaxed.common.risk.engine.model.entity.DataListMeta;
import com.relaxed.common.risk.engine.model.vo.DataListMetaVO;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.extend.mybatis.plus.toolkit.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务层实现
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.341
 */
@RequiredArgsConstructor
@Service
public class DataListMetaServiceImpl extends ExtendServiceImpl<DataListMetaMapper, DataListMeta>
		implements DataListMetaService {

	@Override
	public PageResult<DataListMetaVO> selectByPage(PageParam pageParam, DataListMetaQO dataListMetaQO) {
		IPage<DataListMeta> page = PageUtil.prodPage(pageParam);
		LambdaQueryWrapper<DataListMeta> wrapper = Wrappers.lambdaQuery(DataListMeta.class)
				.eq(ObjectUtil.isNotNull(dataListMetaQO.getId()), DataListMeta::getId, dataListMetaQO.getId());
		this.baseMapper.selectPage(page, wrapper);
		IPage<DataListMetaVO> voPage = page.convert(DataListMetaConverter.INSTANCE::poToVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

}
