package com.relaxed.extend.mybatis.plus.injector;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.relaxed.extend.mybatis.plus.methods.InsertBatch;
import com.relaxed.extend.mybatis.plus.methods.InsertBatchSomeColumnByCollection;
import lombok.RequiredArgsConstructor;

import java.util.List;

import com.relaxed.extend.mybatis.plus.methods.*;

import java.util.List;

/**
 * 自定义 SQL 注入器
 * <p>
 * 继承自 MyBatis-Plus 的 DefaultSqlInjector，提供自定义的 SQL 方法注入。 支持批量插入、忽略重复插入、替换插入等扩展功能。
 */
@RequiredArgsConstructor
public class CustomSqlInjector extends DefaultSqlInjector {

	private final List<AbstractMethod> methods;

	/**
	 * 获取自定义的 SQL 方法列表
	 * <p>
	 * 在原有的 SQL 方法基础上，添加自定义的批量操作方法。
	 * @param mapperClass Mapper 接口类
	 * @param tableInfo 表信息
	 * @return SQL 方法列表
	 */
	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
		List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
		methodList.add(new InsertBatch());
		methodList.add(new InsertIgnoreBatch());
		methodList.add(new ReplaceBatch());
		methodList.add(new InsertBatchSomeColumnByCollection());
		return methodList;
	}

}