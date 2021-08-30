package com.relaxed.extend.sms.executor.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.extend.cache.CacheManage;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;




import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.extend.mybatis.plus.toolkit.PageUtil;
import com.relaxed.extend.sms.executor.config.SmsConstant;
import com.relaxed.extend.sms.executor.mapper.ConfigMapper;
import com.relaxed.extend.sms.executor.model.convert.ConfigConverter;
import com.relaxed.extend.sms.executor.model.entity.Config;
import com.relaxed.extend.sms.executor.model.qo.ConfigQO;
import com.relaxed.extend.sms.executor.model.vo.ConfigVO;
import com.relaxed.extend.sms.executor.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 配置表 业务层实现
 * </p>
 *
 * @author Yakir
 * @since 2021-08-27T14:55:25.330
 */
@RequiredArgsConstructor
@Service
public class ConfigServiceImpl extends ExtendServiceImpl<ConfigMapper, Config> implements ConfigService {


    private final CacheManage cacheManage;
    @Override
    public PageResult<ConfigVO> selectByPage(PageParam pageParam, ConfigQO configQO) {
        IPage<Config> page = PageUtil.prodPage(pageParam);
        LambdaQueryWrapper<Config> wrapper = Wrappers.lambdaQuery(Config.class)
            .eq(ObjectUtil.isNotNull(configQO.getId()),Config::getId, configQO.getId());
        this.baseMapper.selectPage(page,wrapper);
        IPage<ConfigVO> voPage = page.convert(ConfigConverter.INSTANCE::poToVo);
        return new PageResult<>(voPage.getRecords(),voPage.getTotal());
    }


    @Override
    public List<Config> listChannel() {
        StringRedisTemplate stringRedisTemplate= cacheManage.getOperator();
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String channelList = valueOperations.get(SmsConstant.CHANNEL_LIST);
        if (StrUtil.isNotEmpty(channelList)){
            return JSONUtil.toBean(channelList,List.class);
        }
        //若redis 没有 则查询数据库
        LambdaQueryWrapper<Config> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Config::getChannelType, 1);
        wrapper.eq(Config::getIsActive, 1);
        wrapper.eq(Config::getIsEnable, 1);
        wrapper.orderByAsc(Config::getLevel);
        List<Config> configs = this.getBaseMapper().selectList(wrapper);
        if (CollectionUtil.isNotEmpty(configs)){
            valueOperations.set(SmsConstant.CHANNEL_LIST,JSONUtil.toJsonStr(configs),60, TimeUnit.SECONDS);
        }
        return configs;
    }
}
