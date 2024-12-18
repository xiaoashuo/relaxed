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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Yakir
 * @Topic OssClientTest
 * @Description
 * @date 2021/11/26 9:50
 * @Version 1.0
 */
@Slf4j
public class AliOssClientTest {

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
		properties.setEndpoint("https://oss-cn-beijing.aliyuncs.com");
		properties.setRegion("oss-cn-beijing");
	}

	@SneakyThrows
	@Test
	void aliUpload() {
		String relativePath = "img/test3.jpg";
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
		String sourcePath = "test1.jpg";
		String destPath = "test4.jpg";
		String copyDownloadUrl = ossClient.copy(sourcePath, destPath);
		// String copyDownloadUrl = ossClient.copy("test", "img/test3.jpg",
		// destBucketName, destPath);
		Assert.state(ossClient.getDownloadUrl(destPath).equals(copyDownloadUrl), "Copy下载地址不一致");
		log.info("copy结果:{}", copyDownloadUrl);
	}

	@SneakyThrows
	@Test
	void aliDelete() {
		// 单条删除
		String relativePath = "dest/test4.jpg";
		ossClient.delete(relativePath);

		// 批量删除
		Set<String> paths = new HashSet<>();
		paths.add("test/img/test4.jpg");
		paths.add("test/img/test13.jpg");
		ossClient.batchDelete(paths);
	}

	@SneakyThrows
	@Test
	void aliList() {
		String bucketName = "test";
		List<String> paths = ossClient.list(bucketName);
		log.info("结果数组:{}", paths);
	}

}
