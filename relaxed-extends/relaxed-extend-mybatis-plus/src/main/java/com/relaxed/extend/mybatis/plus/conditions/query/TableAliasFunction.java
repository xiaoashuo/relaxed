package com.relaxed.extend.mybatis.plus.conditions.query;

import com.relaxed.extend.mybatis.plus.alias.TableAliasHelper;

@FunctionalInterface
public interface TableAliasFunction<T> {

	/**
	 * 根据实体获取别名
	 * @author yakir
	 * @date 2022/4/12 10:44
	 * @param entity
	 * @return java.lang.String
	 */
	String alias(Class<T> entity);

	/**
	 * 根据别名注解获取别名
	 * @author yakir
	 * @date 2022/4/12 10:45
	 * @param entity
	 * @return com.relaxed.extend.mybatis.plus.conditions.query.TableAliasFunction
	 */
	static <T> TableAliasFunction aliasAnnotation(Class<T> entity) {
		return TableAliasHelper::tableAlias;
	}

}
