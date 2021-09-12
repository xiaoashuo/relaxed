package com.relaxed.common.risk.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.repository.mapper.DataListRecordsMapper;
import com.relaxed.common.risk.biz.service.DataListRecordsService;
import com.relaxed.common.risk.model.converter.DataListRecordsConverter;
import com.relaxed.common.risk.model.entity.DataListRecords;
import com.relaxed.common.risk.model.qo.DataListRecordsQO;
import com.relaxed.common.risk.model.vo.DataListRecordsVO;
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
 * @since 2021-08-29T18:48:19.131
 */
@RequiredArgsConstructor
@Service
public class DataListRecordsServiceImpl extends ExtendServiceImpl<DataListRecordsMapper, DataListRecords>
		implements DataListRecordsService {

	@Override
	public PageResult<DataListRecordsVO> selectByPage(PageParam pageParam, DataListRecordsQO dataListRecordsQO) {
		IPage<DataListRecords> page = PageUtil.prodPage(pageParam);
		LambdaQueryWrapper<DataListRecords> wrapper = Wrappers.lambdaQuery(DataListRecords.class)
				.eq(ObjectUtil.isNotNull(dataListRecordsQO.getId()), DataListRecords::getId, dataListRecordsQO.getId());
		this.baseMapper.selectPage(page, wrapper);
		IPage<DataListRecordsVO> voPage = page.convert(DataListRecordsConverter.INSTANCE::poToVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	@Override
	public List<DataListRecordsVO> listDataRecord(Long dataListId) {
		List<DataListRecords> dataListRecords = baseMapper.listDataRecord(dataListId);
		return DataListRecordsConverter.INSTANCE.poToVOs(dataListRecords);
	}

}
