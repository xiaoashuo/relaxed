package com.relaxed.common.risk.engine.rules.statistics.domain;

import com.relaxed.common.risk.model.enums.FieldType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Map;

/**
 * @author Yakir
 * @Topic AggregateBO
 * @Description
 * @date 2021/8/30 16:05
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class AggregateParamBO implements AggregateParam {

	/**
	 * 模型id
	 */
	private Long modelId;

	/**
	 * 查询字段名称
	 */
	private String searchFieldName;

	/**
	 * 查询字段值
	 */
	private Object searchFieldVal;

	/**
	 * 开始时间
	 */
	private Date beginDate;

	/**
	 * 日期指向字段名
	 */
	private String refDateFieldName;

	/**
	 * 指向日期字段值
	 */
	private Date refDateFieldVal;

	/**
	 * 函数字段名
	 */
	private String functionFieldName;

	/**
	 * 函数字段值
	 */
	private Object functionFieldVal;

	/**
	 * 函数字段类型
	 */
	private FieldType functionFieldType;

}
