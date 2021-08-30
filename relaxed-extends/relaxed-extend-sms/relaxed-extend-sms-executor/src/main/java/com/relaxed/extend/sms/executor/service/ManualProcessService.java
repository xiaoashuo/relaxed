package com.relaxed.extend.sms.executor.service;


import com.relaxed.extend.mybatis.plus.service.ExtendService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.extend.sms.executor.model.entity.ManualProcess;
import com.relaxed.extend.sms.executor.model.qo.ManualProcessQO;
import com.relaxed.extend.sms.executor.model.vo.ManualProcessVO;

/**
 * <p>
 * 人工处理任务表 业务层 
 * </p>
 *
 * @author Yakir
 * @since  2021-08-27T18:18:41.264
 */
public interface ManualProcessService extends ExtendService<ManualProcess> {

    /**
     * 分页查询
     * @param pageParam {@link PageParam}
     * @param manualProcessQO {@link ManualProcessQO}
     * @return {@link PageResult<ManualProcessVO>}
     */
    PageResult<ManualProcessVO> selectByPage(PageParam pageParam, ManualProcessQO manualProcessQO);
        
}