package com.relaxed.common.oss.s3.modifier;

import software.amazon.awssdk.services.s3.internal.BucketUtils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Yakir
 * @Topic PathModifier
 * @Description
 * @date 2021/12/3 14:50
 * @Version 1.0
 */
public interface PathModifier {

	/**
	 * 修改请求path
	 * @author yakir
	 * @date 2021/12/3 14:56
	 * @param optionName 操作名称
	 * @param sourcePath 源路径
	 * @return java.lang.String
	 */
	String modifyRequestPath(String bucket, String optionName, String sourcePath);

	/**
	 * 是否使用使用虚拟主机地址
	 * @author yakir
	 * @date 2021/12/3 14:56
	 * @param pathStyleAccess 是否开启路径模式 true 开启 false 虚拟主机
	 * @param bucket 目录空间
	 * @return java.lang.String
	 */
	default boolean canUseVirtualAddressing(Boolean pathStyleAccess, String bucket) {
		return !pathStyleAccess && BucketUtils.isVirtualAddressingCompatibleBucketName(bucket, false);
	}

	/**
	 * 转换虚拟主机端点 为新端点
	 * @author yakir
	 * @date 2021/12/3 14:56
	 * @param endpoint 端点
	 * @param bucket 目录空间
	 * @return URI
	 */
	default URI convertToVirtualHostEndpoint(URI endpoint, String bucket) {
		return URI.create(String.format("%s://%s.%s", endpoint.getScheme(), bucket, endpoint.getAuthority()));
	}

}
