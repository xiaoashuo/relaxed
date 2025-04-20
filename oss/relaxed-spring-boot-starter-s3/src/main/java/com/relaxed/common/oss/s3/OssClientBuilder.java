package com.relaxed.common.oss.s3;

import com.relaxed.common.oss.s3.domain.EndPointSelect;
import com.relaxed.common.oss.s3.interceptor.ModifyPathInterceptor;
import com.relaxed.common.oss.s3.modifier.PathModifier;
import lombok.Getter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.awscore.util.AwsHostNameUtils;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.internal.BucketUtils;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * S3 对象存储客户端构建器。 用于构建和配置 S3 客户端，支持自定义端点、区域、访问凭证等配置。 提供了灵活的构建选项，包括路径样式访问、ACL 控制、自定义域名等。
 *
 * @author Yakir
 * @since 1.0
 */
@Getter
public class OssClientBuilder {

	/**
	 * S3 服务端点地址。 支持多种云服务商的节点地址，例如： - 阿里云：oss-cn-qingdao.aliyuncs.com - 亚马逊
	 * S3：s3.ap-southeast-1.amazonaws.com - 亚马逊：ap-southeast-1.amazonaws.com
	 *
	 * 注意：如果使用自定义域名转发，则不需要配置此值。
	 */
	private String endpoint;

	/**
	 * S3 服务区域。 如果配置了自定义域名(domain)，则必须配置此值。 如果未配置自定义域名但配置了节点，则此值可选。
	 */
	private String region;

	/**
	 * 访问密钥 ID。 用于身份验证的访问密钥。
	 */
	private String accessKey;

	/**
	 * 访问密钥密码。 用于身份验证的密钥密码。
	 */
	private String accessSecret;

	/**
	 * 存储桶名称。 必填项，用于指定存储空间。
	 */
	private String bucket;

	/**
	 * 自定义域名。 配置后优先级高于 endpoint，此时 endpoint 配置将失效。 配置此值时必须同时配置 region。
	 */
	private String domain;

	/**
	 * 路径样式访问控制。 true：使用路径样式访问（如 http://endpoint/bucketname） false：使用虚拟主机样式访问（如
	 * http://bucketname.endpoint） 默认为 true，支持 nginx 反向代理和 S3 默认配置。
	 */
	private Boolean pathStyleAccess = true;

	/**
	 * 代理服务器 URL。 用于配置 HTTP 代理服务器地址。
	 */
	private String proxyUrl;

	/**
	 * 对象访问控制列表。 用于设置对象的访问权限。
	 */
	private ObjectCannedACL acl;

	/**
	 * 客户端自定义器集合。 用于在构建过程中自定义客户端配置。
	 */
	private Set<OssClientCustomizer> customizers;

	/**
	 * 路径修改器。 用于自定义对象存储路径的修改规则。
	 */
	private PathModifier pathModifier;

	/**
	 * S3 客户端实例。
	 */
	private S3Client s3Client;

	/**
	 * 设置 S3 服务端点地址。
	 * @param endpoint 端点地址
	 * @return 当前构建器实例
	 */
	public OssClientBuilder endpoint(String endpoint) {
		this.endpoint = endpoint;
		return this;
	}

	/**
	 * 设置 S3 服务区域。
	 * @param region 区域名称
	 * @return 当前构建器实例
	 */
	public OssClientBuilder region(String region) {
		this.region = region;
		return this;
	}

	/**
	 * 设置访问密钥 ID。
	 * @param accessKey 访问密钥 ID
	 * @return 当前构建器实例
	 */
	public OssClientBuilder accessKey(String accessKey) {
		this.accessKey = accessKey;
		return this;
	}

