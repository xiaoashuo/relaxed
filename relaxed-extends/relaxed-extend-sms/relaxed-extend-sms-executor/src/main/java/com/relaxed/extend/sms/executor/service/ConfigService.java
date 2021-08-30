package com.relaxed.extend.sms.executor.service;


import com.relaxed.extend.mybatis.plus.service.ExtendService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.extend.sms.executor.model.entity.Config;
import com.relaxed.extend.sms.executor.model.qo.ConfigQO;
import com.relaxed.extend.sms.executor.model.vo.ConfigVO;

import java.util.List;

/**
 * <p>
 * 配置表 业务层 
 * </p>
 *
 * @author Yakir
 * @since  2021-08-27T14:55:25.330
 */
public interface ConfigService extends ExtendService<Config> {

    /**
     * 分页查询
     * @param pageParam {@link PageParam}
     * @param configQO {@link ConfigQO}
     * @return {@link PageResult<ConfigVO>}
     */
    PageResult<ConfigVO> selectByPage(PageParam pageParam, ConfigQO configQO);
    /**
     * 渠道信息配置列表
     * @author yakir
     * @date 2021/8/27 15:11
     * @return java.util.List<com.relaxed.extend.sms.executor.model.entity.Config>
     */
    List<Config> listChannel();
        
}