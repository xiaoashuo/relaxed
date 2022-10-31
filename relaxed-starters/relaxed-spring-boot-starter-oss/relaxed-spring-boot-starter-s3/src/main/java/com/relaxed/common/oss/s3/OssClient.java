package com.relaxed.common.oss.s3;

import com.relaxed.common.oss.s3.exception.OssException;
import com.relaxed.common.oss.s3.modifier.PathModifier;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.utils.IoUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Yakir
 * @Topic OssClient
 * @Description
 * @date 2021/11/25 17:49
 * @Version 1.0
 */
@Slf4j
@Setter
public class OssClient implements DisposableBean {

	private final String endpoint;

	private final String region;

	private final String accessKey;

	private final String accessSecret;

	private final String bucket;

	private final String domain;

	private final boolean pathStyleAccess;

	private final String downloadPrefix;

	private final ObjectCannedACL acl;

	private final PathModifier pathModifier;

	private final S3Client s3Client;

	public OssClient(OssClientBuilder ossClientBuilder) {
		// 同步OssClientBuilder信息
		this.endpoint = ossClientBuilder.getEndpoint();
		this.region = ossClientBuilder.getRegion();
		this.accessKey = ossClientBuilder.getAccessKey();
		this.accessSecret = ossClientBuilder.getAccessSecret();
		this.bucket = ossClientBuilder.getBucket();
		this.domain = ossClientBuilder.getDomain();
		this.pathStyleAccess = ossClientBuilder.getPathStyleAccess();
		this.downloadPrefix = ossClientBuilder.getProxyUrl();
		this.acl = ossClientBuilder.getAcl();
		this.pathModifier = ossClientBuilder.getPathModifier();
		this.s3Client = ossClientBuilder.getS3Client();
	}

	public String upload(InputStream inputStream, Long size, String relativePath) {
		return upload(inputStream, size, relativePath, acl);
	}

	public String upload(File file, String relativePath) {
		return upload(file, relativePath, acl);
	}

	/**
	 * 上传文件
	 * @author yakir
	 * @date 2021/11/26 15:34
	 * @param inputStream 输入流
	 * @param size 流大小
	 * @param relativePath 文件路径 未关联根路径的
	 * @return java.lang.String
	 */
	public String upload(InputStream inputStream, Long size, String relativePath, ObjectCannedACL acl) {
		PutObjectRequest.Builder builder = PutObjectRequest.builder().bucket(bucket).key(relativePath);
		if (acl != null) {
			builder.acl(acl);
		}
		// 返回eTag
		try {
			getS3Client().putObject(builder.build(), RequestBody.fromInputStream(inputStream, size));
		}
		catch (Exception e) {
			throw new OssException(e, "upload file error bucket:%s,path:%s", this.bucket, relativePath);

		}
		return getDownloadUrl(relativePath);
	}

	/**
	 * 上传文件
	 * @author yakir
	 * @date 2022/1/19 16:12
	 * @param file
	 * @param relativePath
	 * @param acl
	 * @return java.lang.String
	 */
	public String upload(File file, String relativePath, ObjectCannedACL acl) {
		PutObjectRequest.Builder builder = PutObjectRequest.builder().bucket(bucket).key(relativePath);
		if (acl != null) {
			builder.acl(acl);
		}
		// 返回eTag
		try {
			getS3Client().putObject(builder.build(), RequestBody.fromFile(file));
		}
		catch (Exception e) {
			throw new OssException(e, "upload file error bucket:%s,path:%s", this.bucket, relativePath);
		}
		return getDownloadUrl(relativePath);
	}

	public List<String> list(String prefix) {
		return list(prefix, null, null);

	}

