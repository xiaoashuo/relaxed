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
 * @since 2021-08-31T11:30:23.317
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class RuleHistoryDTO implements Serializable {

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * 模型ID
	 */
	@ApiModelProperty(value = "模型ID")
	private Long ruleId;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String merchantCode;

	/**
	 * 规则名称
	 */
	@ApiModelProperty(value = "规则名称")
	private String label;

	/**
	 * 初始分数
	 */
	@ApiModelProperty(value = "初始分数")
	private Integer initScore;

	/**
	 * 基数
	 */
	@ApiModelProperty(value = "基数")
	private Integer baseNum;

	/**
	 * 运算符
	 */
	@ApiModelProperty(value = "运算符")
	private String operator;

	/**
	 * 抽象名称
	 */
	@ApiModelProperty(value = "抽象名称")
	private String abstractionName;

	/**
	 * 比例
	 */
	@ApiModelProperty(value = "比例")
	private Integer rate;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String ruleDefinition;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private LocalDateTime updateTime;

}
