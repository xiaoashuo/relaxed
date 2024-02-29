package com.relaxed.common.log.biz.extractor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.biz.annotation.LogDiffTag;
import com.relaxed.common.log.biz.enums.AttrOptionEnum;
import com.relaxed.common.log.biz.model.AttributeChange;

import java.lang.reflect.Field;

/**
 * @author Yakir
 * @Topic DefaultTypeConverter
 * @Description
 * @date 2021/12/14 17:28
 * @Version 1.0
 */
public class SimpleTypeDiffExtractor implements DiffExtractor {

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