	/**
	 * 查询列表
	 * @author yakir
	 * @date 2021/12/1 18:19
	 * @param prefix 前缀匹配 oss 里面 文件物理层路径默认都为字符串 不存在目录
	 * @param marker
	 * @param maxKeys
	 */
	public List<String> list(String prefix, String marker, Integer maxKeys) {
		ListObjectsRequest listObjects = ListObjectsRequest.builder().bucket(bucket).prefix(prefix).marker(marker)
				.maxKeys(maxKeys).build();
		ListObjectsResponse res = getS3Client().listObjects(listObjects);
		List<S3Object> contents = res.contents();
		List<String> paths = new ArrayList<>();
		for (S3Object content : contents) {
			String key = content.key();
			String downloadUrl = getDownloadUrl(key);
			paths.add(downloadUrl);
		}
		return paths;

	}

	/**
	 * 下载字节文件
	 * @author yakir
	 * @date 2022/1/14 13:03
	 * @param relativePath 相对路径 test/img.png
	 * @return byte[]
	 */
	public byte[] download(String relativePath) {
		try {
			GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(relativePath).build();
			try (ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(getObjectRequest)) {
				return IoUtils.toByteArray(responseInputStream);
			}
		}
		catch (Exception e) {
			throw new OssException(e, "download file error bucket:%s,path:%s", this.bucket, relativePath);

		}
	}

	/**
	 * 删除单个文件
	 * @author yakir
	 * @date 2021/12/1 18:19
	 * @param path
	 */
	public void delete(String path) {
		if (!StringUtils.hasText(path)) {
			return;
		}
		try {
			getS3Client().deleteObject(builder -> builder.bucket(bucket).key(path));
		}
		catch (Exception e) {
			throw new OssException(e, "delete file error bucket:%s,path:%s", this.bucket, path);
		}
	}

	/**
	 * 批量删除
	 * @author yakir
	 * @date 2021/12/2 13:58
	 * @param paths 相对于bucket命名空间下的完整路径
	 * <p>
	 * eg: bucket test-oss 文件 img/test.jpg path: img/test.jpg
	 * </p>
	 * @param paths
	 */
	public void batchDelete(Set<String> paths) {
		if (CollectionUtils.isEmpty(paths)) {
			return;
		}
		ArrayList<ObjectIdentifier> keys = new ArrayList<>();
		for (String path : paths) {
			ObjectIdentifier objectId = ObjectIdentifier.builder().key(path).build();
			keys.add(objectId);
		}

		// Delete multiple objects in one request.
		Delete del = Delete.builder().objects(keys).build();
		DeleteObjectsRequest multiObjectDeleteRequest = DeleteObjectsRequest.builder()
				// 指定命名空间
				.bucket(bucket).delete(del).build();
		getS3Client().deleteObjects(multiObjectDeleteRequest);
	}

	/**
	 * copy 对象
	 * @author yakir
	 * @date 2021/12/3 15:53
	 * @param sourcePath img/6.jpg 相对于bucket下面的路径
	 * @param toPath img/5.jpg 相对于bucket下面的路径
	 * @return java.lang.String 目标下载地址
	 */
	public String copy(String sourcePath, String toPath) {
		CopyObjectRequest copyObjRequest = CopyObjectRequest.builder()
				// 此处一定要指定原始命名空间
				.sourceBucket(bucket).sourceKey(sourcePath).destinationBucket(bucket).destinationKey(toPath).build();

		try {
			getS3Client().copyObject(copyObjRequest);
		}
		catch (Exception e) {
			throw new OssException(e, "copy file error bucket:%s,source path:%s,dest path:%s", this.bucket, sourcePath,
					toPath);

		}
		return getDownloadUrl(toPath);

	}

	/**
	 * 获取 绝对路径 的下载url
	 * @author lingting 2021-05-12 18:50
	 */
	public String getDownloadUrl(String relativePath) {
		return pathModifier.getDownloadUrl(domain, bucket, downloadPrefix, relativePath);
	}

	public S3Client getS3Client() {
		return s3Client;
	}

	@Override
	public void destroy() throws Exception {
		if (s3Client != null) {
			s3Client.close();
		}
	}

}
