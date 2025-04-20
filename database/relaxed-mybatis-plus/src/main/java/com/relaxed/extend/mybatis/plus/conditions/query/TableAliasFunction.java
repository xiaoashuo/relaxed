package com.relaxed.extend.mybatis.plus.conditions.query;

import com.relaxed.extend.mybatis.plus.alias.TableAliasHelper;

/**
 * 表别名函数接口
 * <p>
 * 用于处理表别名，支持自定义表别名的生成逻辑。 通常用于多表关联查询时，为表生成别名。 可以通过实现此接口来自定义表别名的生成规则。
 * </p>
 */
@FunctionalInterface
public interface TableAliasFunction {

	/**
	 * 获取表别名
	 * <p>
	 * 根据实体类获取对应的表别名。 实现类需要提供具体的表别名生成逻辑。
	 * </p>
	 * @param clazz 实体类
	 * @return 表别名
	 */
	String alias(Class<?> clazz);

	/**
	 * 根据别名注解获取别名
	 * <p>
	 * 使用 TableAlias 注解中定义的表别名。 如果实体类没有标注 TableAlias 注解，将抛出 TableAliasNotFoundException
	 * 异常。
	 * </p>
	 * @param entity 实体类
	 * @param <T> 实体类型
	 * @return 表别名函数
	 */
	static <T> TableAliasFunction aliasAnnotation(Class<T> entity) {
		return TableAliasHelper::tableAlias;
	}

}
