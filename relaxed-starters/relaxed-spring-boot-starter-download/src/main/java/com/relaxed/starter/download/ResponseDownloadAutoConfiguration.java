package com.relaxed.starter.download;

import com.relaxed.starter.download.annotation.ResponseDownload;
import com.relaxed.starter.download.aop.ResponseDownloadReturnValueHandler;
import com.relaxed.starter.download.domain.DownloadModel;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import com.relaxed.starter.download.annotation.ResponseDownload;
import com.relaxed.starter.download.domain.DownloadModel;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 响应下载自动配置类 用于配置Spring MVC的返回值处理器，支持文件下载功能
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ResponseDownloadAutoConfiguration {

	private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	private final ResponseDownloadReturnValueHandler responseDownloadReturnValueHandler;

	/**
	 * 配置Spring MVC的返回值处理器 将文件下载处理器添加到处理器链的最前面
	 */
	@PostConstruct
	public void setReturnValueHandlers() {
		List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter
				.getReturnValueHandlers();

		List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
		newHandlers.add(responseDownloadReturnValueHandler);
		assert returnValueHandlers != null;
		newHandlers.addAll(returnValueHandlers);
		requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
	}

	/**
	 * 下载响应open api调整
	 * @return doc响应自定义
	 */
	@Bean
	@ConditionalOnClass(Operation.class)
	public OperationCustomizer responseDownloadCustomizer() {
		return (Operation operation, HandlerMethod handlerMethod) -> {
			Method method = handlerMethod.getMethod();

			// 检查方法是否有 @ResponseDownload 注解且返回类型为 DownloadModel
			if (method.isAnnotationPresent(ResponseDownload.class)
					&& DownloadModel.class.equals(method.getReturnType())) {

				// 获取原 @Operation 注解
				operation.setDescription(
						"如果HTTP Status Code是200，则为application/octet-stream，返回文件流；否则为application/json，返回错误json信息");
				ApiResponses responses = operation.getResponses();
				if (responses == null) {
					responses = new ApiResponses();
					operation.setResponses(responses);
				}
				// 处理200成功响应

				ApiResponse successResponse = responses.get("200");
				if (successResponse == null) {
					successResponse = new ApiResponse();
					responses.addApiResponse("200", successResponse);
				}
				// 设置响应媒体类型为application/octet-stream
				Content content = new Content();
				MediaType mediaType = new MediaType();
				mediaType.schema(
						new Schema().type("string").format("binary").contentMediaType("application/octet-stream"));
				content.addMediaType("application/octet-stream", mediaType);
				successResponse.setContent(content);
				successResponse.setDescription("操作成功,返回文件流");
			}

			return operation;
		};
	}

}
