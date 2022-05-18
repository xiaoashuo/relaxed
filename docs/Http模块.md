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
   //	public FileResponse convertToResponse(ResponseWrapper response) {
   //		byte[] fileStream = response.getFileStream();
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
		DefaultSender sender = new DefaultSender();
        //2.创建请求参数
		FileRequest request = new FileRequest();
		request.setChannelNo("test");
		request.setRequestMethod(RequestMethod.GET);
		request.setName("张三拿文件来了");
		log.info("请求参数:{}", request);
        //3.发送请求->获得响应
		FileResponse response = sender.send(request);
		log.info("请求响应:{}", response);
	}
```

## 附:

### 自定义发送者

customSender 实现`ISender`接口即可

```java
/**
 * @author Yakir
 * @Topic DefaultSender
 * @Description
 * @date 2022/5/18 8:34
 * @Version 1.0
 */
@Slf4j
public class DefaultSender implements ISender {

	private final String baseUrl ;

	private final RequestHeaderGenerate headerGenerate;
	public DefaultSender(String baseUrl){
		this.baseUrl=baseUrl;
		this.headerGenerate= () -> null;
	}
	public DefaultSender(String baseUrl,RequestHeaderGenerate headerGenerate){
		this.baseUrl=baseUrl;
		this.headerGenerate=headerGenerate;
	}


	@Override
	public <R extends IResponse> R send(IRequest<R> request) {
		String requestUrl = request.getUrl(baseUrl);
		String channel = request.getChannel();
		RequestForm requestForm = request.generateRequestParam();
		Long startTime = System.currentTimeMillis();
		R response = null;
		Throwable myThrowable = null;
		try {
			HttpRequest httpRequest = buildHttpRequest(requestUrl, requestForm);
			Map<String, String> headMap = headerGenerate.generate();
			fillHttpRequestHeader(httpRequest,headMap);
			ResponseWrapper responseWrapper = new ResponseWrapper();
			HttpResponse execute = httpRequest.execute();
			if (execute.getStatus()!=200){
				throw new HttpException("request failed -{}",execute.body());
			}
			if (request.isDownloadRequest()){
				responseWrapper.setFileStream(execute.bodyBytes());
			}else{
				responseWrapper.setBody(execute.body());
			}
			log.debug("请求渠道:{} url:{} 参数:{} 响应:{}", channel, requestUrl, requestForm, responseWrapper);
			response = request.convertToResponse(responseWrapper);
			return response;

		}
		catch (Throwable throwable) {
			myThrowable = ExceptionUtil.unwrap(throwable);
			throw new RequestException(myThrowable);
		}
		finally {
			// 结束时间
			Long endTime = System.currentTimeMillis();
			publishReqResEvent(channel, requestUrl, request, requestForm, response, myThrowable, startTime, endTime);
		}
	}

	private void fillHttpRequestHeader(HttpRequest httpRequest, Map<String, String> headMap) {
		if (MapUtil.isEmpty(headMap)){
			return;
		}
		for (Map.Entry<String, String> entry : headMap.entrySet()) {
			httpRequest.header(entry.getKey(),entry.getValue());
		}
	}

	private <R extends IResponse> void publishReqResEvent(String channel, String url, IRequest<R> request,
			RequestForm requestForm, R response, Throwable throwable, Long startTime, Long endTime) {
		ReqReceiveEvent event = new ReqReceiveEvent(channel, url, request, requestForm, response, throwable, startTime,
				endTime);
		SpringUtils.publishEvent(event);
	}

	private HttpRequest buildHttpRequest(String requestUrl, RequestForm requestForm) {
		RequestMethod requestMethod = requestForm.getRequestMethod();
		boolean isGet = requestMethod.name().equalsIgnoreCase(RequestMethod.GET.name());
		HttpRequest httpRequest;
		if (isGet) {
			httpRequest = HttpRequest.of(requestUrl);
			httpRequest.setMethod(convertToHuMethod(requestMethod));
			httpRequest.form(requestForm.getForm());
		}
		else {
			httpRequest = HttpRequest.of(requestUrl);
			httpRequest.setMethod(convertToHuMethod(requestMethod));
			String body = requestForm.getBody();
			if (StrUtil.isNotEmpty(body)) {
				// 走json
				httpRequest.body(body);
			}
			else {
				httpRequest.form(requestForm.getForm());
				List<UploadFile> files = requestForm.getFiles();
				Optional.ofNullable(files).ifPresent(uploadFiles -> {
					for (UploadFile uploadFile : uploadFiles) {
						httpRequest.form(uploadFile.getFileName(), uploadFile.getFileData());
					}
				});

			}
		}
		return httpRequest;
	}

	public Method convertToHuMethod(RequestMethod requestMethod) {

		Method method;
		switch (requestMethod) {
		case GET:
			method = Method.GET;
			break;
		case POST:
			method = Method.POST;
			break;
		case PUT:
			method = Method.PUT;
			break;
		case PATCH:
			method = Method.PATCH;
			break;
		case DELETE:
			method = Method.DELETE;
			break;
		default:
			throw new RuntimeException("method not found");

		}
		return method;

	}


	public interface RequestHeaderGenerate{
		Map<String,String> generate();
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

