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
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.utils.IoUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * S3 对象存储客户端。 提供对 S3 兼容的对象存储服务的访问，支持文件上传、下载、删除等操作。 实现了 DisposableBean 接口，确保在应用关闭时正确释放资源。
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
@Setter
public class OssClient implements DisposableBean {

	/**
	 * S3 服务端点地址。
	 */
	private final String endpoint;

	/**
	 * S3 服务区域。
	 */
	private final String region;

	/**
	 * 访问密钥 ID。
	 */
	private final String accessKey;

	/**
	 * 访问密钥密码。
	 */
	private final String accessSecret;

	/**
	 * 存储桶名称。
	 */
	private final String bucket;

	/**
	 * 自定义域名。
	 */
	private final String domain;

	/**
	 * 是否使用路径样式访问。
	 */
	private final boolean pathStyleAccess;

	/**
	 * 下载文件时的前缀 URL。
	 */
	private final String downloadPrefix;

	/**
	 * 对象访问控制列表。
	 */
	private final ObjectCannedACL acl;

	/**
	 * 路径修改器。
	 */
	private final PathModifier pathModifier;

	/**
	 * S3 客户端实例。
	 */
	private final S3Client s3Client;

	/**
	 * 构造 S3 客户端实例。
	 * @param ossClientBuilder S3 客户端构建器
	 */
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

	/**
	 * 上传文件流。 使用默认的 ACL 配置上传文件。
	 * @param inputStream 文件输入流
	 * @param size 文件大小
	 * @param relativePath 相对路径
	 * @return 文件访问 URL
	 */
	public String upload(InputStream inputStream, Long size, String relativePath) {
		return upload(inputStream, size, relativePath, acl);
	}

	/**
	 * 上传本地文件。 使用默认的 ACL 配置上传文件。
	 * @param file 本地文件
	 * @param relativePath 相对路径
	 * @return 文件访问 URL
	 */
	public String upload(File file, String relativePath) {
		return upload(file, relativePath, acl);
	}

	/**
	 * 上传文件流。 使用指定的 ACL 配置上传文件。
	 * @param inputStream 文件输入流
	 * @param size 文件大小
	 * @param relativePath 相对路径
	 * @param acl 对象访问控制列表
	 * @return 文件访问 URL
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
	 * 上传本地文件。 使用指定的 ACL 配置上传文件。
	 * @param file 本地文件
	 * @param relativePath 相对路径
	 * @param acl 对象访问控制列表
	 * @return 文件访问 URL
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

	/**
	 * 列出指定前缀的所有对象。
	 * @param prefix 对象前缀
	 * @return 对象路径列表
	 */
	public List<String> list(String prefix) {
		return list(prefix, null, null);

	}

	/**
	 * 列出指定前缀的对象，支持分页。
	 * @param prefix 对象前缀
	 * @param marker 起始标记
	 * @param maxKeys 最大返回数量
	 * @return 对象路径列表
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
	 * 下载文件到字节数组。
	 * @param relativePath 文件相对路径
	 * @return 文件内容字节数组
	 */
	public byte[] download(String relativePath) {
		try {
			GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(relativePath).build();
			ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
			return objectBytes.asByteArray();
		}
		catch (Exception e) {
			throw new OssException(e, "download file error bucket:%s,path:%s", this.bucket, relativePath);

		}
	}

	/**
	 * 下载文件到本地。
	 * @param relativePath 文件相对路径
	 * @param file 目标文件
	 * @return 目标文件
	 */
	public File download(String relativePath, File file) {
		try {
			GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(relativePath).build();
			try (ResponseInputStream<GetObjectResponse> object = s3Client.getObject(getObjectRequest);
					FileOutputStream fileOutputStream = new FileOutputStream(file)) {
				IoUtils.copy(object, fileOutputStream);
			}
			return file;
		}
		catch (Exception e) {
			throw new OssException(e, "download file error bucket:%s,path:%s", this.bucket, relativePath);
		}
	}

	/**
	 * 下载文件到输出流。
	 * @param relativePath 文件相对路径
	 * @param outputStream 输出流
	 */
	public void download(String relativePath, OutputStream outputStream) {
		try {
			GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(relativePath).build();
			try (ResponseInputStream<GetObjectResponse> object = s3Client.getObject(getObjectRequest)) {
				IoUtils.copy(object, outputStream);
			}
			finally {
				if (outputStream != null) {
					outputStream.close();
				}
			}
		}
		catch (Exception e) {
			throw new OssException(e, "download file error bucket:%s,path:%s", this.bucket, relativePath);
		}
	}

	/**
	 * 删除指定路径的文件。
	 * @param path 文件路径
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
	 * 批量删除文件。
	 * @param paths 文件路径集合
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
	 * 检查文件是否存在。
	 * @param path 文件路径
	 * @return 如果文件存在返回 true，否则返回 false
	 */
	public boolean isExist(String path) {
		try {
			HeadObjectRequest headObjectRequest = HeadObjectRequest.builder().bucket(bucket).key(path).build();
			// 发送请求并检查响应
			HeadObjectResponse response = s3Client.headObject(headObjectRequest);
			return response != null; // 如果存在，响应不为 null
		}
		catch (S3Exception e) {
			if (e.awsErrorDetails().errorCode().equals("NoSuchKey")) {
				return false; // 文件不存在
			}
			else {
				throw e; // 其他 S3 错误
			}
		}
	}

	/**
	 * 复制文件。
	 * @param sourcePath 源文件路径
	 * @param toPath 目标文件路径
	 * @return 目标文件访问 URL
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
	 * 获取文件下载 URL。
	 * @param relativePath 文件相对路径
	 * @return 文件下载 URL
	 */
	public String getDownloadUrl(String relativePath) {
		return pathModifier.getDownloadUrl(domain, bucket, downloadPrefix, relativePath);
	}

	/**
	 * 获取 S3 客户端实例。
	 * @return S3 客户端实例
	 */
	public S3Client getS3Client() {
		return s3Client;
	}

	/**
	 * 销毁客户端实例。 释放 S3 客户端资源。
	 * @throws Exception 如果销毁过程中发生错误
	 */
	@Override
	public void destroy() throws Exception {
		if (s3Client != null) {
			s3Client.close();
		}
	}

}
