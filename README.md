# Relaxed
## 简介

Relaxed项目是一个基础的通用脚手架，旨在为项目快速开发提供一系列的基础能力，方便使用者根据项目需求快速进行功能拓展。

+ Github地址:  https://github.com/xiaoashuo/relaxed

+ 文档地址: https://xiaoashuo.github.io/relaxed-docs/zh/guide/

## 功能介绍

| 模块概要     | 模块路径                              |
| ------------ | ------------------------------------- |
| 缓存模块     | relaxed-spring-boot-starter-cache     |
| 数据权限     | relaxed-spring-boot-starter-datascope |
| 钉钉通知     | relaxed-spring-boot-starter-dingtalk  |
| 通用下载     | relaxed-spring-boot-starter-download  |
| excel模块    | relaxed-spring-boot-starter-easyexcel |
| 异常处理     | relaxed-spring-boot-starter-exception |
| 任务模块     | relaxed-spring-boot-starter-job       |
| 日志模块     | relaxed-spring-boot-starter-log       |
| 邮件模块     | relaxed-spring-boot-starter-mail      |
| 对象存储     | relaxed-spring-boot-starter-oss       |
| 通讯加密     | relaxed-spring-boot-starter-secret    |
| SFTP         | relaxed-spring-boot-starter-sftp      |
| 企业微信通知 | relaxed-spring-boot-starter-wechat    |
| Web模块      | relaxed-spring-boot-starter-web       |

## 安装方式

### 1、拉取项目

```shell
git clone https://github.com/xiaoashuo/relaxed.git
```

### 2、本地安装

```shell
mvn install
```

## 使用说明

> 此处以日志模块为示例

### 1、引入坐标

```xml
<!--引入对应坐标-->     
<dependency>
                <groupId>cn.lovecyy</groupId>
                <artifactId>relaxed-spring-boot-starter-log</artifactId>
                <version>${revision}</version>
</dependency>
```

### 2、日志处理类实现

```java

@Slf4j
@Component
public class AccessLogHandle implements AccessLogHandler<AccessLog> {
    public static final String TRACE_ID = "traceId";

    @Override
    public AccessLog beforeRequest(HttpServletRequest request, LogAccessRule logAccessRule) {
        AccessLog paramMap = new AccessLog();
        Object matchingPatternAttr = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String matchingPattern = matchingPatternAttr == null ? "" : String.valueOf(matchingPatternAttr);
        String uri = URLUtil.getPath(request.getRequestURI());
        paramMap.setTraceId(MDC.get(TRACE_ID));
        paramMap.setUri(uri);
        paramMap.setMethod(request.getMethod());
        paramMap.setIp(IpUtils.getIpAddr(request));
        paramMap.setMatchingPattern(matchingPattern);
        paramMap.setUserAgent(request.getHeader("user-agent"));
        paramMap.setCreatedTime(LocalDateTime.now());
        return paramMap;
    }

    @Override
    public void afterRequest(AccessLog buildParam, HttpServletRequest request, HttpServletResponse response,
                             Long executionTime, Throwable myThrowable, LogAccessRule logAccessRule) {
        buildParam.setUpdatedTime(LocalDateTime.now());
        buildParam.setTime(executionTime);
        buildParam.setHttpStatus(response.getStatus());
        buildParam.setErrorMsg(Optional.ofNullable(myThrowable).map(Throwable::getMessage).orElse(""));
        LogAccessRule.RecordOption recordOption = logAccessRule.getRecordOption();
        LogAccessRule.FieldFilter fieldFilter = logAccessRule.getFieldFilter();

        // 记录请求
        if (recordOption.isIncludeRequest()) {
            String matchRequestKey = fieldFilter.getMatchRequestKey();
            // 获取普通参数
            String params = getParams(request, matchRequestKey, fieldFilter.getReplaceText());
            buildParam.setReqParams(params);
            // 非文件上传请求，记录body，用户改密时不记录body
            // TODO 使用注解控制此次请求是否记录body，更方便个性化定制
            if (!isMultipartContent(request)) {
                buildParam.setReqBody(getRequestBody(request, matchRequestKey, fieldFilter.getReplaceText()));

            }
        }
        // 记录响应
        if (recordOption.isIncludeResponse()) {
            String matchResponseKey = fieldFilter.getMatchResponseKey();
            buildParam.setResult(getResponseBody(request, response, matchResponseKey, fieldFilter.getReplaceText()));
        }

        String header = getHeader(request, "Accept", "Content-Type");
        log.info("\n请求头:\n{}\n请求记录:\n{}", header, convertToAccessLogStr(buildParam));

    }

    /**
     * 判断是否是multipart/form-data请求
     * @param request 请求信息
     * @return 是否是multipart/form-data请求
     */
    public boolean isMultipartContent(HttpServletRequest request) {
        // 获取Content-Type
        String contentType = request.getContentType();
        return (contentType != null) && (contentType.toLowerCase().startsWith("multipart/"));
    }

    public String convertToAccessLogStr(AccessLog accessLog) {
        String LINE_SEPARATOR = System.lineSeparator();
        StringBuilder reqInfo = new StringBuilder().append("traceId:").append(accessLog.getTraceId())
                .append(LINE_SEPARATOR).append("userId:").append(accessLog.getUserId()).append(LINE_SEPARATOR)
                .append("userName:").append(accessLog.getUsername()).append(LINE_SEPARATOR).append("uri:")
                .append(accessLog.getUri()).append(LINE_SEPARATOR).append("matchingPattern:")
                .append(accessLog.getMatchingPattern()).append(LINE_SEPARATOR).append("method:")
                .append(accessLog.getMethod()).append(LINE_SEPARATOR).append("userAgent:")
                .append(accessLog.getUserAgent()).append(LINE_SEPARATOR).append("reqParams:")
                .append(accessLog.getReqParams()).append(LINE_SEPARATOR).append("reqBody:")
                .append(accessLog.getReqBody()).append(LINE_SEPARATOR).append("httpStatus:")
                .append(accessLog.getHttpStatus()).append(LINE_SEPARATOR).append("result:")
                .append(accessLog.getResult()).append(LINE_SEPARATOR).append("errorMsg:")
                .append(accessLog.getErrorMsg()).append(LINE_SEPARATOR).append("time:").append(accessLog.getTime())
                .append(LINE_SEPARATOR).append("createdTime:").append(accessLog.getCreatedTime())
                .append(LINE_SEPARATOR);
        return reqInfo.toString();
    }
}
```



### 3、application.yml

```yml
relaxed:
  log:
    access:
      enabled: true
      #过滤器顺序(可选)
      order: -10
      #url规则列表(可选)
      urlRules:
        #匹配路径
        - urlPattern: /test/log/**/form
          #日志选项
          recordOption:
            #是否忽略 默认为false
            ignore: false
            #记录请求 默认true
            includeRequest: true
            #记录响应 默认true
            includeResponse: true
          #字段级别过滤规则 
          #此示例过滤请求username 响应password
          #请求参数:{"username":"zs","password":"12"}
          #响应参数:{"username":"zs","password":"12"}
          fieldFilter:
            #匹配请求参数路径名,多个以,分隔。
            matchRequestKey: username,password
            #匹配响应参数路径名,多个以,分隔
            matchResponseKey: password
            #替换文本 若内容为空 则不替换
            replaceText: none
```

