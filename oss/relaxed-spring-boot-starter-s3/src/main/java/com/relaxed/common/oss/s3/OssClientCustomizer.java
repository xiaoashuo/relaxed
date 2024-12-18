package com.relaxed.common.oss.s3;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

/**
 * @author Yakir
 * @Topic OssClientCustomizer
 * @Description
 * @date 2021/11/25 18:17
 * @Version 1.0
 */
@FunctionalInterface
public interface OssClientCustomizer {

	/**
	 * 自定义 Customizer
	 * @author yakir
	 * @date 2021/11/25 18:19
	 * @param s3Client
	 */
	void customize(S3Client s3Client);

}
