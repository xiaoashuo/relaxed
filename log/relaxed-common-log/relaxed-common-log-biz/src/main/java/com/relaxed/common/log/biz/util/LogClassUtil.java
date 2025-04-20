package com.relaxed.common.log.biz.util;

import cn.hutool.core.bean.NullWrapperBean;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.InvocationTargetRuntimeException;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.reflect.MethodHandleUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import com.relaxed.common.log.biz.enums.AttrOptionEnum;
import com.relaxed.common.log.biz.model.AttributeChange;
import org.javers.common.string.PrettyValuePrinter;
import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.JaversCoreProperties;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.custom.CustomBigDecimalComparator;
import org.javers.core.metamodel.object.GlobalId;
import org.javers.core.metamodel.object.ValueObjectId;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.javers.core.diff.ListCompareAlgorithm.LEVENSHTEIN_DISTANCE;

/**
 * 日志类工具类 该工具类提供了对象比较和方法调用的相关功能 主要功能包括： 1. 使用 Javers 进行对象差异比较 2. 支持方法调用和参数处理 3.
 * 提供属性变更的格式化输出
 *
 * @author Yakir
 */
public class LogClassUtil {

	/**
	 * Javers 实例，用于对象差异比较
	 */
	private static Javers javers;

	static {
		// 配置日期时间格式
		String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
		String DEFAULT_TIME_FORMAT = "HH:mm:ss";
		JaversCoreProperties.PrettyPrintDateFormats prettyPrintDateFormats = new JaversCoreProperties.PrettyPrintDateFormats();
		prettyPrintDateFormats.setLocalDateTime(DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT);
		prettyPrintDateFormats.setZonedDateTime(DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT + "Z");
		prettyPrintDateFormats.setLocalDate(DEFAULT_DATE_FORMAT);
		prettyPrintDateFormats.setLocalTime(DEFAULT_TIME_FORMAT);
		// 初始化 Javers 实例
		javers = JaversBuilder.javers().withListCompareAlgorithm(LEVENSHTEIN_DISTANCE)
				.withPrettyPrintDateFormats(prettyPrintDateFormats)
				.registerValue(BigDecimal.class, new BigDecimalValueComparator()).build();
	}

	/**
	 * 执行方法调用 对用户传入参数进行必要检查，包括： 1. 忽略多余的参数 2. 参数不够时补齐默认值 3. 处理 null 参数 4. 处理参数类型转换
	 * @param <T> 返回对象类型
	 * @param obj 对象，如果执行静态方法，此值为 null
	 * @param method 方法（对象方法或静态方法）
	 * @param args 参数对象
	 * @return 方法执行结果
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invokeRaw(Object obj, Method method, Object... args)
			throws InvocationTargetException, IllegalAccessException {
		ClassUtil.setAccessible(method);

		// 检查用户传入参数
		final Class<?>[] parameterTypes = method.getParameterTypes();
		final Object[] actualArgs = new Object[parameterTypes.length];
		if (null != args) {
			// 处理数组参数
			if (parameterTypes.length == 1 && parameterTypes[0].isArray()) {
				if (args.length == 1) {
					if (false == parameterTypes[0].isAssignableFrom(args[0].getClass())) {
						final Object targetValue = Convert.convert(parameterTypes[0], args[0]);
						if (null != targetValue) {
							actualArgs[0] = targetValue;
						}
					}
					else {
						actualArgs[0] = args[0];
					}
				}
				else {
					actualArgs[0] = ArrayUtil.addAll(args);
				}
			}
			else {
				// 处理普通参数
				for (int i = 0; i < actualArgs.length; i++) {
					if (i >= args.length || null == args[i]) {
						actualArgs[i] = ClassUtil.getDefaultValue(parameterTypes[i]);
					}
					else if (args[i] instanceof NullWrapperBean) {
						actualArgs[i] = null;
					}
					else if (false == parameterTypes[i].isAssignableFrom(args[i].getClass())) {
						final Object targetValue = Convert.convert(parameterTypes[i], args[i]);
						if (null != targetValue) {
							actualArgs[i] = targetValue;
						}
					}
					else {
						actualArgs[i] = args[i];
					}
				}
			}
		}

		// 处理 default 方法
		if (method.isDefault()) {
			return MethodHandleUtil.invokeSpecial(obj, method, args);
		}

		return (T) method.invoke(ClassUtil.isStatic(method) ? null : obj, actualArgs);
	}

	/**
	 * 比较两个对象的差异
	 * @param source 源对象
	 * @param target 目标对象
	 * @return 差异结果
	 */
	public static Diff compare(Object source, Object target) {
		return javers.compare(source, target);
	}

	/**
	 * 比较两个对象的差异，返回属性变更列表
	 * @param leftValue 原始对象
	 * @param rightValue 目标对象
	 * @return 属性变更列表
	 */
	public static List<AttributeChange> diff(Object leftValue, Object rightValue) {
		return diff(leftValue, rightValue, (attributeChange, source, target) -> true);
	}

	/**
	 * 比较两个对象的差异，返回过滤后的属性变更列表
	 * @param leftValue 原始对象
	 * @param rightValue 目标对象
	 * @param propertyFilter 属性过滤器
	 * @return 过滤后的属性变更列表
	 */
	public static List<AttributeChange> diff(Object leftValue, Object rightValue, PropertyFilter propertyFilter) {
		Diff diff = compare(leftValue, rightValue);
		if (!diff.hasChanges()) {
			return new ArrayList<>();
		}
		Class<?> sourceClass = leftValue.getClass();
		Class<?> targetClass = rightValue.getClass();

		PrettyValuePrinter printer = javers.getCoreConfiguration().getPrettyValuePrinter();
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
				AttrOptionEnum op = AttrOptionEnum.changeTypeEnum(left, right);
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
		return attributeChanges.stream().filter(item -> propertyFilter.filterProperty(item, sourceClass, targetClass))
				.collect(Collectors.toList());
	}

}
