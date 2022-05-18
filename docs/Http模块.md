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

    @Override
    protected RequestForm fillRequestParam(RequestForm requestForm) {
        Map<String, Object> paramMap = BeanUtil.beanToMap(this, false, true);
        requestForm.setForm(paramMap);
        //若要发送json请求 需要将此设置为json
        //requestForm.setBody(json);
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
        //1.创建发送者 此处使用默认的 用户可以自己实现
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

### 自定义发送者

customSender 实现`ISender`接口即可

```java
/**
 * @author Yakir
 * @Topic CustomSender
 * @Description
 * @date 2022/5/18 17:55
 * @Version 1.0
 */
public class CustomSender extends HttpSender {

    public CustomSender(String baseUrl) {
        super(baseUrl);

    }

    public CustomSender(String baseUrl, RequestHeaderGenerate headerGenerate) {
        super(baseUrl, headerGenerate);
    }

    /**
     * 此方法 可以构建自己的http client
     * @author yakir
     * @date 2022/5/18 18:02
     * @param requestUrl
     * @param requestForm
     * @return T
     */
    @Override
    protected <T extends IHttpResponse> T doExecute(String requestUrl, RequestForm requestForm) {
        HttpRequest httpRequest = buildHttpRequest(requestUrl, requestForm);
        Map<String, String> headMap = super.headerGenerate().generate();
        fillHttpRequestHeader(httpRequest, headMap);
        HttpResponse httpResponse = httpRequest.execute();
        if (httpResponse.getStatus() != 200) {
            throw new HttpException("request failed -{}", httpResponse.body());
        }
        HttpResponseWrapper responseWrapper = new HttpResponseWrapper();
        responseWrapper.setCharset(httpResponse.charset());
        responseWrapper.setBodyBytes(httpResponse.bodyBytes());
        return (T) responseWrapper;
    }

}


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

