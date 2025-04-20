package com.relaxed.extend.openapi;

import cn.hutool.core.collection.ListUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;
import java.util.Map;

/**
 * OpenAPI配置属性类。 用于配置Swagger/OpenAPI文档的基本信息，包括文档标题、描述、版本、联系人等。
 *
 * @author hccake
 * @since 1.0
 */
@Data
@ConfigurationProperties(OpenApiProperties.PREFIX)
public class OpenApiProperties {

	public static final String PREFIX = "relaxed.openapi";

	/**
	 * 是否开启OpenAPI文档
	 */
	private Boolean enabled = true;

	/**
	 * 文档基本信息
	 */
	@NestedConfigurationProperty
	private InfoProperties info = new InfoProperties();

	/**
	 * 扩展文档地址
	 */
	@NestedConfigurationProperty
	private ExternalDocumentation externalDocs;

	/**
	 * API服务配置
	 * @see <a href="https://swagger.io/docs/specification/api-host-and-base-path/">API
	 * Server and Base URL</a>
	 */
	private List<Server> servers = null;

	/**
	 * 安全配置
	 * @see <a href=
	 * "https://swagger.io/docs/specification/authentication/">Authentication</a>
	 */
	private List<SecurityRequirement> security = null;

	/**
	 * API标签配置
	 */
	private List<Tag> tags = null;

	/**
	 * API路径配置
	 */
	@NestedConfigurationProperty
	private Paths paths = null;

	/**
	 * 组件配置
	 */
	@NestedConfigurationProperty
	private Components components = null;

	/**
	 * 扩展信息
	 */
	private Map<String, Object> extensions = null;

	/**
	 * 跨域配置
	 */
	private CorsConfig corsConfig;

	/**
	 * 文档基本信息配置类
	 */
	@Data
	public static class InfoProperties {

		/**
		 * 文档标题
		 */
		private String title = null;

		/**
		 * 文档描述
		 */
		private String description = null;

		/**
		 * 服务条款URL
		 */
		private String termsOfService = null;

		/**
		 * 联系人信息
		 */
		@NestedConfigurationProperty
		private Contact contact = null;

		/**
		 * 许可证信息
		 */
		@NestedConfigurationProperty
		private License license = null;

		/**
		 * API版本
		 */
		private String version = null;

		/**
		 * 扩展属性
		 */
		private Map<String, Object> extensions = null;

	}

	/**
	 * 跨域配置类
	 */
	@Data
	public static class CorsConfig {

		/**
		 * 是否开启CORS跨域配置
		 */
		private boolean enabled = false;

		/**
		 * 跨域URL匹配规则
		 */
		private String urlPattern = "/**";

		/**
		 * 允许跨域的源
		 */
		private List<String> allowedOrigins;

		/**
		 * 允许跨域来源的匹配规则
		 */
		private List<String> allowedOriginPatterns;

		/**
		 * 允许跨域的方法列表
		 */
		private List<String> allowedMethods = ListUtil.toList(CorsConfiguration.ALL);

		/**
		 * 允许跨域的头信息
		 */
		private List<String> allowedHeaders = ListUtil.toList(CorsConfiguration.ALL);

		/**
		 * 额外允许跨域请求方获取的response header信息
		 */
		private List<String> exposedHeaders = ListUtil.toList("traceId");

		/**
		 * 是否允许跨域发送Cookie
		 */
		private Boolean allowCredentials = true;

		/**
		 * CORS配置缓存时间，用于控制浏览器端是否发起Option预检请求
		 */
		private Long maxAge;

	}

}
