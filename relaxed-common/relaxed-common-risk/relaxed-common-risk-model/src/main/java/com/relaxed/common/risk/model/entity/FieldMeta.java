package com.relaxed.common.risk.model.entity;

import com.relaxed.common.risk.model.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yakir
 * @Topic FieldMeta
 * @Description 字段信息
 * @date 2021/9/23 14:26
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldMeta<T> {

	/**
	 * 字段名称
	 */
	private String fieldName;

	/**
	 * 字段值
	 */
	private T fieldValue;

	/**
	 * 字段类型
	 */
	private FieldType fieldType;

}
