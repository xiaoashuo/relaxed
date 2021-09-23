package com.relaxed.common.risk.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据传输对象
 *
 * @author Yakir
 * @since 2021-08-29T13:57:50.664
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class PreItemDTO implements Serializable {

	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private Long id;

	/**
	 * 模型ID
	 */
	@ApiModelProperty(value = "模型ID")
	private Long modelId;

	/**
	 * 来源字段
	 */
	@ApiModelProperty(value = "来源字段")
	private String sourceField;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String sourceLabel;

	/**
	 * 目标字段
	 */
	@ApiModelProperty(value = "目标字段")
	private String destField;

	/**
	 * 目标字段类型
	 */
	@ApiModelProperty(value = "目标字段类型")
	private String destFieldType;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String label;

	/**
	 * 参数
	 */
	@ApiModelProperty(value = "参数")
	private String args;

	/**
	 * 转换插件
	 */
	@ApiModelProperty(value = "转换插件")
	private String plugin;

	/**
	 * 响应字段配置信息
	 */
	@ApiModelProperty(value = "响应字段配置信息")
	private String configJson;

	/**
	 * 请求方式
	 */
	@ApiModelProperty(value = "请求方式")
	private String reqType;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Integer status;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private LocalDateTime createTime;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private LocalDateTime updateTime;

}
