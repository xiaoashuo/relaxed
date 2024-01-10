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
                <groupId>com.lovecyy</groupId>
                <artifactId>relaxed-spring-boot-starter-log</artifactId>
                <version>${revision}</version>
</dependency>
```

### 2、日志处理类实现

```java

@Slf4j
@Component
public class AccessLogHandle implements AccessLogHandler<AccessLog> {
	/**
	 * 生产一个日志 根据自己需要格式实现
	 * @param request 请求信息
	 * @param response 响应信息
	 * @param time 执行时长
	 * @param myThrowable 异常信息
	 * @return accessLog
	 */
	@SneakyThrows
	@Override
	public AccessLog prodLog(HttpServletRequest request, HttpServletResponse response, Long time,
			Throwable myThrowable) {
        //编写对应日志记录
        AccessLog log=new AccessLog();
		return log;
	}

	@Override
	public void saveLog(AccessLog accessLog) {
		//此处可以将上述生产出来的log实体 进行异步落库保存，异步实现交由使用者
		// log.info("请求访问日志:{}", accessLog);
	}
}
```



### 3、application.yml

```yml
relaxed:
  log:
    access:
      enabled: true
```

