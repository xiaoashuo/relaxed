package com.relaxed.common.oss.s3;


import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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

	private String downloadPrefix;

	private  ObjectCannedACL acl;

	private S3Client s3Client;

	public void setS3Client(S3Client s3Client) {
		this.s3Client = s3Client;
	}

	public S3Client getS3Client() {
		return s3Client;
	}

	/**
	 * 上传文件 使用默认存储桶目录
	 * @author yakir
	 * @date 2021/12/2 15:34
	 * @param inputStream
	 * @param size
	 * @param relativePath
	 * @return java.lang.String
	 */
	public String upload(InputStream inputStream,Long size,String relativePath){
		return upload(inputStream,size,bucket,relativePath,acl);
	}

	public String upload(InputStream inputStream,Long size,String bucketName,String relativePath){
		return upload(inputStream,size,bucketName,relativePath,acl);
	}
	/**
	 * 上传文件
	 * @author yakir
	 * @date 2021/11/26 15:34
	 * @param inputStream 输入流
	 * @param size  流大小
	 * @param bucketName  桶名称 等同于在当前桶下创建一级目录
	 * @param relativePath 文件路径  未关联根路径的
	 * @return java.lang.String
	 */
	public String upload(InputStream inputStream, Long size, String bucketName, String relativePath, ObjectCannedACL acl){
		relativePath=removeDefaultPrefix(relativePath);
		PutObjectRequest.Builder builder = PutObjectRequest.builder().bucket(bucketName).key(relativePath);
		if (acl!=null){
			builder.acl(acl);
		}
		//返回eTag
		getS3Client().putObject(builder.build(), RequestBody.fromInputStream(inputStream, size));
		return getDownloadUrl(bucketName, relativePath);
	}

	private String removeDefaultPrefix(String source){
		return removePrefix(OssConstants.SLASH,source);
	}
	private String removePrefix(String prefix,String source){
		if (source.startsWith(prefix)){
			return source.substring(prefix.length());
		}
		return source;
	}



	public List<String> list(String bucketName){
		return list(bucketName,null,null);

	}
	public List<String> list(String bucketName,String marker,Integer maxKeys){
		ListObjectsRequest listObjects = ListObjectsRequest
				.builder()
				.bucket(bucket)
				.prefix(bucketName)
                .marker(marker)
				.maxKeys(maxKeys)
				.build();
		ListObjectsResponse res = getS3Client().listObjects(listObjects);
		List<S3Object> contents = res.contents();
		List<String> paths=new ArrayList<>();
		for (S3Object content : contents) {
			String key = content.key();
			String downloadUrl = getDownloadUrl(key);
			paths.add(downloadUrl);
		}
		return paths;

	}

	/**
	 * 删除单个文件
	 * @author yakir
	 * @date 2021/12/1 18:19
	 * @param path
	 */
	public void delete(String path){
	   delete(bucket,path);
	}
	public void delete(String bucketName,String path){
		if (!StringUtils.hasText(path)){
			return;
		}
        getS3Client().deleteObject(builder -> builder.bucket(bucketName).key(removeDefaultPrefix(path)));
	}
    /**
     * 批量删除使用默认桶
     * @author yakir
     * @date 2021/12/3 16:12
     * @param paths
     */
	public void batchDelete(Set<String> paths){
	   batchDelete(bucket,paths);
	}

	/**
	 * 批量删除
	 * @author yakir
	 * @date 2021/12/2 13:58
	 * @param paths
	 */
	public void batchDelete(String bucketName,Set<String> paths){
		if (CollectionUtils.isEmpty(paths)){
			return;
		}
		ArrayList<ObjectIdentifier> keys = new ArrayList<>();
		for (String path : paths) {
			ObjectIdentifier   objectId = ObjectIdentifier.builder()
					.key(getPathWithBucket(bucketName,removeDefaultPrefix(path)))
					.build();
			keys.add(objectId);
		}

		// Delete multiple objects in one request.
		Delete del = Delete.builder()
				.objects(keys)
				.build();
		DeleteObjectsRequest multiObjectDeleteRequest = DeleteObjectsRequest.builder()
				.bucket(bucketName)
				.delete(del)
				.build();
		getS3Client().deleteObjects(multiObjectDeleteRequest);
	}



	/**
	 * copy 对象
	 * @author yakir
	 * @date 2021/12/3 15:53
	 * @param sourceBucketName
	 * @param sourcePath
	 * @param toBucketName
	 * @param toPath
	 * @return java.lang.String 目标下载地址
	 */
	public String copy(String sourceBucketName,String sourcePath,String toBucketName,String toPath){
		CopyObjectRequest copyObjRequest =  CopyObjectRequest
				.builder()
				.sourceBucket(bucket)
				.sourceKey(getPathWithBucket(sourceBucketName,removeDefaultPrefix(sourcePath)))
				.destinationBucket(toBucketName)
				.destinationKey(removeDefaultPrefix(toPath)).build();

		CopyObjectResponse copyObjectResponse = getS3Client().copyObject(copyObjRequest);
		copyObjectResponse.copyObjectResult();
		return getDownloadUrl(toBucketName, toPath);

	}


	private String getPathWithBucket(String bucketName,String relativePath){
		Assert.hasText(relativePath, "path must not be empty");
		if (relativePath.startsWith(OssConstants.SLASH)) {
			relativePath = relativePath.substring(1);
		}
		return bucketName +"/"+ relativePath;
	}

	/**
	 * 获取 绝对路径 的下载url
	 * @author lingting 2021-05-12 18:50
	 */
	public String getDownloadUrl(String bucketName,String relativePath) {
		return String.format("%s/%s/%s", downloadPrefix, bucketName,relativePath);
	}
	public String getDownloadUrl(String relativePath) {
		return String.format("%s/%s", downloadPrefix,relativePath);
	}

	@Override
	public void destroy() throws Exception {
		if (s3Client != null) {
			s3Client.close();
		}
	}
}
