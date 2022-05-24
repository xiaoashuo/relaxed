package com.relaxed.common.swagger;

import cn.hutool.core.util.StrUtil;
import com.relaxed.common.swagger.builder.DocketBuildHelper;
import com.relaxed.common.swagger.builder.MultiRequestHandlerSelectors;
import com.relaxed.common.swagger.constant.SwaggerConstants;
import com.relaxed.common.swagger.property.SecuritySchemaEnum;
import com.relaxed.common.swagger.property.SwaggerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Yakir
 * @Topic SwaggerAutoConfiguration
 * @Description document url
 * http://springfox.github.io/springfox/docs/current/#quick-start-guides
 * @date 2021/7/8 12:51
 * @Version 1.0
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerAutoConfiguration {

	private final SwaggerProperties swaggerProperties;

	@Bean
	@ConditionalOnMissingBean
	public Docket api() {
		DocketBuildHelper helper = new DocketBuildHelper(swaggerProperties);
		// 1. 文档信息构建
		Docket docket = new Docket(swaggerProperties.getDocumentationType().getType()).host(swaggerProperties.getHost())
				.groupName(swaggerProperties.getGroupName()).apiInfo(helper.apiInfo())
				.enable(swaggerProperties.getEnabled());
		// 2.安全配置
		docket.securitySchemes(helper.securitySchema()).securityContexts(helper.securityContext());
		// 3.selection by requestHandler and by paths
		String basePackage = swaggerProperties.getBasePackage();
		ApiSelectorBuilder select = docket.select();
		if (StrUtil.isEmpty(basePackage)) {
			select.apis(MultiRequestHandlerSelectors.any());
		}
		else {
			select.apis(MultiRequestHandlerSelectors.basePackage(basePackage));
		}
		select.paths(helper.paths()).build();
		return docket;
	}

}
