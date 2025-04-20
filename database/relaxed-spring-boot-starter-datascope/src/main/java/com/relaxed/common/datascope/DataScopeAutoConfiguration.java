package com.relaxed.common.datascope;

import com.relaxed.common.datascope.handler.DataPermissionHandler;
import com.relaxed.common.datascope.handler.DefaultDataPermissionHandler;
import com.relaxed.common.datascope.interceptor.DataPermissionAnnotationAdvisor;
import com.relaxed.common.datascope.interceptor.DataPermissionInterceptor;
import com.relaxed.common.datascope.processor.DataScopeSqlProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 数据权限自动配置类
 * <p>
 * 提供数据权限相关的自动配置，包括数据权限处理器、注解处理器和 MyBatis 拦截器的配置。 当存在 DataScope 实现类时自动生效。
 */
@RequiredArgsConstructor
@ConditionalOnBean(DataScope.class)
public class DataScopeAutoConfiguration {

	/**
	 * 配置数据权限处理器
	 * <p>
	 * 根据提供的数据范围集合创建默认的数据权限处理器。
	 * @param dataScopeList 需要控制的数据范围集合
	 * @return 数据权限处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public DataPermissionHandler dataPermissionHandler(List<DataScope> dataScopeList) {
		return new DefaultDataPermissionHandler(dataScopeList);
	}

	/**
	 * 配置数据权限注解处理器
	 * <p>
	 * 创建数据权限注解处理器，用于处理数据权限的链式调用关系。
	 * @return 数据权限注解处理器
	 */
	@Bean
	@ConditionalOnMissingBean(DataPermissionAnnotationAdvisor.class)
	public DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor() {
		return new DataPermissionAnnotationAdvisor();
	}

	/**
	 * 配置 MyBatis 数据权限拦截器
	 * <p>
	 * 创建数据权限拦截器，用于拦截和处理 SQL 语句。
	 * @param dataPermissionHandler 数据权限处理器
	 * @return 数据权限拦截器
	 */
	@Bean
	@ConditionalOnMissingBean
	public DataPermissionInterceptor dataPermissionInterceptor(DataPermissionHandler dataPermissionHandler) {
		return new DataPermissionInterceptor(new DataScopeSqlProcessor(), dataPermissionHandler);
	}

}
