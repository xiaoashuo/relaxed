package com.relaxed.common.oss.s3;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/7/16 15:34
 */
@Data
@ConfigurationProperties(prefix = "relaxed.oss")
public class OssProperties {

	/**
	 *
	 * <p>
	 * endpoint 节点地址, 例如:
	 * </p>
	 * <ul>
	 * <li>阿里云节点: https://oss-cn-qingdao.aliyuncs.com</li>
	 * <li>亚马逊s3节点: https://s3.ap-southeast-1.amazonaws.com</li>
	 * <li>亚马逊节点: https://ap-southeast-1.amazonaws.com</li>
	 * <li>腾讯云节点: https://cos.ap-shanghai.myqcloud.com</li>
	 * <li>华为云节点: https://obs.cn-east-3.myhuaweicloud.com</li>
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
	 * 区域 必须配置 不配做默认取南方节点
	 * </p>
	 *
	 */
	private String region = Region.US_EAST_1.toString();

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
	 */
	private String domain;

	/**
	 * true path-style nginx 反向代理和S3默认支持 pathStyle {http://endpoint/bucketname} false
	 * supports virtual-hosted-style 阿里云等需要配置为 virtual-hosted-style
	 * 模式{http://bucketname.endpoint}
	 */
	private Boolean pathStyleAccess = true;

	/**
	 * 所有文件相关操作都在此路径下进行操作
	 */
	private String rootPath = OssConstants.SLASH;

	/**
	 * 上传时为文件配置acl, 为null 不配置
	 */
	private ObjectCannedACL acl;

}
