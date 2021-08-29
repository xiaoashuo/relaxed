package com.relaxed.common.risk.engine.service;

import com.relaxed.common.risk.engine.model.dto.DataListsDTO;
import com.relaxed.common.risk.engine.model.vo.DataListsVO;
import com.relaxed.common.risk.engine.model.entity.DataLists;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;

/**
 * <p>
 * 业务层
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.389
 */
public interface DataListsService extends ExtendService<DataLists> {

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam}
	 * @param dataListsQO {@link DataListsQO}
	 * @return {@link PageResult<DataListsVO>}
	 */
	PageResult<DataListsVO> selectByPage(PageParam pageParam, DataListsQO dataListsQO);

}