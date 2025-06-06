package com.relaxed.test.oss.s3;

import com.relaxed.common.oss.s3.OssClient;
import com.relaxed.common.oss.s3.OssClientBuilder;
import com.relaxed.common.oss.s3.OssProperties;
import com.relaxed.common.oss.s3.domain.StreamMeta;
import com.relaxed.common.oss.s3.modifier.DefaultPathModifier;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @author Yakir
 * @Topic OssClientTest
 * @Description
 * @date 2021/11/26 9:50
 * @Version 1.0
 */
@Slf4j
public class HuaweiOssClientTest {

	private OssProperties properties;

	private OssClient ossClient;

	@BeforeEach
	public void before() {
		buildProperties();
		OssClientBuilder ossClientBuilder = new OssClientBuilder();
		ossClientBuilder.region(properties.getRegion());
		ossClientBuilder.accessKey(properties.getAccessKey());
		ossClientBuilder.accessSecret(properties.getAccessSecret());
		ossClientBuilder.bucket(properties.getBucket());
		ossClientBuilder.domain(properties.getDomain());
		ossClientBuilder.pathStyleAccess(properties.getPathStyleAccess());
		ossClientBuilder.endpoint(properties.getEndpoint());
		ossClientBuilder.acl(properties.getAcl());
		ossClientBuilder.pathModifier(new DefaultPathModifier());
		ossClient = ossClientBuilder.build();

	}

	private void buildProperties() {
		properties = new OssProperties();
		properties.setAcl(null);
		properties.setBucket("bucket");
		properties.setAccessKey("access-key");
		properties.setAccessSecret("access-secret");
		properties.setPathStyleAccess(false);
		// 根据自己的需求配置
		properties.setEndpoint("https://obs.cn-east-3.myhuaweicloud.com");
		properties.setRegion("cn-east-3");
	}

	@SneakyThrows
	@Test
	void txUpload() {
		String relativePath = "img/test4.jpg";
		File file = new File("D:\\other\\images\\duola.jpg");
		InputStream stream = new FileInputStream(file);
		StreamMeta streamMeta = StreamMeta.convertToByteStreamMeta(stream);
		String downloadUrl = ossClient.upload(streamMeta.getInputStream(), streamMeta.getSize(), relativePath);
		log.info("上传结果:{}", downloadUrl);
		Assert.state(ossClient.getDownloadUrl(relativePath).equals(downloadUrl), "下载地址不一致");
	}

	@SneakyThrows
	@Test
	void txCopy() {
		String sourcePath = "dest";
		String destPath = "test4.jpg";
		String copyDownloadUrl = ossClient.copy(sourcePath, destPath);
		Assert.state(ossClient.getDownloadUrl(destPath).equals(copyDownloadUrl), "Copy下载地址不一致");
		log.info("copy结果:{}", copyDownloadUrl);
	}

	@SneakyThrows
	@Test
	void txDelete() {
		// 单条删除
		String bucketName = "test";
		String relativePath = "img/test10.jpg";
		ossClient.delete(relativePath);

		// 批量删除
		// Set<String> paths=new HashSet<>();
		// paths.add("img/test2.jpg");
		// paths.add("img/test3.jpg");
		// ossClient.batchDelete(bucketName,paths);
	}

	@SneakyThrows
	@Test
	void txList() {

		String bucketName = "test";
		List<String> paths = ossClient.list(bucketName);
		log.info("结果数组:{}", paths);
	}

}
