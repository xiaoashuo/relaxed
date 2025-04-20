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
 * 动态数据源自动配置类 负责配置和管理动态数据源相关的组件，包括： 1. 动态数据源帮助类 - 提供数据源管理、密码加密解密等功能 2. 动态数据源提供者 -
 * 负责从数据库获取数据源配置 3. 数据源属性提供者 - 负责创建和配置数据源属性
 *
 * 配置说明： 1. 在DataSourceAutoConfiguration之后自动配置，确保基础数据源配置已完成 2.
 * 启用DataSourceProperties配置属性绑定 3. 支持自定义PropertyProvider实现，扩展数据源属性配置
 *
 * @author lengleng
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DataSourceProperties.class)
public class DynamicDataSourceAutoConfiguration {

	/**
	 * 配置动态数据源帮助类 提供数据源管理、密码加密解密、数据源校验等功能 支持动态添加和删除数据源
	 * @param stringEncryptor 字符串加密器，用于密码的加密解密
	 * @param defaultDataSourceCreator 默认数据源创建器，用于创建数据源连接池
	 * @param dynamicRoutingDataSource 动态路由数据源，用于管理多个数据源的路由和切换
	 * @param propertyProvider 数据源属性提供者，用于创建和配置数据源属性
	 * @return DynamicDataSourceHelper实例，提供数据源管理功能
	 */
	@Bean
	@ConditionalOnMissingBean
	public DynamicDataSourceHelper dynamicDataSourceHelper(StringEncryptor stringEncryptor,
			DefaultDataSourceCreator defaultDataSourceCreator, DynamicRoutingDataSource dynamicRoutingDataSource,
			PropertyProvider propertyProvider) {
		return new DynamicDataSourceHelper(stringEncryptor, defaultDataSourceCreator, dynamicRoutingDataSource,
				propertyProvider);
	}

	/**
	 * 配置动态数据源提供者 负责从数据库或其他来源获取数据源配置信息 支持加密密码的解密和数据源属性的自定义处理
	 * @param stringEncryptor 字符串加密器，用于解密数据库中的加密密码
	 * @param properties 数据源配置属性，包含数据源的基本配置信息
	 * @param propertyProvider 数据源属性提供者，用于创建和配置数据源属性
	 * @return DynamicDataSourceProvider实例，提供数据源配置获取功能
	 */
	@Bean
	@ConditionalOnMissingBean
	public DynamicDataSourceProvider dynamicDataSourceProvider(StringEncryptor stringEncryptor,
			DataSourceProperties properties, PropertyProvider propertyProvider) {
		return new JdbcDynamicDataSourceProvider(stringEncryptor, properties, propertyProvider);
	}

	/**
	 * 配置默认的数据源属性提供者 负责创建和配置数据源属性对象 可以通过实现PropertyProvider接口来自定义数据源属性的创建逻辑
	 * 默认实现设置基本的数据源属性，并启用延迟初始化
	 * @return PropertyProvider实例，提供数据源属性创建功能
	 */
	@Bean
	@ConditionalOnMissingBean
	public PropertyProvider propertyProvider() {
		return new DefaultPropertyProvider();
	}

}
