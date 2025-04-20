package com.relaxed.extend.openapi;

import org.springdoc.core.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 将Relaxed OpenAPI的开关配置同步至SpringDoc。 用于确保Relaxed的OpenAPI配置与SpringDoc的配置保持一致。
 *
 * @author hccake
 * @since 1.0
 */
public class SwaggerEnabledStatusReplaceEnvironmentPostProcessor implements EnvironmentPostProcessor {

	/**
	 * 资源名称
	 */
	private static final String REPLACE_SOURCE_NAME = "replaceEnvironment";

	private static final String BALLCAT_OPENAPI_ENABLED_KEY = OpenApiProperties.PREFIX + ".enabled";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		// 如果已经独立配置了springdoc的开关信息，则不处理
		String springFoxSwaggerEnabledValue = environment.getProperty(Constants.SPRINGDOC_ENABLED);
		if (StringUtils.hasText(springFoxSwaggerEnabledValue)) {
			return;
		}

		// 获取Relaxed的OpenAPI开关状态
		boolean ballcatEnabledOpenApi = true;
		String ballcatOpenApiEnabledValue = environment.getProperty(BALLCAT_OPENAPI_ENABLED_KEY);
		if (StringUtils.hasText(ballcatOpenApiEnabledValue)) {
			ballcatEnabledOpenApi = "true".equalsIgnoreCase(ballcatOpenApiEnabledValue);
		}

		// 将Relaxed OpenAPI的开关状态同步至SpringDoc
		Map<String, Object> map = new HashMap<>(1);
		map.put(Constants.SPRINGDOC_ENABLED, ballcatEnabledOpenApi);
		replace(environment.getPropertySources(), map);
	}

	/**
	 * 替换属性源
	 * @param propertySources 属性源集合
	 * @param map 新的属性值
	 */
	private void replace(MutablePropertySources propertySources, Map<String, Object> map) {
		MapPropertySource target = null;
		if (propertySources.contains(REPLACE_SOURCE_NAME)) {
			PropertySource<?> source = propertySources.get(REPLACE_SOURCE_NAME);
			if (source instanceof MapPropertySource) {
				target = (MapPropertySource) source;
				target.getSource().putAll(map);
			}
		}
		if (target == null) {
			target = new MapPropertySource(REPLACE_SOURCE_NAME, map);
		}
		if (!propertySources.contains(REPLACE_SOURCE_NAME)) {
			propertySources.addFirst(target);
		}
	}

}
