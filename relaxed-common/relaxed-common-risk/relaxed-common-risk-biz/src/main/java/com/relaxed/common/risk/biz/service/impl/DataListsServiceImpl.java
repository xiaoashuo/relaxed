package com.relaxed.common.risk.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;

import com.relaxed.common.risk.biz.service.DataListsService;
import com.relaxed.common.risk.model.converter.DataListsConverter;
import com.relaxed.common.risk.model.entity.DataLists;
import com.relaxed.common.risk.model.qo.DataListsQO;
import com.relaxed.common.risk.model.vo.DataListsVO;
import com.relaxed.common.risk.repository.mapper.DataListsMapper;
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
 * @since 2021-08-29T18:48:19.389
 */
@RequiredArgsConstructor
@Service
public class DataListsServiceImpl extends ExtendServiceImpl<DataListsMapper, DataLists> implements DataListsService {

	@Override
	public PageResult<DataListsVO> selectByPage(PageParam pageParam, DataListsQO dataListsQO) {
		IPage<DataLists> page = PageUtil.prodPage(pageParam);
		LambdaQueryWrapper<DataLists> wrapper = Wrappers.lambdaQuery(DataLists.class)
				.eq(ObjectUtil.isNotNull(dataListsQO.getId()), DataLists::getId, dataListsQO.getId());
		this.baseMapper.selectPage(page, wrapper);
		IPage<DataListsVO> voPage = page.convert(DataListsConverter.INSTANCE::poToVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	@Override
	public List<DataListsVO> list(List<Long> modelIds, Integer status) {
		List<DataLists> list = baseMapper.list(modelIds, status);
		return DataListsConverter.INSTANCE.poToVOs(list);
	}

	@Override
	public List<DataListsVO> list(Long modelId, Integer status) {
		List<DataLists> list = baseMapper.list(modelId, status);
		return DataListsConverter.INSTANCE.poToVOs(list);
	}

}
