package com.relaxed.common.swagger.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic SwaggerAggregatortProperties
 * @Description
 * @date 2021/7/8 14:01
 * @Version 1.0
 */
@Data
@ConfigurationProperties("relaxed.swagger.aggregator")
public class SwaggerAggregatorProperties {

	/**
	 * 聚合文档源信息 { "providerResources": [ { "name": "provider name", "url":
	 * "/v2/websockets.json", "swaggerVersion": "1.0" } ] }
	 */
	private List<SwaggerResource> providerResources = new ArrayList<>();

}
