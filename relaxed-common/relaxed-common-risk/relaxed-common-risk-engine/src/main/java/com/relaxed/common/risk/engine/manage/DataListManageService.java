package com.relaxed.common.risk.engine.manage;

import java.util.Map;

/**
 * @author Yakir
 * @Topic DataListManageService
 * @Description
 * @date 2021/8/30 11:19
 * @Version 1.0
 */
public interface DataListManageService {

	/**
	 * 得到数据列表记录map
	 * @author yakir
	 * @date 2021/8/31 17:10
	 * @param modelId
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 */
	Map<String, Object> getDataListMap(Long modelId);

}