	/**
	 * 设置访问密钥密码。
	 * @param accessSecret 访问密钥密码
	 * @return 当前构建器实例
	 */
	public OssClientBuilder accessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
		return this;
	}

	/**
	 * 设置存储桶名称。
	 * @param bucket 存储桶名称
	 * @return 当前构建器实例
	 */
	public OssClientBuilder bucket(String bucket) {
		this.bucket = bucket;
		return this;
	}

	/**
	 * 设置自定义域名。
	 * @param domain 自定义域名
	 * @return 当前构建器实例
	 */
	public OssClientBuilder domain(String domain) {
		this.domain = domain;
		return this;
	}

	/**
	 * 设置路径样式访问控制。
	 * @param pathStyleAccess 是否使用路径样式访问
	 * @return 当前构建器实例
	 */
	public OssClientBuilder pathStyleAccess(Boolean pathStyleAccess) {
		this.pathStyleAccess = pathStyleAccess;
		return this;
	}

	/**
	 * 设置对象访问控制列表。
	 * @param objectCannedACL 对象访问控制列表
	 * @return 当前构建器实例
	 */
	public OssClientBuilder acl(ObjectCannedACL objectCannedACL) {
		this.acl = objectCannedACL;
		return this;
	}

	/**
	 * 设置客户端自定义器集合。
	 * @param customizers 自定义器集合
	 * @return 当前构建器实例
	 */
	public OssClientBuilder customizers(Iterable<OssClientCustomizer> customizers) {
		this.customizers = this.append(null, customizers);
		return this;
	}

	/**
	 * 设置路径修改器。
	 * @param pathModifier 路径修改器
	 * @return 当前构建器实例
	 */
	public OssClientBuilder pathModifier(PathModifier pathModifier) {
		this.pathModifier = pathModifier;
		return this;
	}

	/**
	 * 将新元素添加到集合中。
	 * @param set 目标集合
	 * @param additions 要添加的元素
	 * @param <T> 元素类型
	 * @return 更新后的集合
	 */
	private <T> Set<T> append(Set<T> set, Iterable<? extends T> additions) {
		Set<T> result = new LinkedHashSet(set != null ? set : Collections.emptySet());
		additions.forEach(result::add);
		return Collections.unmodifiableSet(result);
	}

	/**
	 * 构建 S3 客户端实例。 根据配置创建并返回 S3 客户端实例。
	 * @return S3 客户端实例
	 */
	public OssClient build() {
		EndPointSelect endPointSelect = EndPointSelect.toBuilder().domain(domain).pathStyleAccess(pathStyleAccess)
				.bucket(bucket).endpoint(endpoint).pathModifier(pathModifier).build();
		proxyUrl = endPointSelect.getProxyUrl();
		// 构建S3Client
		S3ClientBuilder s3ClientBuilder = create(endPointSelect);
		s3Client = s3ClientBuilder.build();
		if (!CollectionUtils.isEmpty(this.customizers)) {
			this.customizers.forEach((customizer) -> {
				customizer.customize(s3Client);
			});
		}
		return new OssClient(this);
	}

	/**
	 * 创建 S3 客户端构建器。 根据端点选择策略创建相应的 S3 客户端构建器。
	 * @param endPointSelect 端点选择策略
	 * @return S3 客户端构建器
	 */
	private S3ClientBuilder create(EndPointSelect endPointSelect) {
		S3ClientBuilder builder = S3Client.builder();
		// 设置区域
		builder.region(Region.of(region));
		// 设置凭证
		AwsBasicCredentials awsCreds = AwsBasicCredentials.create(this.accessKey, this.accessSecret);
		builder.credentialsProvider(StaticCredentialsProvider.create(awsCreds));
		// 关闭路径形式
		builder.serviceConfiguration(sb -> sb.pathStyleAccessEnabled(pathStyleAccess).chunkedEncodingEnabled(false));

		builder.endpointOverride(endPointSelect.getProxyEndPoint()).credentialsProvider(
				StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, accessSecret)));
		// 配置

		builder.overrideConfiguration(cb -> {
			cb.addExecutionInterceptor(
					new ModifyPathInterceptor(bucket, endPointSelect.isUseVirtualAddress(), pathModifier));
		});
		return builder;
	}

}
