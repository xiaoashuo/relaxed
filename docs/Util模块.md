# Util模块

## 引入依赖

```xml
   <dependency>
            <groupId>com.lovecyy</groupId>
            <artifactId>relaxed-common-util</artifactId>
            <version>${version}</version>
   </dependency>
```



## 文件工具类(FileUtils)

> 默认处理器类型为local，本地文件存储。支持覆写，参考拓展文件处理器，每种处理器类型对应一个处理器，若存在多个注册，则以最后一次注册为主。

### 1.文件上传

```java
	@Test
	void localUpload() {
        //根路径
		String rootPath = "D:\\other\\100000";
        //相对路径
		String relativePath = "file";
        //上传文件
		String uploadFilePath = "D:\\other\\100000\\jieqingzhengming_131331131313.pdf";
		FileMultipartFile fileMultipartFile = new FileMultipartFile("file", new File(uploadFilePath));
        //执行上传
		FileMeta fileMeta = FileUtils.upload(rootPath, relativePath, fileMultipartFile,
				FileConfig.create().splitDate(true));
		log.info("upload  successfully,result:[{}] ,相对路径[{}],完整路径[{}]", JSONUtil.toJsonStr(fileMeta),
				fileMeta.getRelativeFilePath(), fileMeta.getFullFilePath());
	}
```

### 2.文件删除

```java
	@Test
	void localDelete() {
        //根路径
		String rootPath = "D:\\other\\100000";
        //相对文件路径
		String relativePath = "\\file\\20230517\\AN0PbFjifzsWsDdNTNohv.pdf";
        //执行删除 若当前目录无文件 则会自动清除最近一级空目录
		boolean deleted = FileUtils.delete(rootPath, relativePath);
		log.info("deleted  successfully,result:[{}]", deleted);
	}
```

### 3.文件下载

```java
	@SneakyThrows
	@Test
	void localDownload() {
        //根路径
		String rootPath = "D:\\other\\100000";
        //相对路径
		String relativePath = "\\file\\20230517\\k03KkgJPQdXjCFtmTvhG_.pdf";
        //写入到流  默认会关闭输出流
		FileUtils.writeToStream(rootPath, relativePath,new FileOutputStream(new File("D:\\other\\100000\\file\\"+ IdUtil.getSnowflakeNextIdStr()+".pdf")));
        //byte[] content = FileUtils.downloadByte(rootPath, relativePath);
		log.info("download  successfully");
	}
```

### 扩展操作

#### 配置属性(FileConfig)

| 属性              | 类型     | 描述                                                  | 示例                                                         |
| ----------------- | -------- | ----------------------------------------------------- | ------------------------------------------------------------ |
| maxFilenameLength | int      | 最大文件名长度                                        | 100                                                          |
| maxSize           | long     | 最大文件大小                                          | 52428800B（字节）                                            |
| splitDate         | boolean  | 是否按日期切分，若为true 则会在目录后拼接当前日期目录 | true /mnt/20220303<br />false /mnt                           |
| separator         | string   | 路径分隔符                                            | 路径分隔符                                                   |
| allowedExtension  | array    | 允许上传文件扩展名                                    | ["mp4","mp3"]                                                |
| fileNameConverter | function | 文件名称转换器                                        | new FileNameConverter() {    @Override    public String extractFileName(String originalFilename) {       return "新文件名";    } } |



#### 拓展文件处理器

接下来以扩展oss为例，说明

##### 1.实现`FileHandler`接口

```java
public class OssFileHandler implements FileHandler {

    private OssClient ossClient= SpringUtils.getBean(OssClient.class);
    @Override
    public String supportType() {
        return "oss";
    }

    @SneakyThrows
    @Override
    public String upload(String dirPath, String filename, String separator, MultipartFile file) {
        ossClient.upload(file.getInputStream(),file.getSize(),dirPath+separator+filename);
        String fileId = IdUtil.getSnowflakeNextId() + "";
        return fileId;
    }

    @Override
    public boolean delete(String rootPath, String relativePath) {
        ossClient.delete(rootPath+relativePath);
        return true;
    }

    @Override
    public void writeToStream(String rootPath, String relativePath, OutputStream outputStream) {
        byte[] content = downloadByte(rootPath, relativePath);
        IoUtil.write(outputStream,true,content);
    }

    @Override
    public byte[] downloadByte(String rootPath, String relativePath) {
        return ossClient.download(rootPath+relativePath);
    }
}
```

##### 2.建立相关配置

`resources`下面建立`META-INF/services`文件夹

创建文件以`com.relaxed.common.core.util.file.FileHandler`命名

文件内容如下

```java
com.xxx.config.file.OssFileHandler
```

##### 3.开始使用

> 此处需要注意，在每个方法的第一个位置指定处理类型。默认处理类型为local

文件上传

```java
@Test
	void fileOssUpload() {
		String rootPath = "1d";
		String relativePath = "test";
		String uploadFilePath = "D:\\other\\100000\\jieqingzhengming_131331131313.pdf";
		FileMultipartFile fileMultipartFile = new FileMultipartFile("file", new File(uploadFilePath));
		FileMeta fileMeta = FileUtils.upload("oss",rootPath, relativePath, fileMultipartFile,
				FileConfig.create().splitDate(true).separator("/"));
		log.info("upload  successfully,result:[{}] ,相对路径[{}],完整路径[{}]", JSONUtil.toJsonStr(fileMeta),
				fileMeta.getRelativeFilePath(), fileMeta.getFullFilePath());
	}
```

文件删除

```java
	@Test
	void fileOssDelete() {
		String rootPath = "1d";
		String relativePath = "/test/20230517/pTNBgjKoDkNbGlhM497xi.pdf";
		boolean deleted = FileUtils.delete("oss",rootPath, relativePath);
		log.info("deleted  successfully,result:[{}]", deleted);
	}
```

文件下载

```java
	@SneakyThrows
	@Test
	void fileOssDownload() {
		String rootPath = "1d";
		String relativePath = "/test/20230517rQ-3hIVSz-_RSDl8ItuHv.pdf";
	     FileUtils.writeToStream("oss",rootPath, relativePath,new FileOutputStream(new File("D:\\other\\100000\\file\\"+ IdUtil.getSnowflakeNextIdStr()+".pdf")));
		log.info("download  successfully");
	}
```



