package com.relaxed.fastexcel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Excel配置属性类 用于管理Excel相关的配置信息 主要功能: 1. 配置Excel模板路径 2. 支持通过配置文件注入属性 3. 提供默认配置值
 *
 * @author lengleng
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = ExcelConfigProperties.PREFIX)
public class ExcelConfigProperties {

	/**
	 * 配置属性前缀 用于在配置文件中定位Excel相关配置
	 */
	static final String PREFIX = "excel";

	/**
	 * Excel模板文件路径 默认值为"excel"目录 可通过配置文件修改: excel.template-path=your_template_path
	 */
	private String templatePath = "excel";

}
