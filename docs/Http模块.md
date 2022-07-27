# Http模块

## 前言

第三方接口成套对接，一般变动的仅仅为path和参数部分。

每次开发一个单独接口，都要经过下述流程

创建参数->打开链接->执行请求->关闭链接->转换响应。

针对上述操作重复性繁多，故此封装一层。

将打开链接 -执行请求-关闭链接进行了一层包装，

只要定义好参数+响应=>完成接口对接。

## 使用示例

### 引入依赖

```xml
   <dependency>
            <groupId>com.lovecyy</groupId>
            <artifactId>relaxed-common-http</artifactId>
            <version>${version}</version>
   </dependency>
```



### 1、创建响应实体

FileResponse  继承`BaseResponse`

```java
@Data
public class FileResponse extends BaseResponse {
    private String fileContent;
}

```

### 2、创建请求实体

FileRequest 继承`AbstractRequest<R>`

```java
@Data
public class FileRequest extends AbstractRequest<FileResponse> {
    private String name;

    @Override
    public String getUrl(String baseUrl) {
        return baseUrl+"/file/create";
    }

    //默认以form形式传参 若需要以json传参 需按如下要求重写此方法
    @Override
    protected RequestForm fillRequestParam(RequestForm requestForm) {
       requestForm.setBody(toJsonRequestParam());
        return requestForm;
    }
   //若是下载请求 此处需要返回true,并实现convertToResponse方法
   // @Override
   //public boolean isDownloadRequest() {
   //	return true;
   //}

   // @Override
   //	public FileResponse convertToResponse(IHttpResponse response) {
   //		byte[] fileStream = response.bodyBytes();
   //		FileResponse fileResponse = new FileResponse();
   //		fileResponse.setCode(200);
   //		fileResponse.setMessage("success");
   //		fileResponse.setFileContent(Base64.encode(fileStream));
   //		return fileResponse;
   //	}
}
```

### 3、执行单元测试

```java
	@Test
	public void testUpload() {
        //1.创建发送者 此处使用默认的 用户可以自己定义。只需要实现ISenderr接口即可
        HttpSender httpSender = new HttpSender(baseUrl, requestHeaderGenerate);
        //2.创建请求参数
		FileRequest request = new FileRequest();
		request.setChannelNo("test");
		request.setRequestMethod(RequestMethod.GET);
		request.setName("张三拿文件来了");
		log.info("请求参数:{}", request);
        //3.发送请求->获得响应
		FileResponse response = httpSender.send(request);
		log.info("请求响应:{}", response);
	}
```

## 附:

### 定义通知器,拦截器

#### 1.通知器`RequestResultNotifier`

```java
//接收事件通知
RequestResultNotifier requestResultNotifier = reqReceiveEvent -> log.info("event {}", reqReceiveEvent);

```

#### 2. 拦截器`RequestInterceptor.java`

```java

		RequestInterceptor<HttpRequest, HttpResponse> requestInterceptor = new RequestInterceptor<HttpRequest, HttpResponse>() {
			@Override
			public HttpRequest requestInterceptor(HttpRequest request, RequestForm requestForm,
					Map<String, Object> context) {
				request.setConnectionTimeout(10000);
				request.setReadTimeout(10000);
				return request;
			}

			@Override
			public HttpResponse responseInterceptor(HttpRequest request, HttpResponse response,
					Map<String, Object> context) {
				return response;
			}
		};
```

#### 3.注册发送器

```java
//指定 通知器 拦截器
HttpSender httpSender = new HttpSender(baseUrl, requestHeaderProvider, requestResultNotifier,
				requestInterceptor);
```

### 记录请求日志

1.定义事件监听者

```java
@Slf4j
@Component
public class ReqReceiveListener {

	/**
	 * 参数为Object类型时，所有事件都会监听到 参数为指定类型事件时，该参数类型事件或者其子事件（子类）都可以接收到
	 */
        @Async(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
	@EventListener
	public void event(ReqReceiveEvent event) {
		log.info("接收事件:{}", event);
	}

}
```

ReqReceiveEvent 参数解析

```java
@Setter
@Getter
@ToString
public class ReqReceiveEvent extends ApplicationEvent {

	/**
	 * 渠道
	 */
	private final String channel;

	/**
	 * 请求地址
	 */
	private final String url;

	/**
	 * 原始请求带参数
	 */
	private final IRequest request;

	/**
	 * 转换后的请求参数
	 */
	private final RequestForm requestForm;

	/**
	 * 请求上下文
	 */
	private final Map<String, Object> context;

	/**
	 * 转换后的响应
	 */
	private final IResponse response;

	/**
	 * 异常信息
	 */
	private Throwable throwable;

	/**
	 * 开始时间
	 */
	private Long startTime;

	/**
	 * 结束时间
	 */
	private Long endTime;
}

```

