package com.relaxed.extend.sms.executor.service;

import com.relaxed.extend.mybatis.plus.service.ExtendService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.extend.sms.executor.model.entity.Template;
import com.relaxed.extend.sms.executor.model.qo.TemplateQO;
import com.relaxed.extend.sms.executor.model.vo.TemplateVO;

/**
 * <p>
 * 模板表 业务层 
 * </p>
 *
 * @author Yakir
 * @since  2021-08-27T18:18:41.302
 */
public interface TemplateService extends ExtendService<Template> {

    /**
     * 分页查询
     * @param pageParam {@link PageParam}
     * @param templateQO {@link TemplateQO}
     * @return {@link PageResult<TemplateVO>}
     */
    PageResult<TemplateVO> selectByPage(PageParam pageParam, TemplateQO templateQO);
        
}