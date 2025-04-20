package com.relaxed.common.oss.s3;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

/**
 * S3 客户端自定义器接口。 用于在 S3 客户端构建过程中进行自定义配置。 通过实现此接口，可以在客户端创建时添加自定义的配置逻辑。
 *
 * @author Yakir
 * @since 1.0
 */
@FunctionalInterface
public interface OssClientCustomizer {

	/**
	 * 自定义 S3 客户端配置。 在 S3 客户端构建过程中调用此方法，允许对客户端进行自定义配置。
	 * @param s3Client S3 客户端构建器
	 */
	void customize(S3Client s3Client);

}
