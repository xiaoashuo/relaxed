package com.relaxed.common.log.action.extractor;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.action.annotation.LogTag;
import com.relaxed.common.log.action.enums.AttrOptionEnum;
import com.relaxed.common.log.action.model.AttributeChange;

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

	private static Javers javers = JaversBuilder.javers().withListCompareAlgorithm(LEVENSHTEIN_DISTANCE).build();

	@Override
	public String diffValue(Field field, LogTag logTag, Object oldFieldValue, Object newFieldValue) {
		Diff diff = javers.compare(oldFieldValue, newFieldValue);
		if (!diff.hasChanges()) {
			return "";
		}
		PrettyValuePrinter printer = PrettyValuePrinter.getDefault();
		List<AttributeChange> attributeChanges = new ArrayList<>();
		Changes changes = diff.getChanges();
		for (Change change : changes) {
			AttributeChange attributeChange = new AttributeChange();
			GlobalId affectedGlobalId = change.getAffectedGlobalId();
			if (affectedGlobalId instanceof ValueObjectId) {
				ValueObjectId valueObjectId = (ValueObjectId) affectedGlobalId;
				String fragment = valueObjectId.getFragment();
				attributeChange.setPath("/" + fragment);
			}
			if ((change instanceof ValueChange)) {
				ValueChange valueChange = (ValueChange) change;

				String propertyName = valueChange.getPropertyName();
				Object left = valueChange.getLeft();
				Object right = valueChange.getRight();
				AttrOptionEnum op = left == null ? AttrOptionEnum.ADD
						: (right == null ? AttrOptionEnum.REMOVE : AttrOptionEnum.REPLACE);
				String path = attributeChange.getPath();
				if (!StringUtils.hasText(path)) {
					attributeChange.setPath("/" + propertyName);
				}
				else {
					attributeChange.setPath(path + "/" + propertyName);
				}
				attributeChange.setOp(op.toString());
				attributeChange.setProperty(propertyName);
				attributeChange.setLeftValue(printer.format(left));
				attributeChange.setRightValue(printer.format(right));

			}
			attributeChanges.add(attributeChange);
		}
		return JSONUtil.toJsonStr(attributeChanges);
	}

}
