package com.relaxed.extend.mybatis.plus.methods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;
import java.util.function.Predicate;

/**
 * 批量插入部分字段方法实现
 * <p>
 * 该实现类扩展了 MyBatis-Plus 的 {@link InsertBatchSomeColumn} 方法， 支持通过集合参数进行批量插入，并且可以指定要插入的字段。
 *
 * @author Yakir
 */
public class InsertBatchSomeColumnByCollection extends AbstractMethod {

	/**
	 * SQL 方法名称
	 */
	private static final String SQL_METHOD = "insertBatchSomeColumn";

	/**
	 * 默认构造方法
	 */
	public InsertBatchSomeColumnByCollection() {
		super(SQL_METHOD);
	}

	/**
	 * 字段筛选条件
	 * <p>
	 * 用于指定要插入的字段，可以通过设置该条件来过滤不需要插入的字段。
	 */
	@Setter
	@Accessors(chain = true)
	private Predicate<TableFieldInfo> predicate;

	/**
	 * 带字段筛选条件的构造方法
	 * @param predicate 字段筛选条件
	 */
	public InsertBatchSomeColumnByCollection(Predicate<TableFieldInfo> predicate) {
		this(SQL_METHOD, predicate);
	}

	/**
	 * 带方法名和字段筛选条件的构造方法
	 * @param methodName 方法名称
	 * @param predicate 字段筛选条件
	 */
	public InsertBatchSomeColumnByCollection(String methodName, Predicate<TableFieldInfo> predicate) {
		super(methodName);
		this.predicate = predicate;
	}

	/**
	 * 注入 MappedStatement
	 * <p>
	 * 处理主键生成策略，构建 SQL 语句，并创建 MappedStatement。 支持自增主键和序列主键的处理。
	 * @param mapperClass Mapper 接口类
	 * @param modelClass 实体类
	 * @param tableInfo 表信息
	 * @return MappedStatement
	 */
	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		KeyGenerator keyGenerator = new NoKeyGenerator();
		SqlMethod sqlMethod = SqlMethod.INSERT_ONE;
		List<TableFieldInfo> fieldList = tableInfo.getFieldList();
		String insertSqlColumn = tableInfo.getKeyInsertSqlColumn(true, null, false)
				+ this.filterTableFieldInfo(fieldList, predicate, TableFieldInfo::getInsertSqlColumn, EMPTY);
		String columnScript = LEFT_BRACKET + insertSqlColumn.substring(0, insertSqlColumn.length() - 1) + RIGHT_BRACKET;
		String insertSqlProperty = tableInfo.getKeyInsertSqlProperty(true, ENTITY_DOT, false)
				+ this.filterTableFieldInfo(fieldList, predicate, i -> i.getInsertSqlProperty(ENTITY_DOT), EMPTY);
		insertSqlProperty = LEFT_BRACKET + insertSqlProperty.substring(0, insertSqlProperty.length() - 1)
				+ RIGHT_BRACKET;
		// 从 list 改为 collection. 允许传入除 list外的参数类型
		String valuesScript = SqlScriptUtils.convertForeach(insertSqlProperty, "collection", null, ENTITY, COMMA);
		String keyProperty = null;
		String keyColumn = null;
		// 表包含主键处理逻辑,如果不包含主键当普通字段处理
		if (tableInfo.havePK()) {
			if (tableInfo.getIdType() == IdType.AUTO) {
				/* 自增主键 */
				keyGenerator = new Jdbc3KeyGenerator();
				keyProperty = tableInfo.getKeyProperty();
				keyColumn = tableInfo.getKeyColumn();
			}
			else {
				if (null != tableInfo.getKeySequence()) {
					keyGenerator = TableInfoHelper.genKeyGenerator(this.methodName, tableInfo, builderAssistant);
					keyProperty = tableInfo.getKeyProperty();
					keyColumn = tableInfo.getKeyColumn();
				}
			}
		}
		String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), columnScript, valuesScript);
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		return this.addInsertMappedStatement(mapperClass, modelClass, this.methodName, sqlSource, keyGenerator,
				keyProperty, keyColumn);
	}

}
