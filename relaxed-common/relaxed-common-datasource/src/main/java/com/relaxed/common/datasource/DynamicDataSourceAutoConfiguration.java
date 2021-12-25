/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.relaxed.common.datasource;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.relaxed.common.datasource.config.DataSourceProperties;
import com.relaxed.common.datasource.config.JdbcDynamicDataSourceProvider;
import com.relaxed.common.datasource.provider.DefaultPropertyProvider;
import com.relaxed.common.datasource.provider.PropertyProvider;
import com.relaxed.common.datasource.toolkit.DynamicDataSourceHelper;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lengleng
 * @date 2020-02-06
 * <p>
 * 动态数据源切换配置
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DataSourceProperties.class)
public class DynamicDataSourceAutoConfiguration {

	/**
	 * 动态数据源帮助类
	 * @param stringEncryptor
	 * @param defaultDataSourceCreator
	 * @param dynamicRoutingDataSource
	 * @param propertyProvider
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public DynamicDataSourceHelper dynamicDataSourceHelper(StringEncryptor stringEncryptor,
			DefaultDataSourceCreator defaultDataSourceCreator, DynamicRoutingDataSource dynamicRoutingDataSource,
			PropertyProvider propertyProvider) {
		return new DynamicDataSourceHelper(stringEncryptor, defaultDataSourceCreator, dynamicRoutingDataSource,
				propertyProvider);
	}

	@Bean
	@ConditionalOnMissingBean
	public DynamicDataSourceProvider dynamicDataSourceProvider(StringEncryptor stringEncryptor,
			DataSourceProperties properties, PropertyProvider propertyProvider) {
		return new JdbcDynamicDataSourceProvider(stringEncryptor, properties, propertyProvider);
	}

	/**
	 * 数据源属性提供者 可以自定义实现 设置连接池属性等等
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public PropertyProvider propertyProvider() {
		return new DefaultPropertyProvider();
	}

}
