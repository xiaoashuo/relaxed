package com.relaxed.common.risk.engine.service;

import com.relaxed.common.risk.engine.model.qo.MobileInfoQO;
import com.relaxed.common.risk.engine.model.vo.MobileInfoVO;
import com.relaxed.common.risk.engine.model.entity.MobileInfo;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;

/**
 * <p>
 * 业务层
 * </p>
 *
 * @author Yakir
 * @since 2021-09-01T13:49:40.174
 */
public interface MobileInfoService extends ExtendService<MobileInfo> {

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam}
	 * @param mobleInfoQO {@link MobileInfoQO}
	 * @return {@link PageResult< MobileInfoVO >}
	 */
	PageResult<MobileInfoVO> selectByPage(PageParam pageParam, MobileInfoQO mobleInfoQO);

	/**
	 * 查询一个条记录
	 * @author yakir
	 * @date 2021/9/1 14:16
	 * @param province
	 * @param city
	 * @return com.relaxed.common.risk.engine.model.vo.MobileInfoVO
	 */
	MobileInfoVO selectOneLimit1(String province, String city);

}