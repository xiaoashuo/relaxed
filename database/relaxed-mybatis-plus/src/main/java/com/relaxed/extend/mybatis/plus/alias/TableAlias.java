package com.relaxed.extend.mybatis.plus.alias;

import java.lang.annotation.*;

/**
 * 表别名注解
 * <p>
 * 用于标记实体类对应的数据库表别名。 在 SQL 查询中可以使用该别名来引用表。
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableAlias {

	/**
	 * 表别名
	 * <p>
	 * 在 SQL 查询中使用的表别名。
	 * @return 表别名
	 */
	String value();

}
