package com.relaxed.extend.mybatis.plus.conditions.query;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * 其他表列别名函数接口
 * <p>
 * 用于处理其他表的列别名，支持自定义列别名的生成逻辑。 通常用于多表关联查询时，为其他表的列生成别名。
 */
@FunctionalInterface
public interface OtherTableColumnAliasFunction<T> extends SFunction<T, String> {

	/**
	 * 获取列别名
	 * <p>
	 * 根据表别名和列名生成完整的列别名。
	 * @param tableAlias 表别名
	 * @param column 列名
	 * @return 完整的列别名
	 */
	static String alias(String tableAlias, String column) {
		return tableAlias + "." + column;
	}

}
