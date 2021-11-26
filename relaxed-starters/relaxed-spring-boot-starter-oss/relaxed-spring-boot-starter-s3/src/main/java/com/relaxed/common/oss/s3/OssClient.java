package com.relaxed.common.oss.s3;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

/**
 * @author Yakir
 * @Topic OssClient
 * @Description
 * @date 2021/11/25 17:49
 * @Version 1.0
 */
@Setter
@Getter
public class OssClient {


	private  String endpoint;

	private  String region;

	private  String accessKey;

	private  String accessSecret;

	private  String bucket;
	private  String domain;
	private  String root;
	/**
	 * true path-style nginx 反向代理和S3默认支持 pathStyle {http://endpoint/bucketname} false
	 * supports virtual-hosted-style 阿里云等需要配置为 virtual-hosted-style
	 * 模式{http://bucketname.endpoint}
	 */
	private Boolean pathStyleAccess = true;
	private  ObjectCannedACL acl;

	private S3Client s3Client;

	public void setS3Client(S3Client s3Client) {
		this.s3Client = s3Client;
	}


	/**
	 * 上传文件
	 * @author yakir
	 * @date 2021/11/26 15:34
	 * @param inputStream 输入流
	 * @param size  流大小
	 * @param relativePath 文件路径  未关联根路径的
	 * @return java.lang.String
	 */
	public String upload(InputStream inputStream,Long size,String relativePath){
		String fullPath = getPath(relativePath);
		PutObjectRequest.Builder builder = PutObjectRequest.builder().bucket(bucket).key(fullPath);
		getS3Client().putObject(builder.build(), RequestBody.fromInputStream(inputStream,size));
        return fullPath;
	}


	/**
	 * 获取相对与bucket的完整路径
	 * @author yakir
	 * @date 2021/11/26 15:20
	 * @param relativePath
	 * @return java.lang.String
	 */
	public String getPath(String relativePath){
		Assert.hasText(relativePath, "path must not be empty");
		if (relativePath.startsWith(OssConstants.SLASH)) {
			relativePath = relativePath.substring(1);
		}
		return getRoot() + relativePath;
	}

}
