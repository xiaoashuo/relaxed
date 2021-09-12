package com.relaxed.common.risk.biz.service;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.model.entity.DataListMeta;
import com.relaxed.common.risk.model.qo.DataListMetaQO;
import com.relaxed.common.risk.model.vo.DataListMetaVO;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

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