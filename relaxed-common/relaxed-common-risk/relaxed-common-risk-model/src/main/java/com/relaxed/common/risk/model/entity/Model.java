package com.relaxed.common.risk.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

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
	@NotEmpty(message = "模型名称不能为空")
	@ApiModelProperty(value = "模型名称")
	private String modelName;

	/**
	 * 模型标签
	 */
	@NotEmpty(message = "模型标签不能为空")
	@ApiModelProperty(value = "模型标签")
	private String label;

	/**
	 * 事件中标识实体的主键
	 */
	@NotEmpty(message = "模型实体名称不能为空")
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
