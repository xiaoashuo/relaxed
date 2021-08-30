package com.relaxed.extend.sms.executor.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;



import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.extend.mybatis.plus.toolkit.PageUtil;
import com.relaxed.extend.sms.executor.mapper.TemplateMapper;
import com.relaxed.extend.sms.executor.model.convert.TemplateConverter;
import com.relaxed.extend.sms.executor.model.entity.Template;
import com.relaxed.extend.sms.executor.model.qo.TemplateQO;
import com.relaxed.extend.sms.executor.model.vo.TemplateVO;
import com.relaxed.extend.sms.executor.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 模板表 业务层实现
 * </p>
 *
 * @author Yakir
 * @since 2021-08-27T18:18:41.302
 */
@RequiredArgsConstructor
@Service
public class TemplateServiceImpl extends ExtendServiceImpl<TemplateMapper, Template> implements TemplateService {

    @Override
    public PageResult<TemplateVO> selectByPage(PageParam pageParam, TemplateQO templateQO) {
        IPage<Template> page = PageUtil.prodPage(pageParam);
        LambdaQueryWrapper<Template> wrapper = Wrappers.lambdaQuery(Template.class)
            .eq(ObjectUtil.isNotNull(templateQO.getId()),Template::getId, templateQO.getId());
        this.baseMapper.selectPage(page,wrapper);
        IPage<TemplateVO> voPage = page.convert(TemplateConverter.INSTANCE::poToVo);
        return new PageResult<>(voPage.getRecords(),voPage.getTotal());
    }

}
