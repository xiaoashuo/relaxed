package com.relaxed.common.risk.biz.service;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.model.entity.Model;
import com.relaxed.common.risk.model.qo.ModelQO;
import com.relaxed.common.risk.model.vo.ModelVO;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * @author Yakir
 * @Topic ModelService
 * @Description
 * @date 2021/8/29 8:50
 * @Version 1.0
 */
public interface ModelService extends ExtendService<Model> {

	/**
	 * 查询列表
	 * @author yakir
	 * @date 2021/8/29 9:10
	 * @param pageParam
	 * @param modelQO
	 * @return com.relaxed.common.model.domain.PageResult<com.relaxed.common.risk.model.vo.ModelVO>
	 */
	PageResult<ModelVO> selectByPage(PageParam pageParam, ModelQO modelQO);

	/**
	 * 根据状态查询列表
	 * @author yakir
	 * @date 2021/8/29 10:14
	 * @param status
	 * @return java.util.List<com.relaxed.common.risk.model.vo.ModelVO>
	 */
	List<ModelVO> listByStatus(Integer status);

	/**
	 * 根据guid查询model
	 * @author yakir
	 * @date 2021/8/29 10:52
	 * @param guid
	 * @return com.relaxed.common.risk.model.vo.ModelVO
	 */
	ModelVO getByGuid(String guid);

	/**
	 * 保存model
	 * @author yakir
	 * @date 2021/9/12 16:59
	 * @param model
	 * @return boolean
	 */
	boolean add(Model model);
	/**
	 * 根据模型名称查询模型
	 * @author yakir
	 * @date 2021/9/12 17:04
	 * @param modelName
	 * @return com.relaxed.common.risk.model.entity.Model
	 */
    Model getByModelName(String modelName);

    /**
     * 删除模型
     * @author yakir
     * @date 2021/9/12 17:18
     * @param id
     * @return boolean
     */
    boolean del(Long id);
}
