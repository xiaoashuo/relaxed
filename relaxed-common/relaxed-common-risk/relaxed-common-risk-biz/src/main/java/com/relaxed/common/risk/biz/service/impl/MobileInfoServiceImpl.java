package com.relaxed.common.risk.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.repository.mapper.MobileInfoMapper;
import com.relaxed.common.risk.biz.service.MobileInfoService;
import com.relaxed.common.risk.model.converter.MobileInfoConverter;
import com.relaxed.common.risk.model.entity.MobileInfo;
import com.relaxed.common.risk.model.qo.MobileInfoQO;
import com.relaxed.common.risk.model.vo.MobileInfoVO;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.relaxed.extend.mybatis.plus.toolkit.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务层实现
 * </p>
 *
 * @author Yakir
 * @since 2021-09-01T13:49:40.174
 */
@RequiredArgsConstructor
@Service
public class MobileInfoServiceImpl extends ExtendServiceImpl<MobileInfoMapper, MobileInfo>
		implements MobileInfoService {

	@Override
	public PageResult<MobileInfoVO> selectByPage(PageParam pageParam, MobileInfoQO mobleInfoQO) {
		IPage<MobileInfo> page = PageUtil.prodPage(pageParam);
		LambdaQueryWrapper<MobileInfo> wrapper = Wrappers.lambdaQuery(MobileInfo.class)
				.eq(ObjectUtil.isNotNull(mobleInfoQO.getId()), MobileInfo::getId, mobleInfoQO.getId());
		this.baseMapper.selectPage(page, wrapper);
		IPage<MobileInfoVO> voPage = page.convert(MobileInfoConverter.INSTANCE::poToVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	@Override
	public MobileInfoVO selectOneLimit1(String province, String city) {
		MobileInfo mobileInfo = baseMapper.selectOne(Wrappers.lambdaQuery(MobileInfo.class)
				.eq(MobileInfo::getProvince, province).eq(MobileInfo::getCity, city).last("limit 1"));
		return MobileInfoConverter.INSTANCE.poToVo(mobileInfo);
	}

}
