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
 * 实体类型差异提取器，用于处理复杂对象类型的差异比较。 该实现类通过 {@link LogClassUtil} 工具类比较对象的属性变化， 并将变化结果转换为 JSON
 * 格式的字符串。 主要用于处理实体类对象的属性变更记录。
 *
 * @author Yakir
 * @since 1.0.0
 */
public class EntityTypeExtractor implements DiffExtractor {

	/**
	 * 提取并转换实体对象的差异值
	 * @param field 字段对象
	 * @param logDiffTag 差异标签注解
	 * @param oldFieldValue 字段的旧值
	 * @param newFieldValue 字段的新值
	 * @return 属性变更列表的 JSON 字符串表示
	 */
	@Override
	public String diffValue(Field field, LogDiffTag logDiffTag, Object oldFieldValue, Object newFieldValue) {
		List<AttributeChange> attributeChanges = LogClassUtil.diff(oldFieldValue, newFieldValue);
		return JSONUtil.toJsonStr(attributeChanges);
	}

}
