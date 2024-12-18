package com.relaxed.extend.mybatis.plus.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;

/**
 * @author Yakir
 * @Topic InsertBatch
 * @Description
 * @date 2021/7/11 10:46
 * @Version 1.0
 */
public class InsertBatch extends AbstractInsertBatch {

	private static final String SQL_METHOD = "insertBatch";

	public InsertBatch() {
		super(SQL_METHOD);
	}

	public InsertBatch(String methodName) {
		super(methodName);
	}

	@Override
	protected String getSql() {
		return SqlMethod.INSERT_ONE.getSql();
	}

}
