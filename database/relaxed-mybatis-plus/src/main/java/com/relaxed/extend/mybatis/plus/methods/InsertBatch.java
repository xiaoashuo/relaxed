package com.relaxed.extend.mybatis.plus.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;

/**
 * 批量插入方法实现
 * <p>
 * 该实现类提供了批量插入数据的功能，支持通过集合参数进行批量插入。 继承自 {@link AbstractInsertBatch}，使用 MyBatis-Plus 的
 * {@link SqlMethod#INSERT_ONE} 作为 SQL 模板。
 *
 * @author Yakir
 */
public class InsertBatch extends AbstractInsertBatch {

	/**
	 * SQL 方法名称
	 */
	private static final String SQL_METHOD = "insertBatch";

	/**
	 * 默认构造方法
	 */
	public InsertBatch() {
		super(SQL_METHOD);
	}

	/**
	 * 带方法名的构造方法
	 * @param methodName 方法名称
	 */
	public InsertBatch(String methodName) {
		super(methodName);
	}

	/**
	 * 获取 SQL 语句模板
	 * <p>
	 * 使用 MyBatis-Plus 的 INSERT_ONE SQL 模板。
	 * @return SQL 语句模板
	 */
	@Override
	protected String getSql() {
		return SqlMethod.INSERT_ONE.getSql();
	}

}
