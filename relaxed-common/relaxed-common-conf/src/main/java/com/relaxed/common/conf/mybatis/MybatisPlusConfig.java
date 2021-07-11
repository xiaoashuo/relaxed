package com.relaxed.common.conf.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.relaxed.extend.mybatis.plus.injector.CustomSqlInjector;
import com.relaxed.extend.mybatis.plus.methods.InsertBatch;
import com.relaxed.extend.mybatis.plus.methods.InsertBatchSomeColumnByCollection;
import com.relaxed.extend.mybatis.plus.methods.InsertIgnoreBatch;
import com.relaxed.extend.mybatis.plus.methods.ReplaceBatch;
import net.sf.jsqlparser.statement.replace.Replace;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hccake
 * @date 2020/04/19 默认配置MybatisPlus分页插件，通过conditional注解达到覆盖效用
 */
@Configuration
public class MybatisPlusConfig {

	/**
	 * MybatisPlusInterceptor 插件，默认提供分页插件</br>
	 * 如需其他MP内置插件，则需自定义该Bean
	 * @return MybatisPlusInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean(MybatisPlusInterceptor.class)
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return interceptor;
	}

	/**
	 * 自动填充处理类
	 * @return FillMetaObjectHandle
	 */
	@Bean
	@ConditionalOnMissingBean(MetaObjectHandler.class)
	public FillMetaObjectHandle fillMetaObjectHandle() {
		return new FillMetaObjectHandle();
	}

	/**
	 * 自定义批量插入方法注入
	 * @return ISqlInjector
	 */
	@Bean
	@ConditionalOnMissingBean(ISqlInjector.class)
	public ISqlInjector customSqlInjector() {
		List<AbstractMethod> list = new ArrayList<>();
		// 对于只在更新时进行填充的字段不做插入处理
		list.add(new InsertBatchSomeColumnByCollection(t -> t.getFieldFill() != FieldFill.UPDATE));
		list.add(new InsertBatch());
		list.add(new InsertIgnoreBatch());
		list.add(new ReplaceBatch());
		return new CustomSqlInjector(list);
	}

}
