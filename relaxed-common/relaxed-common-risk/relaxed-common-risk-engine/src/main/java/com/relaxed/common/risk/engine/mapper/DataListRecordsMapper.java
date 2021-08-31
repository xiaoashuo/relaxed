package com.relaxed.common.risk.engine.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.risk.engine.model.entity.DataListRecords;

import com.relaxed.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.131
 */
public interface DataListRecordsMapper extends ExtendMapper<DataListRecords> {

	/**
	 * 根据数据列表id 查询所有记录
	 * @author yakir
	 * @date 2021/8/31 16:44
	 * @param dataListId
	 * @return java.util.List<com.relaxed.common.risk.engine.model.entity.DataListRecords>
	 */
	default List<DataListRecords> listDataRecord(Long dataListId) {
		return this
				.selectList(Wrappers.lambdaQuery(DataListRecords.class).eq(DataListRecords::getDataListId, dataListId));
	}

}
