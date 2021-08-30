package com.relaxed.common.risk.engine.rules.extractor;

import com.relaxed.common.risk.engine.enums.FieldType;
import com.relaxed.common.risk.engine.model.vo.FieldVO;

import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic FieldExtractor
 * @Description
 * @date 2021/8/30 13:35
 * @Version 1.0
 */
public interface FieldExtractor {

	/**
	 * 字段名提取器 eg: fields.userId -> userId
	 * @author yakir
	 * @date 2021/8/30 13:36
	 * @param originFieldName
	 * @return java.lang.String 转换后字段名
	 */
	String extractorFieldName(String originFieldName);

	/**
	 * 字段值提取器
	 * @author yakir
	 * @date 2021/8/30 14:11
	 * @param fieldName
	 * @param maps
	 * @return T
	 */
	<T> T extractorFieldValue(String fieldName, Map... maps);

	/**
	 * 提取字段类型
	 * @author yakir
	 * @date 2021/8/30 14:24
	 * @param fieldName
	 * @return com.relaxed.common.risk.engine.enums.FieldType
	 */
	FieldType extractorFieldType(String fieldName, List<FieldVO> fieldVOS);

}
