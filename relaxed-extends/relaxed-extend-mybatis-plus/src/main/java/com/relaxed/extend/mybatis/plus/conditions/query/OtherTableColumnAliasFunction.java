package com.relaxed.extend.mybatis.plus.conditions.query;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * 连表查询时，从其他表获取的查询条件
 *
 * @author hccake
 */
@FunctionalInterface
public interface OtherTableColumnAliasFunction<T> extends SFunction<T, String> {

	/**
	 * 联表别名 -> 加入本次查询
	 * @author yakir
	 * @date 2022/2/14 15:47
	 * @param aliasColumn 表 test 别名t 列明 id 则此处传入t.id
	 * @return com.relaxed.extend.mybatis.plus.conditions.query.OtherTableColumnAliasFunction
	 */
	static OtherTableColumnAliasFunction joinTableAliasColumn(String aliasColumn) {
		return o -> aliasColumn;
	}

}
