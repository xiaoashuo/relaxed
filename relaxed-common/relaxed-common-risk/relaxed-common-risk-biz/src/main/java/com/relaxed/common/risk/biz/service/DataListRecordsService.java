package com.relaxed.common.risk.biz.service;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.model.entity.DataListRecords;
import com.relaxed.common.risk.model.qo.DataListRecordsQO;
import com.relaxed.common.risk.model.vo.DataListRecordsVO;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

import java.util.List;

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

	/**
	 * 查询数据列表记录
	 * @author yakir
	 * @date 2021/8/31 16:43
	 * @param dataListid
	 * @return java.util.List<com.relaxed.common.risk.model.vo.DataListRecordsVO>
	 */
	List<DataListRecordsVO> listDataRecord(Long dataListid);

	/**
	 * 添加记录内容
	 * @author yakir
	 * @date 2021/9/21 18:04
	 * @param dataListRecords
	 * @return boolean
	 */
	boolean add(DataListRecords dataListRecords);

	/**
	 * 编辑记录内容
	 * @author yakir
	 * @date 2021/9/21 18:04
	 * @param dataListRecords
	 * @return boolean
	 */
	boolean edit(DataListRecords dataListRecords);

	/**
	 * 删除记录内容
	 * @author yakir
	 * @date 2021/9/21 18:05
	 * @param id
	 * @return boolean
	 */
	boolean del(Long id);

}