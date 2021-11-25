package com.relaxed.common.swagger.property;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic SwaggerProperties
 * @Description
 * @date 2021/7/8 12:49
 * @Version 1.0
 */
@Data
@ConfigurationProperties(SwaggerProperties.PREFIX)
public class SwaggerProperties {

	public static final String PREFIX = "relaxed.swagger";

	/**
	 * 是否开启swagger
	 */
	private Boolean enabled = true;

	/**
	 * 分组名称
	 */
	private String groupName;

	/**
	 * swagger会解析的包路径
	 **/
	private String basePackage = "";

	/**
	 * 文档版本，默认使用 2.0
	 */
	private DocumentationTypeEnum documentationType = DocumentationTypeEnum.OAS_30;

	/**
	 * swagger会解析的url规则
	 **/
	private List<String> basePath = new ArrayList<>();

	/**
	 * 在basePath基础上需要排除的url规则
	 **/
	private List<String> excludePath = new ArrayList<>();

	/**
	 * 需要排除的服务
	 */
	private List<String> ignoreProviders = new ArrayList<>();

	/**
	 * 标题
	 **/
	private String title = "";

	/**
	 * 描述
	 **/
	private String description = "";

	/**
	 * 版本
	 **/
	private String version = "";

	/**
	 * 许可证
	 **/
	private String license = "";

	/**
	 * 许可证URL
	 **/
	private String licenseUrl = "";

	/**
	 * 服务条款URL
	 **/
	private String termsOfServiceUrl = "";

	/**
	 * host信息
	 **/
	private String host = "";

	/**
	 * 联系人信息
	 */
	private Contact contact = new Contact();

	/**
	 * 全局统一鉴权配置
	 **/
	private Authorization authorization = new Authorization();

	@Data
	@NoArgsConstructor
	public static class Contact {

		/**
		 * 联系人
		 **/
		private String name = "";

		/**
		 * 联系人url
		 **/
		private String url = "";

		/**
		 * 联系人email
		 **/
		private String email = "";

	}

	@Data
	@NoArgsConstructor
	public static class Authorization {

		/**
		 * schema type
		 */
		private SecuritySchemaEnum schema = SecuritySchemaEnum.OATH2;

		/**
		 * 鉴权策略ID，需要和SecurityReferences ID保持一致 不然授权参数不在全局请求中生效
		 */
		private String name = "";

		/**
		 * 需要开启鉴权URL的正则
		 */
		private String authRegex = "^.*$";

		/**
		 * 鉴权作用域列表
		 */
		private List<AuthorizationScope> authorizationScopeList = new ArrayList<>();

		/**
		 * API KEY SCHEMA
		 */
		private ApiKey apiKey = new ApiKey();

		/**
		 * OAUTH2 SCHEMA
		 */
		private Oauth2 oauth2 = new Oauth2();

		/**
		 * params name=> 标签值 与 SecurityReferences ID 一致 key name=>请求的key in=> 请求放在位置 此处
		 * name key name 使用Authorization=>name
		 */
		@Data
		@NoArgsConstructor
		public static class ApiKey {

			/**
			 * key can be "header", "query" or "cookie"
			 */
			private String in = SecurityScheme.In.HEADER.toString();

		}

		/**
		 * oauth 2
		 */
		@Data
		@NoArgsConstructor
		public static class Oauth2 {

			/**
			 * token请求地址，如需开启OAuth2 password 类型登陆则必传此参数
			 */
			private String tokenUrl = "";

		}

	}

	@Data
	@NoArgsConstructor
	public static class AuthorizationScope {

		/**
		 * 作用域名称
		 */
		private String scope = "";

		/**
		 * 作用域描述
		 */
		private String description = "";

	}

}
