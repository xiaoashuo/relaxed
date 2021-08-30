package com.relaxed.extend.sms.executor.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;




import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.extend.mybatis.plus.toolkit.PageUtil;
import com.relaxed.extend.sms.executor.mapper.ManualProcessMapper;
import com.relaxed.extend.sms.executor.model.convert.ManualProcessConverter;
import com.relaxed.extend.sms.executor.model.entity.ManualProcess;
import com.relaxed.extend.sms.executor.model.qo.ManualProcessQO;
import com.relaxed.extend.sms.executor.model.vo.ManualProcessVO;
import com.relaxed.extend.sms.executor.service.ManualProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 人工处理任务表 业务层实现
 * </p>
 *
 * @author Yakir
 * @since 2021-08-27T18:18:41.264
 */
@RequiredArgsConstructor
@Service
public class ManualProcessServiceImpl extends ExtendServiceImpl<ManualProcessMapper, ManualProcess> implements ManualProcessService {

    @Override
    public PageResult<ManualProcessVO> selectByPage(PageParam pageParam, ManualProcessQO manualProcessQO) {
        IPage<ManualProcess> page = PageUtil.prodPage(pageParam);
        LambdaQueryWrapper<ManualProcess> wrapper = Wrappers.lambdaQuery(ManualProcess.class)
            .eq(ObjectUtil.isNotNull(manualProcessQO.getId()),ManualProcess::getId, manualProcessQO.getId());
        this.baseMapper.selectPage(page,wrapper);
        IPage<ManualProcessVO> voPage = page.convert(ManualProcessConverter.INSTANCE::poToVo);
        return new PageResult<>(voPage.getRecords(),voPage.getTotal());
    }

}
