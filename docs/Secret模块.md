# Secret使用

## 1.引入坐标

```xml
        <dependency>
            <groupId>com.lovecyy</groupId>
            <artifactId>relaxed-spring-boot-starter-secret</artifactId>
            <version>${version}</version>
        </dependency>
```

## 2.定义加解密类

实现接口`SecretHandler`

```java
@Component
public class DefaultParamHandler implements SecretHandler<BaseDto, BaseDto> {

	@Override
	public BaseDto encryptResBody(BaseDto body, ServerHttpRequest request, ServerHttpResponse response) {
		body.setContent("加密数据");
		return body;
	}

	@Override
	public BaseDto decryptReqBody(BaseDto body) {
		body.setTest("ada");
		return body;
	}

}

```

## 3.测试使用

> 方法上加注解优先于类上注解

```java
@RequestMapping("/test")
@RestController
public class TestController {

	@RequestDecrypt
	@ResponseEncrypt
	@PostMapping("/ab")
	public BaseDto test(@RequestBody BaseDto<String> ba) {
		BaseDto baseDto = new BaseDto();
		baseDto.setContent("afsafsasfa");
		return baseDto;
	}

}
```

