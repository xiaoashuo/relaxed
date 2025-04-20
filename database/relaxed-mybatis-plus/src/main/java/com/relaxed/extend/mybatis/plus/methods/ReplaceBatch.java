package com.relaxed.extend.mybatis.plus.methods;

/**
 * ReplaceBatch
 *
 * @author Yakir
 */
public class ReplaceBatch extends AbstractInsertBatch {

	private static final String SQL_METHOD = "replaceBatch";

	/**
	 * 表示插入替换数据，需求表中有PrimaryKey，或者unique索引，如果数据库已经存在数据，则用新数据替换，如果没有数据效果则和insert into一样；
	 */
	private static final String INSERT_IGNORE_SQL = "\"<script>\\nREPLACE INTO %s %s VALUES %s\\n</script>\"";

	public ReplaceBatch() {
		super(SQL_METHOD);
	}

	public ReplaceBatch(String methodName) {
		super(methodName);
	}

	@Override
	protected String getSql() {
		return SQL_METHOD;
	}

}
