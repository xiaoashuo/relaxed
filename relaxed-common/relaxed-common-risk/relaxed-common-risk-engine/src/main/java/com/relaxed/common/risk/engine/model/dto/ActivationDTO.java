package com.relaxed.common.risk.engine.model.dto;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import lombok.experimental.Accessors;

/**
 * 数据传输对象
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.435
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class ActivationDTO implements Serializable {

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String activationName;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String label;

	/**
	 * model id
	 */
	@ApiModelProperty(value = "model id")
	private Long modelId;

	/**
	 * 注释
	 */
	@ApiModelProperty(value = "注释")
	private String comment;

	/**
	 * 底部阀值
	 */
	@ApiModelProperty(value = "底部阀值")
	private Integer bottom;

	/**
	 * 中间阀值
	 */
	@ApiModelProperty(value = "中间阀值")
	private Integer median;

	/**
	 * 顶部阀值
	 */
	@ApiModelProperty(value = "顶部阀值")
	private Integer high;

	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
	private Integer status;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String ruleOrder;

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
