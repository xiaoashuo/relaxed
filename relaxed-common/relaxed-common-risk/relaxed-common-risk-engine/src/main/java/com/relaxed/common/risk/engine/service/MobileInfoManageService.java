package com.relaxed.common.risk.engine.service;

import com.relaxed.common.risk.model.vo.MobileInfoVO;

/**
 * @author Yakir
 * @Topic MobileInfoService
 * @Description
 * @date 2021/9/1 13:47
 * @Version 1.0
 */
public interface MobileInfoManageService {

	/**
	 * 获取手机信息
	 * @author yakir
	 * @date 2021/9/1 13:48
	 * @param mobile
	 * @return com.relaxed.common.risk.model.vo.MobileInfoVO
	 */
	MobileInfoVO getByMobile(String mobile);

}
