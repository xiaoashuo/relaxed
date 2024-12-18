package com.relaxed.extend.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic MapF2F
 * @Description
 * @date 2021/10/20 15:35
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface MapResult {

	/**
	 * 列key
	 * @return
	 */
	String key();

	/**
	 * 获取值名称 默认获取所有数据
	 * @return
	 */
	String[] valueNames() default {};

	/**
	 * 值拼接符 当接收map泛型参数为String类型，生效
	 * @return
	 */
	String valJoint() default "-";

	/**
	 * 是否允许key重复。如果不允许，而实际结果出现了重复，会抛出org.springframework.dao.DuplicateKeyException。
	 * @return boolean
	 */
	boolean isAllowKeyRepeat() default true;

}
