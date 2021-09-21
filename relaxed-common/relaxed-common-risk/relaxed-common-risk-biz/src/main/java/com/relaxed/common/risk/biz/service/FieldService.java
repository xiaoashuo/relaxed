package com.relaxed.common.risk.biz.service;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.model.entity.Field;
import com.relaxed.common.risk.model.entity.Model;
import com.relaxed.common.risk.model.qo.FieldQO;
import com.relaxed.common.risk.model.vo.FieldVO;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * <p>
 * 业务层
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T12:14:38.328
 */
public interface FieldService extends ExtendService<Field> {

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam}
	 * @param fieldQO {@link FieldQO}
	 * @return {@link PageResult<FieldVO>}
	 */
	PageResult<FieldVO> selectByPage(PageParam pageParam, FieldQO fieldQO);

	/**
	 * 根据modelId查询字段列表
	 * @author yakir
	 * @date 2021/8/29 12:52
	 * @param modelId
	 * @return java.util.List<com.relaxed.common.risk.model.entity.Field>
	 */
	List<FieldVO> listByModelId(Long modelId);

	/**
	 * 添加字段
	 * @author yakir
	 * @date 2021/9/19 17:22
	 * @param field
	 * @return boolean
	 */
    boolean add(Field field);

    /**
     * 编辑字段
     * @author yakir
     * @date 2021/9/19 17:28
     * @param field
     * @return boolean
     */
	boolean edit(Field field);


	/**
	 * 删除字段
	 * @author yakir
	 * @date 2021/9/19 17:34
	 * @param model
	 * @param field
	 * @return boolean
	 */
	boolean del(Model model, Field field);
}