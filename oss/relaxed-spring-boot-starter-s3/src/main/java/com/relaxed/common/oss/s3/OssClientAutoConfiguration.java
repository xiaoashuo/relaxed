package com.relaxed.common.oss.s3;

import com.relaxed.common.core.util.file.FileHandlerLoader;
import com.relaxed.common.oss.s3.handler.OssFileHandler;
import com.relaxed.common.oss.s3.modifier.DefaultPathModifier;
import com.relaxed.common.oss.s3.modifier.PathModifier;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * S3 对象存储客户端自动配置类。 提供 S3 客户端的自动配置功能，包括： 1. 配置 S3 客户端构建器 2. 配置路径修改器 3. 创建 S3 客户端实例
 *
 * @author Yakir
 * @since 1.0
 */
@EnableConfigurationProperties({ OssProperties.class })
@Configuration(proxyBeanMethods = false)
public class OssClientAutoConfiguration {

	/**
	 * 创建 S3 客户端构建器。 如果容器中不存在 OssClientBuilder 实例，则创建一个新的实例。 配置包括端点、区域、访问密钥、存储桶等基本属性。
	 * @param ossProperties S3 配置属性
	 * @param pathModifier 路径修改器
	 * @param ossClientBuilders S3 客户端自定义器提供者
	 * @return S3 客户端构建器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public OssClientBuilder ossClientBuilder(OssProperties ossProperties, PathModifier pathModifier,
			ObjectProvider<OssClientCustomizer> ossClientBuilders) {
		OssClientBuilder ossClientBuilder = new OssClientBuilder();
		ossClientBuilder.endpoint(ossProperties.getEndpoint());
		ossClientBuilder.region(ossProperties.getRegion());
		ossClientBuilder.accessKey(ossProperties.getAccessKey());
		ossClientBuilder.accessSecret(ossProperties.getAccessSecret());
		ossClientBuilder.bucket(ossProperties.getBucket());
		ossClientBuilder.domain(ossProperties.getDomain());
		ossClientBuilder.pathStyleAccess(ossProperties.getPathStyleAccess());
		ossClientBuilder.acl(ossProperties.getAcl());
		ossClientBuilder.pathModifier(pathModifier);
		ossClientBuilder.customizers(ossClientBuilders.orderedStream()::iterator);
		return ossClientBuilder;
	}

	/**
	 * 创建默认路径修改器。 如果容器中不存在 PathModifier 实例，则创建一个新的 DefaultPathModifier 实例。
	 * @return 路径修改器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public PathModifier pathModifier() {
		return new DefaultPathModifier();
	}

	/**
	 * 创建 S3 客户端实例。 如果容器中不存在 OssClient 实例，则使用构建器创建一个新的实例。 使用 @Lazy 注解确保在需要时才创建实例。
	 * @param ossClientBuilder S3 客户端构建器
	 * @return S3 客户端实例
	 */
	@Lazy
	@Bean
	@ConditionalOnMissingBean({ OssClient.class })
	public OssClient ossClient(OssClientBuilder ossClientBuilder) {
		OssClient ossClient = ossClientBuilder.build();
		// 注册sftp文件处理器
		FileHandlerLoader.register(new OssFileHandler("oss", ossClient));
		return ossClient;
	}

}
