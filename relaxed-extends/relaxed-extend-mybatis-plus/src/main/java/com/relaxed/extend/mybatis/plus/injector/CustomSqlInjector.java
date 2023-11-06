package com.relaxed.extend.mybatis.plus.injector;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.relaxed.extend.mybatis.plus.methods.InsertBatch;
import com.relaxed.extend.mybatis.plus.methods.InsertBatchSomeColumnByCollection;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Yakir
 * @Topic CustomSqlInjector
 * @Description
 * @date 2021/6/10 10:05
 * @Version 1.0
 */
@RequiredArgsConstructor
public class CustomSqlInjector extends DefaultSqlInjector {

	private final List<AbstractMethod> methods;

	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
		List<AbstractMethod> list = super.getMethodList(mapperClass, tableInfo);
		list.add(new InsertBatchSomeColumnByCollection(t -> t.getFieldFill() != FieldFill.UPDATE));
		list.add(new InsertBatch());
		list.addAll(this.methods);
		return list;
	}

}
