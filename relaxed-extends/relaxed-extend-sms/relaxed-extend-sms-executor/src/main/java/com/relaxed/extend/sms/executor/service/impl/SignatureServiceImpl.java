package com.relaxed.extend.sms.executor.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;




import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.extend.mybatis.plus.toolkit.PageUtil;
import com.relaxed.extend.sms.executor.mapper.SignatureMapper;
import com.relaxed.extend.sms.executor.model.convert.SignatureConverter;
import com.relaxed.extend.sms.executor.model.entity.Signature;
import com.relaxed.extend.sms.executor.model.qo.SignatureQO;
import com.relaxed.extend.sms.executor.model.vo.SignatureVO;
import com.relaxed.extend.sms.executor.service.SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 签名表 业务层实现
 * </p>
 *
 * @author Yakir
 * @since 2021-08-27T18:18:41.224
 */
@RequiredArgsConstructor
@Service
public class SignatureServiceImpl extends ExtendServiceImpl<SignatureMapper, Signature> implements SignatureService {

    @Override
    public PageResult<SignatureVO> selectByPage(PageParam pageParam, SignatureQO signatureQO) {
        IPage<Signature> page = PageUtil.prodPage(pageParam);
        LambdaQueryWrapper<Signature> wrapper = Wrappers.lambdaQuery(Signature.class)
            .eq(ObjectUtil.isNotNull(signatureQO.getId()),Signature::getId, signatureQO.getId());
        this.baseMapper.selectPage(page,wrapper);
        IPage<SignatureVO> voPage = page.convert(SignatureConverter.INSTANCE::poToVo);
        return new PageResult<>(voPage.getRecords(),voPage.getTotal());
    }

}
