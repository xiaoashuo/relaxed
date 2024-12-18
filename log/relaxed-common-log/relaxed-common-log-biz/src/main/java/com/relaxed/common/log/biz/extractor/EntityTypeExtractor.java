package com.relaxed.common.log.biz.extractor;

import cn.hutool.json.JSONUtil;

import com.relaxed.common.log.biz.annotation.LogDiffTag;
import com.relaxed.common.log.biz.enums.AttrOptionEnum;
import com.relaxed.common.log.biz.model.AttributeChange;
import com.relaxed.common.log.biz.util.LogClassUtil;
import org.javers.common.string.PrettyValuePrinter;
import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.metamodel.object.GlobalId;
import org.javers.core.metamodel.object.ValueObjectId;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.javers.core.diff.ListCompareAlgorithm.LEVENSHTEIN_DISTANCE;

/**
 * @author Yakir
 * @Topic EntityTypeConverter
 * @Description
 * @date 2021/12/16 18:37
 * @Version 1.0
 */
public class EntityTypeExtractor implements DiffExtractor {

	@Override
	public String diffValue(Field field, LogDiffTag logDiffTag, Object oldFieldValue, Object newFieldValue) {
		List<AttributeChange> attributeChanges = LogClassUtil.diff(oldFieldValue, newFieldValue);
		return JSONUtil.toJsonStr(attributeChanges);
	}

}
