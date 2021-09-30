package com.relaxed.common.risk.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 视图层
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.507
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class AbstractionVO implements Serializable {

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * Abstraction 名称
	 */
	@ApiModelProperty(value = "Abstraction 名称")
	private String name;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String label;

	/**
	 * MODEL ID
	 */
	@ApiModelProperty(value = "MODEL ID")
	private Long modelId;

	/**
	 * 统计类型
	 */
	@ApiModelProperty(value = "统计类型")
	private Integer aggregateType;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String searchField;

	/**
	 * 时间段类型
	 */
	@ApiModelProperty(value = "时间段类型")
	private Integer searchIntervalType;

	/**
	 * 时间段具体值
	 */
	@ApiModelProperty(value = "时间段具体值")
	private Integer searchIntervalValue;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String functionField;

	/**
	 * 数据校验
	 */
	@ApiModelProperty(value = "数据校验")
	private String ruleScript;

	/**
	 * 数据校验入口
	 */
	@ApiModelProperty(value = "数据校验入口")
	private String ruleScriptEntry;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String ruleDefinition;

	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
	private Integer status;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String comment;

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
