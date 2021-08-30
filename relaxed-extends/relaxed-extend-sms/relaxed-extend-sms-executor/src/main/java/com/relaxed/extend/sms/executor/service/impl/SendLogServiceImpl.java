package com.relaxed.extend.sms.executor.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;




import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.extend.mybatis.plus.toolkit.PageUtil;
import com.relaxed.extend.sms.executor.mapper.SendLogMapper;
import com.relaxed.extend.sms.executor.model.convert.SendLogConverter;
import com.relaxed.extend.sms.executor.model.entity.SendLog;
import com.relaxed.extend.sms.executor.model.qo.SendLogQO;
import com.relaxed.extend.sms.executor.model.vo.SendLogVO;
import com.relaxed.extend.sms.executor.service.SendLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 日志表 业务层实现
 * </p>
 *
 * @author Yakir
 * @since 2021-08-27T18:18:41.122
 */
@RequiredArgsConstructor
@Service
public class SendLogServiceImpl extends ExtendServiceImpl<SendLogMapper, SendLog> implements SendLogService {

    @Override
    public PageResult<SendLogVO> selectByPage(PageParam pageParam, SendLogQO sendLogQO) {
        IPage<SendLog> page = PageUtil.prodPage(pageParam);
        LambdaQueryWrapper<SendLog> wrapper = Wrappers.lambdaQuery(SendLog.class)
            .eq(ObjectUtil.isNotNull(sendLogQO.getId()),SendLog::getId, sendLogQO.getId());
        this.baseMapper.selectPage(page,wrapper);
        IPage<SendLogVO> voPage = page.convert(SendLogConverter.INSTANCE::poToVo);
        return new PageResult<>(voPage.getRecords(),voPage.getTotal());
    }

}
