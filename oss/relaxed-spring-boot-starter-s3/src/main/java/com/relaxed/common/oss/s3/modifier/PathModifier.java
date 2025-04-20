package com.relaxed.common.oss.s3.modifier;

import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.s3.internal.BucketUtils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 路径修改器接口。 用于处理 OSS 请求路径的修改和转换，支持： 1. 请求路径修改 2. 虚拟主机地址判断 3. 虚拟主机端点转换 4. 下载 URL 生成
 *
 * @author Yakir
 * @since 1.0
 */
public interface PathModifier {

	/**
	 * 修改请求路径。 根据 bucket 名称和操作名称修改源路径。
	 * @param bucket 存储桶名称
	 * @param optionName 操作名称
	 * @param sourcePath 源路径
	 * @return 修改后的路径
	 */
	default String modifyRequestPath(String bucket, String optionName, String sourcePath) {
		return sourcePath.replaceFirst("/" + bucket, "");
	}

	/**
	 * 判断是否可以使用虚拟主机地址。 根据路径模式设置和存储桶名称判断是否支持虚拟主机访问。
	 * @param pathStyleAccess 是否开启路径模式，true 表示开启，false 表示使用虚拟主机
	 * @param bucket 存储桶名称
	 * @return 是否可以使用虚拟主机地址
	 */
	default boolean canUseVirtualAddressing(Boolean pathStyleAccess, String bucket) {
		return !pathStyleAccess && BucketUtils.isVirtualAddressingCompatibleBucketName(bucket, false);
	}

	/**
	 * 将端点转换为虚拟主机端点。 根据存储桶名称和原始端点生成新的虚拟主机端点。
	 * @param endpoint 原始端点
	 * @param bucket 存储桶名称
	 * @return 虚拟主机端点
	 */
	default URI convertToVirtualHostEndpoint(URI endpoint, String bucket) {
		return URI.create(String.format("%s://%s.%s", endpoint.getScheme(), bucket, endpoint.getAuthority()));
	}

	/**
	 * 生成下载 URL。 根据域名、存储桶、下载前缀和相对路径生成下载 URL。
	 * @param domain 域名
	 * @param bucket 存储桶名称
	 * @param downloadPrefix 下载前缀
	 * @param relativePath 相对路径
	 * @return 下载 URL
	 */
	default String getDownloadUrl(String domain, String bucket, String downloadPrefix, String relativePath) {
		if (StringUtils.hasText(domain)) {
			return String.format("%s/%s/%s", downloadPrefix, bucket, relativePath);
		}
		else {
			return String.format("%s/%s", downloadPrefix, relativePath);
		}
	}

}
