# OSS使用

## 使用方式

### 1、引入坐标

```xml
  <dependency>
    <groupId>com.lovecyy</groupId>
    <artifactId>relaxed-spring-boot-starter-s3</artifactId>
    <version>${version}</version>
</dependency>
```

### 2、application.yml

> 此处以COS为列 ,其余对象存储 只需要修改endpoint 和region即可.

```yml
relaxed:
  oss:
    bucket: bucket
    accessKey:
    accessSecret:
    endpoint: https://cos.ap-shanghai.myqcloud.com
    pathStyleAccess: false
    region: ap-shanghai
```

### 3、上传操作

```java
	String relativePath = "tx/test3.jpg";
		FileInputStream fileInputStream = new FileInputStream(
				ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + TEST_OBJECT_NAME));
		StreamMeta streamMeta = StreamMeta.convertToByteStreamMeta(fileInputStream);
		String downloadUrl = ossClient.upload(streamMeta.getInputStream(), streamMeta.getSize(), relativePath);
		log.info("上传结果:{}", downloadUrl);
		Assert.state(ossClient.getDownloadUrl(relativePath).equals(downloadUrl), "下载地址不一致");
```

### 4、复制操作

```java
	@SneakyThrows
	@Test
	void txCopy() {
		String sourcePath = "tx/test3.jpg";
		String destPath = "tx/test4.jpg";
		String copyDownloadUrl = ossClient.copy(sourcePath, destPath);
		// String copyDownloadUrl = ossClient.copy("test", "img/test3.jpg",
		// destBucketName, destPath);
		Assert.state(ossClient.getDownloadUrl(destPath).equals(copyDownloadUrl), "Copy下载地址不一致");
		log.info("copy结果:{}", copyDownloadUrl);
	}
```

### 5、删除操作

```java
	@SneakyThrows
	@Test
	void txDelete() {
		// 单条删除
		// String relativePath = "tx/test4.jpg";
		// ossClient.delete(relativePath);

		// 批量删除
		Set<String> paths = new HashSet<>();
		paths.add("tx/test3.jpg");
		paths.add("tx/test4.jpg");
		ossClient.batchDelete(paths);
	}
```

### 6、下载操作

```java
	@Autowired
private OssClient ossClient;

@Test
public void transferDir() throws IOException {
        String relativePath = "test/test.txt";
        byte[] result = ossClient.download(relativePath);
        }
```

