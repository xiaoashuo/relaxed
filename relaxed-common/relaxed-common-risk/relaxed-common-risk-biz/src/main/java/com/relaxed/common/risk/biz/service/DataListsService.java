package com.relaxed.common.risk.biz.service;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.model.entity.DataLists;
import com.relaxed.common.risk.model.qo.DataListsQO;
import com.relaxed.common.risk.model.vo.DataListsVO;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

import java.util.List;

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

	/**
	 * 根据modelIds 查询黑白名单列表
	 * @author yakir
	 * @date 2021/8/31 15:52
	 * @param modelIds
	 * @param status
	 * @return java.util.List<com.relaxed.common.risk.model.vo.DataListsVO>
	 */
	List<DataListsVO> list(List<Long> modelIds, Integer status);

	/**
	 * 根据id查询 数据列表
	 * @author yakir
	 * @date 2021/8/31 16:31
	 * @param modelId
	 * @param status
	 * @return java.util.List<com.relaxed.common.risk.model.vo.DataListsVO>
	 */
	List<DataListsVO> list(Long modelId, Integer status);

	/**
	 * 增加黑白名单数据列表
	 * @author yakir
	 * @date 2021/9/12 18:13
	 * @param dataLists
	 * @return boolean
	 */
	boolean add(DataLists dataLists);

	/**
	 * 删除黑白名单数据列表 根据id
	 * @author yakir
	 * @date 2021/9/12 18:17
	 * @param id
	 * @return boolean
	 */
	boolean del(Long id);

	/**
	 * 编辑数据列表
	 * @author yakir
	 * @date 2021/9/12 18:19
	 * @param dataLists
	 * @return boolean
	 */
	boolean edit(DataLists dataLists);

}