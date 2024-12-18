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
 * @author Yakir
 * @Topic LogClassUtil
 * @Description
 * @date 2023/12/20 9:59
 * @Version 1.0
 */
public class LogClassUtil {

	private static Javers javers;

	static {

		String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
		String DEFAULT_TIME_FORMAT = "HH:mm:ss";
		JaversCoreProperties.PrettyPrintDateFormats prettyPrintDateFormats = new JaversCoreProperties.PrettyPrintDateFormats();
		prettyPrintDateFormats.setLocalDateTime(DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT);
		prettyPrintDateFormats.setZonedDateTime(DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT + "Z");
		prettyPrintDateFormats.setLocalDate(DEFAULT_DATE_FORMAT);
		prettyPrintDateFormats.setLocalTime(DEFAULT_TIME_FORMAT);
		javers = JaversBuilder.javers().withListCompareAlgorithm(LEVENSHTEIN_DISTANCE)
				.withPrettyPrintDateFormats(prettyPrintDateFormats)
				.registerValue(BigDecimal.class, new BigDecimalValueComparator()).build();

	}

	/**
	 * 执行方法
	 *
	 * <p>
	 * 对于用户传入参数会做必要检查，包括：
	 *
	 * <pre>
	 *     1、忽略多余的参数
	 *     2、参数不够补齐默认值
	 *     3、传入参数为null，但是目标参数类型为原始类型，做转换
	 * </pre>
	 * @param <T> 返回对象类型
	 * @param obj 对象，如果执行静态方法，此值为{@code null}
	 * @param method 方法（对象方法或static方法都可）
	 * @param args 参数对象
	 * @return 结果
	 * @throws InvocationTargetRuntimeException 目标方法执行异常
	 * @throws UtilException {@link IllegalAccessException}异常的包装
	 * @since 5.8.1
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invokeRaw(Object obj, Method method, Object... args)
			throws InvocationTargetException, IllegalAccessException {
		ClassUtil.setAccessible(method);

		// 检查用户传入参数：
		// 1、忽略多余的参数
		// 2、参数不够补齐默认值
		// 3、通过NullWrapperBean传递的参数,会直接赋值null
		// 4、传入参数为null，但是目标参数类型为原始类型，做转换
		// 5、传入参数类型不对应，尝试转换类型
		final Class<?>[] parameterTypes = method.getParameterTypes();
		final Object[] actualArgs = new Object[parameterTypes.length];
		if (null != args) {
			// 判断参数类型长度是否为1 且为数组
			if (parameterTypes.length == 1 && parameterTypes[0].isArray()) {
				// 判断参数是否为1 个 若1个直接赋值 否则 强制转换
				if (args.length == 1) {
					if (false == parameterTypes[0].isAssignableFrom(args[0].getClass())) {
						// 对于类型不同的字段，尝试转换，转换失败则使用原对象类型
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
				for (int i = 0; i < actualArgs.length; i++) {
					if (i >= args.length || null == args[i]) {
						// 越界或者空值
						actualArgs[i] = ClassUtil.getDefaultValue(parameterTypes[i]);
					}
					else if (args[i] instanceof NullWrapperBean) {
						// 如果是通过NullWrapperBean传递的null参数,直接赋值null
						actualArgs[i] = null;
					}
					else if (false == parameterTypes[i].isAssignableFrom(args[i].getClass())) {
						// 对于类型不同的字段，尝试转换，转换失败则使用原对象类型
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

		if (method.isDefault()) {
			// 当方法是default方法时，尤其对象是代理对象，需使用句柄方式执行
			// 代理对象情况下调用method.invoke会导致循环引用执行，最终栈溢出
			return MethodHandleUtil.invokeSpecial(obj, method, args);
		}

		return (T) method.invoke(ClassUtil.isStatic(method) ? null : obj, actualArgs);
	}

	/**
	 * 比较两个对象差异
	 * @param source
	 * @param target
	 * @return
	 */
	public static Diff compare(Object source, Object target) {
		return javers.compare(source, target);
	}

	/**
	 * diff 比较
	 * @param leftValue
	 * @param rightValue
	 * @return
	 */
	public static List<AttributeChange> diff(Object leftValue, Object rightValue) {
		return diff(leftValue, rightValue, (attributeChange, source, target) -> true);
	}

	/**
	 * diff 比较
	 * @param leftValue 原始对象
	 * @param rightValue 差异对象
	 * @param propertyFilter 数据集属性过滤器
	 * @return
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
