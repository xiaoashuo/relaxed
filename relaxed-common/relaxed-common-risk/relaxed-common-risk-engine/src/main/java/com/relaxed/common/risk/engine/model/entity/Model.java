package com.relaxed.common.risk.engine.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Yakir
 * @since 2021-08-29T09:04:20.388
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
@TableName("engine_model")
public class Model implements Serializable {

	/**
	 * 主键
	 */
	@TableId(value = "ID")
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String guid;

	/**
	 * 模型名称
	 */
	@ApiModelProperty(value = "模型名称")
	private String modelName;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String label;

	/**
	 * 事件中标识实体的主键
	 */
	@ApiModelProperty(value = "事件中标识实体的主键")
	private String entityName;

	/**
	 * 事件主键
	 */
	@ApiModelProperty(value = "事件主键")
	private String entryName;

	/**
	 * 事件时间
	 */
	@ApiModelProperty(value = "事件时间")
	private String referenceDate;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Integer fieldValidate;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String code;

	/**
	 * 1 标识模板
	 */
	@ApiModelProperty(value = "1 标识模板")
	private Integer template;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String dashboardUrl;

	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
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
