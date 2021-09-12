package com.relaxed.common.risk.biz.service;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.model.entity.MobileInfo;
import com.relaxed.common.risk.model.qo.MobileInfoQO;
import com.relaxed.common.risk.model.vo.MobileInfoVO;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

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
	 * @return com.relaxed.common.risk.model.vo.MobileInfoVO
	 */
	MobileInfoVO selectOneLimit1(String province, String city);

}