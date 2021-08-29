package com.relaxed.common.risk.engine.service;

import com.relaxed.common.risk.engine.model.dto.DataListMetaDTO;
import com.relaxed.common.risk.engine.model.vo.DataListMetaVO;
import com.relaxed.common.risk.engine.model.entity.DataListMeta;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;

/**
 * <p>
 * 业务层
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.341
 */
public interface DataListMetaService extends ExtendService<DataListMeta> {

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam}
	 * @param dataListMetaQO {@link DataListMetaQO}
	 * @return {@link PageResult<DataListMetaVO>}
	 */
	PageResult<DataListMetaVO> selectByPage(PageParam pageParam, DataListMetaQO dataListMetaQO);

}