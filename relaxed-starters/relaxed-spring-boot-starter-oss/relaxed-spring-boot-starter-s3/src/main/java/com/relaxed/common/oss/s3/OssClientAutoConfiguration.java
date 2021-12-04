package com.relaxed.common.oss.s3;

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
 * @author Yakir
 * @Topic OssClientAutoConfiguration
 * @Description
 * @date 2021/11/25 18:23
 * @Version 1.0
 */
@EnableConfigurationProperties({ OssProperties.class })
@Configuration(proxyBeanMethods = false)
public class OssClientAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public OssClientBuilder ossClientBuilder(OssProperties ossProperties, PathModifier pathModifier,
			ObjectProvider<OssClientCustomizer> ossClientBuilders) {
		OssClientBuilder ossClientBuilder = new OssClientBuilder();
		ossClientBuilder.rootPath(ossProperties.getRootPath());
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

	@Bean
	@ConditionalOnMissingBean
	public PathModifier pathModifier() {
		return new DefaultPathModifier();
	}

	@Lazy
	@Bean
	@ConditionalOnMissingBean({ OssClient.class })
	public OssClient ossClient(OssClientBuilder ossClientBuilder) {
		return ossClientBuilder.build();
	}

}
