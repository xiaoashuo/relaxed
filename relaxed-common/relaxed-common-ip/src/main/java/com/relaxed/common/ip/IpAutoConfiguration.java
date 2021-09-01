package com.relaxed.common.ip;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

/**
 * @author Yakir
 * @Topic Ip2RegionAutoConfiguration
 * @Description
 * @date 2021/9/1 11:07
 * @Version 1.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(Ip2RegionProperties.class)
@ConditionalOnClass(DbSearcher.class)
public class IpAutoConfiguration {

	/**
	 * ip 初始化
	 * @author yakir
	 * @date 2021/9/1 11:21
	 * @param properties
	 * @return com.relaxed.common.ip.Ip2RegionSearcher
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = Ip2RegionProperties.PREFIX, name = "enabled", havingValue = "true")
	public Ip2RegionSearcher autoIp2regionSearcher(Ip2RegionProperties properties) {
		try {
			InputStream inputStream = IpAutoConfiguration.class.getResourceAsStream(properties.getDbFile());
			byte[] bytes = IoUtil.readBytes(inputStream);
			DbConfig dbConfig = new DbConfig();
			DbSearcher dbSearcher = new DbSearcher(dbConfig, bytes);
			return new Ip2RegionSearcher(dbSearcher);
		}
		catch (DbMakerConfigException e) {
			// 默认配置下忽略此异常
		}
		catch (Exception e) {
			log.error("初始化ip库异常,文件{}", properties.getDbFile(), e);
		}
		throw new IpException("IP 初始化异常");
	}

}
