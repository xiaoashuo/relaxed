package com.relaxed.common.risk.biz.service;

import com.relaxed.common.risk.model.dto.PluginDTO;
import com.relaxed.common.risk.model.vo.PluginVO;
import com.relaxed.common.risk.model.entity.Plugin;
import com.relaxed.common.risk.model.qo.PluginQO;
import com.relaxed.extend.mybatis.plus.service.ExtendService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;

/**
 * <p>
 * 业务层
 * </p>
 *
 * @author Yakir
 * @since 2021-09-28T13:43:11.834
 */
public interface PluginService extends ExtendService<Plugin> {

	/**
	 * 分页查询
	 * @param pageParam {@link PageParam}
	 * @param pluginQO {@link PluginQO}
	 * @return {@link PageResult<PluginVO>}
	 */
	PageResult<PluginVO> selectByPage(PageParam pageParam, PluginQO pluginQO);

}