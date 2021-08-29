package com.relaxed.common.risk.engine.service;

import com.relaxed.common.risk.engine.model.dto.DataListRecordsDTO;
import com.relaxed.common.risk.engine.model.qo.DataListRecordsQO;
import com.relaxed.common.risk.engine.model.vo.DataListRecordsVO;
import com.relaxed.common.risk.engine.model.entity.DataListRecords;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;

/**
 * <p>
 * 业务层
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.131
 */
public interface DataListRecordsService extends ExtendService<DataListRecords> {

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam}
	 * @param dataListRecordsQO {@link DataListRecordsQO}
	 * @return {@link PageResult<DataListRecordsVO>}
	 */
	PageResult<DataListRecordsVO> selectByPage(PageParam pageParam, DataListRecordsQO dataListRecordsQO);

}