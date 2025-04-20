package com.relaxed.extend.mybatis.plus.methods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.relaxed.extend.mybatis.plus.toolkit.ExtendConstants;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 批量插入抽象方法类
 * <p>
 * 提供了批量插入数据的基础实现，包括： 1. 主键生成策略的处理 2. SQL 语句的构建 3. 字段和值的处理
 *
 * @author Yakir
 */
public abstract class AbstractInsertBatch extends AbstractMethod {

	/**
	 * 构造方法
	 * @param methodName 方法名称
	 */
	protected AbstractInsertBatch(String methodName) {
		super(methodName);
	}

	/**
	 * 注入 MappedStatement
	 * <p>
	 * 处理主键生成策略，构建 SQL 语句，并创建 MappedStatement。
	 * @param mapperClass Mapper 接口类
	 * @param modelClass 实体类
	 * @param tableInfo 表信息
	 * @return MappedStatement
	 */
	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		// === mybatis 主键逻辑处理：主键生成策略，以及主键回填=======
		KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
		String keyColumn = null;
		String keyProperty = null;
		// 如果需要回填主键
		if (tableInfo.getKeyProperty() != null && !"".equals(tableInfo.getKeyProperty())) {
			// 表包含主键处理逻辑,如果不包含主键当普通字段处理
			if (tableInfo.getIdType() == IdType.AUTO) {
				/* 自增主键 */
				keyGenerator = Jdbc3KeyGenerator.INSTANCE;
				keyProperty = getKeyProperty(tableInfo);
				keyColumn = tableInfo.getKeyColumn();
			}
			else {
				if (null != tableInfo.getKeySequence()) {
					keyGenerator = TableInfoHelper.genKeyGenerator(this.methodName, tableInfo, builderAssistant);
					keyProperty = getKeyProperty(tableInfo);
					keyColumn = tableInfo.getKeyColumn();
				}
			}
		}

		String sql = String.format(getSql(), tableInfo.getTableName(), prepareFieldSql(tableInfo),
				prepareValuesSqlForMysqlBatch(tableInfo));
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		return this.addInsertMappedStatement(mapperClass, modelClass, this.methodName, sqlSource, keyGenerator,
				keyProperty, keyColumn);
	}

	/**
	 * 获取主键属性名
	 * @param tableInfo 表信息
	 * @return 主键属性名
	 */
	private String getKeyProperty(TableInfo tableInfo) {
		return tableInfo.getKeyProperty();
	}

	/**
	 * 准备字段 SQL
	 * <p>
	 * 构建 INSERT 语句中的字段部分。
	 * @param tableInfo 表信息
	 * @return 字段 SQL
	 */
	protected String prepareFieldSql(TableInfo tableInfo) {
		List<TableFieldInfo> fieldList = tableInfo.getFieldList();
		String columnFields = tableInfo.getKeyInsertSqlColumn(true, null, false)
				+ fieldList.stream().map(AbstractInsertBatch::getInsertSqlColumn).collect(Collectors.joining(NEWLINE));
		return SqlScriptUtils.convertTrim(columnFields, LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
	}

	/**
	 * 准备值 SQL
	 * <p>
	 * 构建 INSERT 语句中的值部分。
	 * @param tableInfo 表信息
	 * @return 值 SQL
	 */
	protected String prepareValuesSqlForMysqlBatch(TableInfo tableInfo) {
		List<TableFieldInfo> fieldList = tableInfo.getFieldList();
		String valueList = tableInfo.getKeyInsertSqlProperty(true, ENTITY_DOT, true) + fieldList.stream()
				.map(AbstractInsertBatch::getInsertSqlPropertyMaybeIf).collect(Collectors.joining(NEWLINE));
		String valueSql = SqlScriptUtils.convertTrim(valueList, LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
		String valuesScript = SqlScriptUtils.convertForeach(valueSql, "collection", null, ENTITY, COMMA);
		return valuesScript;
	}

	/**
	 * 获取插入值的 SQL 脚本片段
	 * <p>
	 * 用于构建 INSERT 语句中的值部分。 不生成 if 标签。
	 * @param tableFieldInfo 表字段信息
	 * @return SQL 脚本片段
	 */
	private static String getInsertSqlPropertyMaybeIf(TableFieldInfo tableFieldInfo) {
		return tableFieldInfo.getInsertSqlProperty(ENTITY_DOT);
	}

	/**
	 * 获取插入字段的 SQL 脚本片段
	 * <p>
	 * 用于构建 INSERT 语句中的字段部分。 不生成 if 标签。
	 * @param tableFieldInfo 表字段信息
	 * @return SQL 脚本片段
	 */
	private static String getInsertSqlColumn(TableFieldInfo tableFieldInfo) {
		return tableFieldInfo.getInsertSqlColumn();
	}

	/**
	 * 获取 SQL 语句
	 * <p>
	 * 子类需要实现该方法，返回具体的 SQL 语句模板。
	 * @return SQL 语句模板
	 */
	protected abstract String getSql();

}
