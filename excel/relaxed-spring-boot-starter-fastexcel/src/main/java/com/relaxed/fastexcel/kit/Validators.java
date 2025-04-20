package com.relaxed.fastexcel.kit;

import javax.validation.*;
import java.util.Set;

/**
 * Excel数据校验工具类 基于JSR-303规范实现的数据校验工具 主要功能: 1. 支持对象属性校验 2. 支持注解驱动校验 3. 支持自定义校验规则 4. 支持批量校验
 *
 * @author L.cm
 * @since 1.0.0
 */
public class Validators {

	/**
	 * JSR-303校验器实例
	 */
	private static final Validator validator;

	static {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	/**
	 * 校验对象的所有约束条件 校验规则: 1. 基于JSR-303注解 2. 支持自定义校验器 3. 支持分组校验 4. 支持级联校验
	 * @param object 待校验的对象
	 * @param <T> 对象类型
	 * @return 约束违反集合,如果没有违反则返回空集合
	 * @throws IllegalArgumentException 当对象为null时抛出
	 * @throws ValidationException 当校验过程中发生不可恢复的错误时抛出
	 */
	public static <T> Set<ConstraintViolation<T>> validate(T object) {
		return validator.validate(object);
	}

}
