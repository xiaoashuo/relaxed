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
 * @author Yakir
 * @Topic OssClientBuilder
 * @Description
 * @date 2021/11/25 18:08
 * @Version 1.0
 */
@Getter
public class OssClientBuilder {

	/**
	 *
	 * <p>
	 * endpoint 节点地址, 例如:
	 * </p>
	 * <ul>
	 * <li>阿里云节点: oss-cn-qingdao.aliyuncs.com</li>
	 * <li>亚马逊s3节点: s3.ap-southeast-1.amazonaws.com</li>
	 * <li>亚马逊节点: ap-southeast-1.amazonaws.com</li>
	 * </ul>
	 * <p>
	 * 只需要完整 正确的节点地址即可
	 * </p>
	 * <p>
	 * 如果使用 自定义的域名转发 不需要配置本值
	 * </p>
	 */
	private String endpoint;

	/**
	 * <p>
	 * 区域
	 * </p>
	 *
	 * <p>
	 * 如果配置了自定义域名(domain), 必须配置本值
	 * </p>
	 * <p>
	 * 如果没有配置自定义域名(domain), 配置了节点, 那么当前值可以不配置
	 * </p>
	 */
	private String region;

	/**
	 * 密钥key
	 */
	private String accessKey;

	/**
	 * 密钥Secret
	 */
	private String accessSecret;

	/**
	 * <p>
	 * bucket 必填 命名空间
	 * </p>
	 */
	private String bucket;

	/**
	 * <p>
	 * 自定义域名转发
	 * </p>
	 * <p>
	 * 本值优先级最高, 配置本值后 endpoint 无效
	 * </p>
	 * <p>
	 * 配置本值必须配置 region
	 * </p>
	 */
	private String domain;

	/**
	 * true path-style nginx 反向代理和S3默认支持 pathStyle {http://endpoint/bucketname} false
	 * supports virtual-hosted-style 阿里云等需要配置为 virtual-hosted-style
	 * 模式{http://bucketname.endpoint}
	 */
	private Boolean pathStyleAccess = true;

	/**
	 * 代理url
	 */
	private String proxyUrl;

	/**
	 * 上传时为文件配置acl, 为null 不配置
	 */
	private ObjectCannedACL acl;

	private Set<OssClientCustomizer> customizers;

	private PathModifier pathModifier;

	private S3Client s3Client;

	public OssClientBuilder endpoint(String endpoint) {
		this.endpoint = endpoint;
		return this;
	}

	public OssClientBuilder region(String region) {
		this.region = region;
		return this;
	}

	public OssClientBuilder accessKey(String accessKey) {
		this.accessKey = accessKey;
		return this;
	}

	public OssClientBuilder accessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
		return this;
	}

	public OssClientBuilder bucket(String bucket) {
		this.bucket = bucket;
		return this;
	}

	public OssClientBuilder domain(String domain) {
		this.domain = domain;
		return this;
	}

	public OssClientBuilder pathStyleAccess(Boolean pathStyleAccess) {
		this.pathStyleAccess = pathStyleAccess;
		return this;
	}

	public OssClientBuilder acl(ObjectCannedACL objectCannedACL) {
		this.acl = objectCannedACL;
		return this;
	}

	public OssClientBuilder customizers(Iterable<OssClientCustomizer> customizers) {
		this.customizers = this.append(null, customizers);
		return this;
	}

	public OssClientBuilder pathModifier(PathModifier pathModifier) {
		this.pathModifier = pathModifier;
		return this;
	}

	private <T> Set<T> append(Set<T> set, Iterable<? extends T> additions) {
		Set<T> result = new LinkedHashSet(set != null ? set : Collections.emptySet());
		additions.forEach(result::add);
		return Collections.unmodifiableSet(result);
	}

	public OssClient build() {
		EndPointSelect endPointSelect = EndPointSelect.toBuilder().domain(domain).pathStyleAccess(pathStyleAccess)
				.bucket(bucket).endpoint(endpoint).build();
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
