package com.relaxed.common.risk.engine.rules.statistics.domain;

import com.relaxed.common.risk.model.entity.FieldMeta;
import com.relaxed.common.risk.model.enums.FieldType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Yakir
 * @Topic AggregateBO
 * @Description
 * @date 2021/8/30 16:05
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class AggregateParamBO implements IAggregateParam {

	/**
	 * 模型id
	 */
	private Long modelId;

	/**
	 * 范围的开始时间 -> 指向日期字段元数据 组合成一个区间
	 */
	private Date beginDate;

	/**
	 * 查询字段元数据
	 */
	private FieldMeta<Object> searchFieldMeta;

	/**
	 * 指向日期字段元数据
	 */
	private FieldMeta<Date> refDateFieldMeta;

	/**
	 * 函数字段元数据
	 */
	private FieldMeta<Object> functionFieldMeta;

}
