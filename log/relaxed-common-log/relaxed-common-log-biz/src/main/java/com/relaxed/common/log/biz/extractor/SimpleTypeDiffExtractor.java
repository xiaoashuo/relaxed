package com.relaxed.common.log.biz.extractor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.biz.annotation.LogDiffTag;
import com.relaxed.common.log.biz.enums.AttrOptionEnum;
import com.relaxed.common.log.biz.model.AttributeChange;

import java.lang.reflect.Field;

/**
 * 简单类型差异提取器，用于处理基本数据类型和简单对象的差异比较。 该实现类通过比较字段的旧值和新值，生成包含操作类型、属性名、路径和值的变更记录。
 * 主要用于处理基本类型（如String、Integer等）的属性变更记录。
 *
 * @author Yakir
 * @since 1.0.0
 */
public class SimpleTypeDiffExtractor implements DiffExtractor {

	/**
	 * 提取并转换简单类型字段的差异值
	 * @param field 字段对象
	 * @param logDiffTag 差异标签注解
	 * @param oldFieldValue 字段的旧值
	 * @param newFieldValue 字段的新值
	 * @return 属性变更的 JSON 字符串表示，如果值未发生变化则返回空字符串
	 */
	@Override
	public String diffValue(Field field, LogDiffTag logDiffTag, Object oldFieldValue, Object newFieldValue) {
		if ((oldFieldValue == null && newFieldValue == null) || ObjectUtil.equals(oldFieldValue, newFieldValue)) {
			return "";
		}
		AttrOptionEnum op = AttrOptionEnum.changeTypeEnum(oldFieldValue, newFieldValue);
		AttributeChange attributeChange = new AttributeChange();
		attributeChange.setOp(op.name());
		String name = field.getName();
		attributeChange.setProperty(name);
		attributeChange.setPath("/" + name);
		attributeChange.setLeftValue(StrUtil.toString(oldFieldValue));
		attributeChange.setRightValue(StrUtil.toString(newFieldValue));

		return JSONUtil.toJsonStr(attributeChange);
	}

}
