package com.relaxed.extend.sms.executor.service;


import com.relaxed.extend.mybatis.plus.service.ExtendService;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.extend.sms.executor.model.entity.Signature;
import com.relaxed.extend.sms.executor.model.qo.SignatureQO;
import com.relaxed.extend.sms.executor.model.vo.SignatureVO;

/**
 * <p>
 * 签名表 业务层 
 * </p>
 *
 * @author Yakir
 * @since  2021-08-27T18:18:41.224
 */
public interface SignatureService extends ExtendService<Signature> {

    /**
     * 分页查询
     * @param pageParam {@link PageParam}
     * @param signatureQO {@link SignatureQO}
     * @return {@link PageResult<SignatureVO>}
     */
    PageResult<SignatureVO> selectByPage(PageParam pageParam, SignatureQO signatureQO);
        
}