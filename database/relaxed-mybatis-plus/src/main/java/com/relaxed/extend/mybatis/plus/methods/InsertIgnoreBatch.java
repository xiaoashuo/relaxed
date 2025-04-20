package com.relaxed.extend.mybatis.plus.methods;

/**
 * 忽略重复的批量插入方法实现
 * <p>
 * 该实现类提供了忽略重复数据的批量插入功能。 当插入的数据与表中已存在的数据发生唯一键冲突时，会忽略该条数据的插入。 继承自
 * {@link AbstractInsertBatch}，使用 MySQL 的 INSERT IGNORE 语法。
 *
 * @author Yakir
 */
public class InsertIgnoreBatch extends AbstractInsertBatch {

	/**
	 * SQL 方法名称
	 */
	private static final String SQL_METHOD = "insertIgnoreBatch";

	/**
	 * 默认构造方法
	 */
	public InsertIgnoreBatch() {
		super(SQL_METHOD);
	}

	/**
	 * 带方法名的构造方法
	 * @param methodName 方法名称
	 */
	public InsertIgnoreBatch(String methodName) {
		super(methodName);
	}

	/**
	 * MySQL 的 INSERT IGNORE 语句模板
	 * <p>
	 * 使用 MySQL 的 INSERT IGNORE 语法，当插入的数据与表中已存在的数据发生唯一键冲突时， 会忽略该条数据的插入，而不是抛出异常。
	 */
	private static final String INSERT_IGNORE_SQL = "<script>\\nINSERT IGNORE INTO %s %s VALUES %s\\n</script>";

	/**
	 * 获取 SQL 语句模板
	 * <p>
	 * 返回 MySQL 的 INSERT IGNORE 语句模板。
	 * @return SQL 语句模板
	 */
	@Override
	protected String getSql() {
		return INSERT_IGNORE_SQL;
	}

}
