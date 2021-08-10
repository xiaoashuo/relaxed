package com.relaxed.common.swagger;

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
import springfox.documentation.spi.service.contexts.SecurityContext;
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
		// 1. 文档信息构建
		Docket docket = new Docket(swaggerProperties.getDocumentationType().getType()).host(swaggerProperties.getHost())
				.groupName(swaggerProperties.getGroupName()).apiInfo(apiInfo());
		// 2.安全配置
		docket.securitySchemes(securitySchema()).securityContexts(securityContext());
		// 3.selection by requestHandler and by paths
		docket.select().apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage())).paths(paths())
				.build();
		return docket;
	}

	private List<SecurityScheme> securitySchema() {
		SwaggerProperties.Authorization authorizationProps = swaggerProperties.getAuthorization();

		DocumentationType documentationType = swaggerProperties.getDocumentationType().getType();
		SecuritySchemaEnum schema = authorizationProps.getSchema();
		String name = authorizationProps.getName();
		List<SecurityScheme> securitySchemes = new ArrayList<>();
		switch (schema) {
		case OATH2:

			List<AuthorizationScope> authorizationScopeList = authorizationProps.getAuthorizationScopeList().stream()
					.map(scope -> new AuthorizationScope(scope.getScope(), scope.getDescription()))
					.collect(Collectors.toList());
			String tokenUrl = authorizationProps.getOauth2().getTokenUrl();
			SecurityScheme securityScheme;
			if (documentationType.equals(DocumentationType.SWAGGER_2)) {
				// swagger2 OAuth2
				List<GrantType> grantTypes = Collections
						.singletonList(new ResourceOwnerPasswordCredentialsGrant(tokenUrl));
				securityScheme = new OAuth(name, authorizationScopeList, grantTypes);
			}
			else {
				// Swagger3 Oauth2
				securityScheme = OAuth2Scheme.OAUTH2_PASSWORD_FLOW_BUILDER.name(name).tokenUrl(tokenUrl)
						.scopes(authorizationScopeList).build();
			}
			securitySchemes = Collections.singletonList(securityScheme);
			break;
		case API_KEY:
			SwaggerProperties.Authorization.ApiKey apiKey = authorizationProps.getApiKey();
			securitySchemes = Collections.singletonList(new ApiKey(name, name, apiKey.getIn()));
			break;
		}

		return securitySchemes;
	}

	/**
	 * 配置默认的全局鉴权策略的开关，通过正则表达式进行匹配；默认匹配所有URL
	 * @return SecurityContext
	 */
	private List<SecurityContext> securityContext() {
		SecurityContext securityContext = SecurityContext.builder().securityReferences(defaultAuth()).build();
		return Collections.singletonList(securityContext);
	}

	/**
	 * 默认的全局鉴权策略
	 * @return List<SecurityReference>
	 */
	private List<SecurityReference> defaultAuth() {
		SwaggerProperties.Authorization authorization = swaggerProperties.getAuthorization();
		List<AuthorizationScope> authorizationScopeList = authorization.getAuthorizationScopeList().stream()
				.map(scope -> new AuthorizationScope(scope.getScope(), scope.getDescription()))
				.collect(Collectors.toList());

		AuthorizationScope[] authorizationScopes = new AuthorizationScope[authorizationScopeList.size()];

		SecurityReference securityReference = SecurityReference.builder().reference(authorization.getName())
				.scopes(authorizationScopeList.toArray(authorizationScopes)).build();

		return Collections.singletonList(securityReference);
	}

	private Predicate<String> paths() {
		// base-path 和 exclude-path 的默认值处理
		if (swaggerProperties.getBasePath().isEmpty()) {
			swaggerProperties.getBasePath().add(SwaggerConstants.DEFAULT_BASE_PATH);
		}
		if (swaggerProperties.getExcludePath().isEmpty()) {
			swaggerProperties.getExcludePath().addAll(SwaggerConstants.DEFAULT_EXCLUDE_PATH);
		}

		List<Predicate<String>> basePath = new ArrayList<>();
		for (String path : swaggerProperties.getBasePath()) {
			basePath.add(PathSelectors.ant(path));
		}
		List<Predicate<String>> excludePath = new ArrayList<>();
		for (String path : swaggerProperties.getExcludePath()) {
			excludePath.add(PathSelectors.ant(path));
		}
		// 必须满足basePath 且不满足 exclude-path
		return s -> basePath.stream().anyMatch(x -> x.test(s)) && excludePath.stream().noneMatch(x -> x.test(s));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(swaggerProperties.getTitle()).description(swaggerProperties.getDescription())
				.license(swaggerProperties.getLicense()).licenseUrl(swaggerProperties.getLicenseUrl())
				.termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
				.contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(),
						swaggerProperties.getContact().getEmail()))
				.version(swaggerProperties.getVersion()).build();
	}

}
