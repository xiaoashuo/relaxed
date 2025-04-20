package com.relaxed.fastexcel.annotation;

import com.relaxed.fastexcel.head.HeadGenerator;

import java.lang.annotation.*;

/**
 * Excel工作表注解 用于配置Excel导出时的工作表信息,包括: 1. 工作表序号 2. 工作表名称 3. 包含/排除的字段 4. 表头生成器
 *
 * @author Yakir
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sheet {

	/**
	 * 工作表序号 -1表示使用默认序号
	 * @return 工作表序号
	 */
	int sheetNo() default -1;

	/**
	 * 工作表名称 必填项,用于指定导出的工作表名称
	 * @return 工作表名称
	 */
	String sheetName();

	/**
	 * 包含的字段列表 指定需要导出的字段,如果为空则导出所有字段
	 * @return 包含的字段列表
	 */
	String[] includes() default {};

	/**
	 * 排除的字段列表 指定不需要导出的字段
	 * @return 排除的字段列表
	 */
	String[] excludes() default {};

	/**
	 * 表头生成器类 用于自定义表头的生成逻辑
	 * @return 表头生成器类
	 */
	Class<? extends HeadGenerator> headGenerateClass() default HeadGenerator.class;

}
