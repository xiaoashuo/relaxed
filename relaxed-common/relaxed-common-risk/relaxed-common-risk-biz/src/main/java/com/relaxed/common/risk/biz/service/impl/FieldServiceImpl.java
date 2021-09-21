package com.relaxed.common.risk.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.relaxed.common.cache.annotation.Cached;
import com.relaxed.common.core.exception.BusinessException;
import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageResult;
import com.relaxed.common.risk.biz.distributor.EventDistributor;
import com.relaxed.common.risk.biz.distributor.subscribe.SubscribeEnum;
import com.relaxed.common.risk.biz.exception.RiskCode;
import com.relaxed.common.risk.biz.exception.RiskException;
import com.relaxed.common.risk.model.converter.ModelConverter;
import com.relaxed.common.risk.model.entity.Model;
import com.relaxed.common.risk.repository.mapper.FieldMapper;
import com.relaxed.common.risk.biz.service.FieldService;
import com.relaxed.common.risk.model.converter.FieldConverter;
import com.relaxed.common.risk.model.entity.Field;
import com.relaxed.common.risk.model.qo.FieldQO;
import com.relaxed.common.risk.model.vo.FieldVO;
import com.relaxed.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.relaxed.extend.mybatis.plus.toolkit.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * <p>
 * 业务层实现
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T12:14:38.328
 */
@RequiredArgsConstructor
@Service
public class FieldServiceImpl extends ExtendServiceImpl<FieldMapper, Field> implements FieldService {
	private final EventDistributor eventDistributor;

	@Override
	public PageResult<FieldVO> selectByPage(PageParam pageParam, FieldQO fieldQO) {
		IPage<Field> page = PageUtil.prodPage(pageParam);
		LambdaQueryWrapper<Field> wrapper = Wrappers.lambdaQuery(Field.class).eq(ObjectUtil.isNotNull(fieldQO.getId()),
				Field::getId, fieldQO.getId());
		this.baseMapper.selectPage(page, wrapper);
		IPage<FieldVO> voPage = page.convert(FieldConverter.INSTANCE::poToVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	@Cached(prefix = "model-fields", key = "#modelId")
	@Override
	public List<FieldVO> listByModelId(Long modelId) {
		List<Field> list = baseMapper.listByModelId(modelId);
		return list != null ? FieldConverter.INSTANCE.poToVOs(list) : null;
	}

	@Override
	public boolean add(Field field) {
		Long modelId = field.getModelId();
		String fieldName = field.getFieldName();
		Field sqlField=baseMapper.selectOne(modelId,fieldName);
		Assert.isNull(sqlField,"model field name already exists.");
		if (SqlHelper.retBool(baseMapper.insert(field))){
			//发布订阅
			eventDistributor.distribute(SubscribeEnum.PUB_SUB_FIELD_CHANNEL.getChannel(), JSONUtil.toJsonStr(FieldConverter.INSTANCE.poToVo(field)));
			return true;
		}
		return false;
	}

	@Override
	public boolean edit(Field field) {
		Field sqlField=getById(field.getId());
		Assert.notNull(sqlField,"field can not exists.");
		if (SqlHelper.retBool(baseMapper.updateById(field))){
			eventDistributor.distribute(SubscribeEnum.PUB_SUB_FIELD_CHANNEL.getChannel(), JSONUtil.toJsonStr(FieldConverter.INSTANCE.poToVo(field)));
			return true;
		}
		return false;
	}



	@Override
	public boolean del(Model model, Field field) {
		String fieldName = field.getFieldName();
		if(model.getEntryName().equals(fieldName)
				||model.getReferenceDate().equals(fieldName)){
			 throw new RiskException(RiskCode.FIELD_NOT_ALLOWED_DEL);
		}
		if (SqlHelper.retBool(baseMapper.deleteById(field.getId()))){
			eventDistributor.distribute(SubscribeEnum.PUB_SUB_FIELD_CHANNEL.getChannel(), JSONUtil.toJsonStr(FieldConverter.INSTANCE.poToVo(field)));
			return true;
		}
		return false;
	}
}
