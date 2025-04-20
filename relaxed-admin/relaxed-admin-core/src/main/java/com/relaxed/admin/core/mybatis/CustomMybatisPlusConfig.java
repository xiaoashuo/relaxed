package com.relaxed.admin.core.mybatis;

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
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * MyBatis-Plus 自定义配置类 提供分页插件、自动填充处理器和自定义SQL注入器 支持批量插入、忽略插入和替换插入等扩展功能
 *
 * @author hccake
 * @since 1.0
 */
@Configuration
public class CustomMybatisPlusConfig {

	/**
	 * 配置MyBatis-Plus拦截器 默认提供分页插件，支持MySQL数据库 如需其他内置插件，需要自定义该Bean
	 * @return MyBatis-Plus拦截器实例
	 */
	@Bean
	@ConditionalOnMissingBean(MybatisPlusInterceptor.class)
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return interceptor;
	}

	/**
	 * 配置自动填充处理器 用于自动填充实体类中的创建时间和更新时间字段
	 * @return 自动填充处理器实例
	 */
	@Bean
	@ConditionalOnMissingBean(MetaObjectHandler.class)
	public FillMetaObjectHandle fillMetaObjectHandle() {
		return new FillMetaObjectHandle();
	}

	/**
	 * 配置自定义SQL注入器 注入批量插入、忽略插入和替换插入等扩展方法 对于只在更新时进行填充的字段不做插入处理
	 * @return SQL注入器实例
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
