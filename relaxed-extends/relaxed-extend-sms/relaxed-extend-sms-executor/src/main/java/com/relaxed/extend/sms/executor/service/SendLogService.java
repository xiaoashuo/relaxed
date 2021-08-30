package com.relaxed.extend.sms.executor.service;

import com.relaxed.extend.mybatis.plus.service.ExtendService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.extend.sms.executor.model.entity.SendLog;
import com.relaxed.extend.sms.executor.model.qo.SendLogQO;
import com.relaxed.extend.sms.executor.model.vo.SendLogVO;

/**
 * <p>
 * 日志表 业务层 
 * </p>
 *
 * @author Yakir
 * @since  2021-08-27T18:18:41.122
 */
public interface SendLogService extends ExtendService<SendLog> {

    /**
     * 分页查询
     * @param pageParam {@link PageParam}
     * @param sendLogQO {@link SendLogQO}
     * @return {@link PageResult<SendLogVO>}
     */
    PageResult<SendLogVO> selectByPage(PageParam pageParam, SendLogQO sendLogQO);
        
}