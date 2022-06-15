package com.relaxed.common.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2020/6/11 23:19
 */
@Data
@ConfigurationProperties(prefix = "relaxed.wechat")
public class WechatProperties {

	/**
	 * Web hook 地址
	 */
	private String url;

}
