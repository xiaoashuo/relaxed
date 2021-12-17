package com.relaxed.common.log.action.extractor;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.action.annotation.LogTag;
import com.relaxed.common.log.action.enums.AttrOptionEnum;
import com.relaxed.common.log.action.model.AttributeChange;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.assertj.core.util.Lists;
import org.javers.common.string.PrettyValuePrinter;
import org.javers.core.Changes;
import org.javers.core.ChangesByObject;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.NewObject;
import org.javers.core.diff.changetype.ObjectRemoved;
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

	@AllArgsConstructor
	@Data
	public static class Student {

		private Integer id;

		private String name;

		private Integer age;

		private List<Teacher> teachers;

		public Student(Integer id, String name, Integer age) {
			this.id = id;
			this.name = name;
			this.age = age;
		}

	}

	@Data
	public static class Teacher {

		private String name;

		private Integer age;

	}

	public static void main(String[] args) {
		Student old = new Student(1, "韩信", 22);
		Teacher teacher = new Teacher();
		teacher.setName("韩信的老师");
		teacher.setAge(33);
		old.setTeachers(Lists.newArrayList(teacher));

		Student newObj = new Student(1, "阿离", 32);
		Teacher teacher1 = new Teacher();
		teacher1.setName("阿离的老师");
		teacher1.setAge(44);
		newObj.setTeachers(Lists.newArrayList(teacher1));

		Javers javers = JaversBuilder.javers().withListCompareAlgorithm(LEVENSHTEIN_DISTANCE).build();

		Diff diff = javers.compare(old, newObj);
		System.out.println("对象是否有改变-" + diff.hasChanges());
		System.out.println(diff.prettyPrint());
		StringBuilder b = new StringBuilder();
		diff.groupByObject().forEach(it -> {
			String str = it.toString();
			b.append(str);
			System.out.println(str);
		});
		String diffJson = javers.getJsonConverter().toJson(diff);
		System.out.println("改变json" + diffJson);
		List<ChangesByObject> changesByObjects = diff.groupByObject();
		Changes changes = diff.getChanges();
		for (Change change : changes) {
			if ((change instanceof NewObject)) {
				System.out.println("新增改动: " + change);
				// change.getAffectedObject().ifPresent(System.out::println);
				System.out.println("新增");

			}

			if ((change instanceof ObjectRemoved)) {
				System.out.println("删除改动: " + change);
				// change.getAffectedObject().ifPresent(System.out::println);
			}

			if ((change instanceof ValueChange)) {
				GlobalId affectedGlobalId = change.getAffectedGlobalId();
				if (affectedGlobalId instanceof ValueObjectId) {
					ValueObjectId valueObjectId = (ValueObjectId) affectedGlobalId;
					String fragment = valueObjectId.getFragment();
				}
				System.out.println("修改改动: " + change);
				// change.getAffectedObject().ifPresent(System.out::println);
			}

		}

	}

}
