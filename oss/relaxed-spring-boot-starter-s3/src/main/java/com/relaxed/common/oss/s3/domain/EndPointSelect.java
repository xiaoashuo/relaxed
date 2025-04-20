package com.relaxed.common.oss.s3.domain;

import com.relaxed.common.oss.s3.modifier.PathModifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.net.URI;

/**
 * OSS 端点选择器。 用于管理和选择 OSS 服务的端点，支持： 1. 自定义域名访问 2. 虚拟主机访问 3. 路径样式访问 4. 代理端点配置
 *
 * @author Yakir
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public class EndPointSelect {

	/**
	 * 代理端点 URI
	 */
	private final URI proxyEndPoint;

	/**
	 * 代理 URL
	 */
	private final String proxyUrl;

	/**
	 * 是否使用虚拟主机地址
	 */
	private final boolean useVirtualAddress;

	/**
	 * 创建构建器实例
	 * @return 构建器实例
	 */
	public static Builder toBuilder() {
		return new Builder();
	}

	/**
	 * 端点选择器构建器。 用于构建 EndPointSelect 实例，支持链式调用。
	 */
	public static class Builder {

		private String domain;

		private String endpoint;

		private boolean pathStyleAccess;

		private String bucket;

		private PathModifier pathModifier;

		public Builder domain(String domain) {
			this.domain = domain;
			return this;
		}

		public Builder endpoint(String endpoint) {
			this.endpoint = endpoint;
			return this;
		}

		public Builder pathStyleAccess(boolean pathStyleAccess) {
			this.pathStyleAccess = pathStyleAccess;
			return this;
		}

		public Builder bucket(String bucket) {
			this.bucket = bucket;
			return this;
		}

		public Builder pathModifier(PathModifier pathModifier) {
			this.pathModifier = pathModifier;
			return this;
		}

		public EndPointSelect build() {
			URI proxyEndPoint;
			String proxyUrl;
			boolean useVirtualAddress;
			if (StringUtils.hasText(domain)) {
				proxyEndPoint = URI.create(domain);
				proxyUrl = domain;
				useVirtualAddress = false;
			}
			else {
				// 使用托管形式
				// 参考文档https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/VirtualHosting.html
				// 手动改变 替代 S3BucketEndpointResolver#changeToDnsEndpoint
				proxyEndPoint = URI.create(endpoint);
				useVirtualAddress = pathModifier.canUseVirtualAddressing(pathStyleAccess, bucket);
				if (useVirtualAddress) {
					proxyEndPoint = pathModifier.convertToVirtualHostEndpoint(proxyEndPoint, bucket);
					proxyUrl = proxyEndPoint.toString();
				}
				else {
					// 路径模式 经过拦截器会默认为endpoint后面加上bucket
					// 此时只需要下载地址保持和请求一致即可 BaseClientHandler#finalizeSdkHttpFullRequest#67
					proxyUrl = String.format("%s/%s", proxyEndPoint, bucket);
				}
			}
			return new EndPointSelect(proxyEndPoint, proxyUrl, useVirtualAddress);

		}

	}

}
