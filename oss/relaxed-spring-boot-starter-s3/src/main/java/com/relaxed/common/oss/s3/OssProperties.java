package com.relaxed.common.oss.s3;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;

/**
 * S3 对象存储配置属性类。 用于配置 S3 客户端的基本属性，包括端点、区域、访问凭证等。 通过 relaxed.oss 前缀进行配置。
 *
 * @author Yakir
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "relaxed.oss")
public class OssProperties {

	/**
	 * S3 服务端点地址。 支持多种云服务商的节点地址，例如： - 阿里云：https://oss-cn-qingdao.aliyuncs.com - 亚马逊
	 * S3：https://s3.ap-southeast-1.amazonaws.com -
	 * 亚马逊：https://ap-southeast-1.amazonaws.com - 腾讯云：https://cos.ap-shanghai.myqcloud.com
	 * - 华为云：https://obs.cn-east-3.myhuaweicloud.com
	 *
	 * 注意：如果使用自定义域名转发，则不需要配置此值。
	 */
	private String endpoint;

	/**
	 * S3 服务区域。 必须配置，如果不配置则默认使用 US_EAST_1 区域。
	 */
	private String region = Region.US_EAST_1.toString();

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
	 * 自定义域名。 配置后优先级高于 endpoint，此时 endpoint 和 pathStyleAccess 配置将失效。
	 */
	private String domain;

	/**
	 * 路径样式访问控制。 true：使用路径样式访问（如 http://endpoint/bucketname） false：使用虚拟主机样式访问（如
	 * http://bucketname.endpoint） 默认为 true，支持 nginx 反向代理和 S3 默认配置。
	 */
	private Boolean pathStyleAccess = true;

	/**
	 * 对象访问控制列表。 用于设置上传文件的访问权限，为 null 时不配置。
	 */
	private ObjectCannedACL acl;

}
