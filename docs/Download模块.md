# Download模块

## 简言

由于每个项目基本都有导出功能，虽然实现不复杂，但过于繁琐，

因此将导出部分单独抽成注解形式。以方便使用。

## 使用方式

### 1、引入坐标

```pom
      <dependency>
            <groupId>com.lovecyy</groupId>
            <artifactId>relaxed-spring-boot-starter-download</artifactId>
            <version>${version}</version>
      </dependency>
```

### 2、创建controller

> 方法必须使用`@ResponseDownload`标注+返回值为`DownloadModel`

```java
 
	@ResponseDownload(channel = DownTypeEnum.LOCAL)
	@GetMapping("local")
	public DownloadModel local() {
		DownloadModel downloadModel = new DownloadModel("D:\\test","123.jpg");
		return downloadModel;
	}
```

参数解析:

#### @ResponseDownload

+ channel 渠道LOCAL,SFTP,OSS,OTHER(自定义)默认为LOCAL

​    若要支持 SFTP(参考SFTP模块文档) ，OSS（参考SFTP模块文档） 需要引入对应依赖

+ customHandler 当channel为OTHER时必须指定，且被SPRING容器管理，及注册成BEAN
+ inline 是否直接展示 默认为false
+ contentType 如果未指定，会尝试获取 如果尝试获取失败，则默认application/octet-stream
+ charset  如果下载包含中文的文本文件出现乱码，可以尝试指定编码 默认UTF-8
+ headers 响应头 数目长度必须为偶数 基数位为key 偶数位 为value

#### DownloadModel

+ fileName 文件名称 带后缀 123.png
+ fileType 文件类型 png
+ parentPath 文件父级路径 绝对路径 eg /test/123.png 此处就是/test
+ separator  文件路径分隔符 默认为系统分隔符 
+ fullFilePath 文件全路径  eg /test/123.png
+ extra 额外参数 自定义处理器时可能用的到
+ downloadCallback 后置处理器 执行完之后可以做一些收尾工作

3、开始使用

直接访问地址 即可 下载附件。