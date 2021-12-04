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


	private final   String endpoint;

	private final  String region;

	private final  String accessKey;

	private  final String accessSecret;

	private final  String bucket;
	private final String domain;
	private final String root;
	/**
	 * true path-style nginx 反向代理和S3默认支持 pathStyle {http://endpoint/bucketname} false
	 * supports virtual-hosted-style 阿里云等需要配置为 virtual-hosted-style
	 * 模式{http://bucketname.endpoint}
	 */
	private final boolean pathStyleAccess;

	private final String downloadPrefix;

	private final ObjectCannedACL acl;

	private final S3Client s3Client;

	public   OssClient(OssClientBuilder ossClientBuilder){
		//同步OssClientBuilder信息
		this.endpoint=ossClientBuilder.getEndpoint();
		this.region=ossClientBuilder.getRegion();
		this.accessKey=ossClientBuilder.getAccessKey();
		this.accessSecret=ossClientBuilder.getAccessSecret();
		this.bucket=ossClientBuilder.getBucket();
		this.domain=ossClientBuilder.getDomain();
		this.pathStyleAccess=ossClientBuilder.getPathStyleAccess();
		this.downloadPrefix=ossClientBuilder.getProxyUrl();
		this.root=ossClientBuilder.getRootPath();
		this.acl=ossClientBuilder.getAcl();
		this.s3Client=ossClientBuilder.getS3Client();
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
		PutObjectRequest.Builder builder = PutObjectRequest.builder().bucket(bucketName).key(relativePath);
		if (acl!=null){
			builder.acl(acl);
		}
		//返回eTag
		getS3Client().putObject(builder.build(), RequestBody.fromInputStream(inputStream, size));
		return getDownloadUrl(bucketName, relativePath);
	}





	public List<String> list(String bucketName){
		return list(bucketName,null,null);

	}
	/**
	 * 查询列表
	 * @author yakir
	 * @date 2021/12/1 18:19
	 * @param bucketName 桶名称
	 * @param marker
	 * @param maxKeys
	 */
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
			String downloadUrl = String.format("%s/%s", downloadPrefix,key);
			paths.add(downloadUrl);
		}
		return paths;

	}


	/**
	 * 删除单个文件
	 * @author yakir
	 * @date 2021/12/1 18:19
	 * @param bucketName 桶名称
	 * @param path
	 */
	public void delete(String path){
		if (!StringUtils.hasText(path)){
			return;
		}
        getS3Client().deleteObject(builder -> builder.bucket(bucket).key(path));
	}


	/**
	 * 批量删除
	 * @author yakir
	 * @date 2021/12/2 13:58
	 * @param paths 相对于bucket命名空间下的完整路径
	 *                 <p>
	 *                  eg: bucket test-oss    文件 img/test.jpg
	 *                      path: img/test.jpg
	 *                 </p>
	 * @param paths
	 */
	public void batchDelete(Set<String> paths){
		if (CollectionUtils.isEmpty(paths)){
			return;
		}
		ArrayList<ObjectIdentifier> keys = new ArrayList<>();
		for (String path : paths) {
			ObjectIdentifier   objectId = ObjectIdentifier.builder()
					.key(path)
					.build();
			keys.add(objectId);
		}

		// Delete multiple objects in one request.
		Delete del = Delete.builder()
				.objects(keys)
				.build();
		DeleteObjectsRequest multiObjectDeleteRequest = DeleteObjectsRequest.builder()
				//指定命名空间
				.bucket(bucket)
				.delete(del)
				.build();
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
	public String copy(String sourcePath, String toPath){
		CopyObjectRequest copyObjRequest =  CopyObjectRequest
				.builder()
				//此处一定要指定原始命名空间
				.sourceBucket(bucket)
				.sourceKey(sourcePath)
				.destinationBucket(bucket)
				.destinationKey(toPath).build();

		CopyObjectResponse copyObjectResponse = getS3Client().copyObject(copyObjRequest);
		copyObjectResponse.copyObjectResult();
		return getDownloadUrl(toPath);

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
		if (StringUtils.hasText(domain)){
			return String.format("%s/%s/%s", downloadPrefix, bucket,relativePath);
		}else{
			return String.format("%s/%s", downloadPrefix,relativePath);
		}
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
