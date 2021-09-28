package com.relaxed.common.risk.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.risk.model.converter.PluginConverter;
import com.relaxed.common.risk.model.qo.PluginQO;
import com.relaxed.common.risk.repository.mapper.PluginMapper;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;

import com.relaxed.common.risk.repository.mapper.PluginMapper;
import com.relaxed.common.risk.biz.service.PluginService;
import com.relaxed.common.risk.model.dto.PluginDTO;
import com.relaxed.common.risk.model.entity.Plugin;
import com.relaxed.common.risk.model.vo.PluginVO;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.extend.mybatis.plus.toolkit.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务层实现
 * </p>
 *
 * @author Yakir
 * @since 2021-09-28T13:43:11.834
 */
@RequiredArgsConstructor
@Service
public class PluginServiceImpl extends ExtendServiceImpl<PluginMapper, Plugin> implements PluginService {

	@Override
	public PageResult<PluginVO> selectByPage(PageParam pageParam, PluginQO pluginQO) {
		IPage<Plugin> page = PageUtil.prodPage(pageParam);
		LambdaQueryWrapper<Plugin> wrapper = Wrappers.lambdaQuery(Plugin.class)
				.eq(ObjectUtil.isNotNull(pluginQO.getId()), Plugin::getId, pluginQO.getId());
		this.baseMapper.selectPage(page, wrapper);
		IPage<PluginVO> voPage = page.convert(PluginConverter.INSTANCE::poToVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

}
