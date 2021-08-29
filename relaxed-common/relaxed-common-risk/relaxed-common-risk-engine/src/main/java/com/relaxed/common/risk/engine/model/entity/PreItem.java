package com.relaxed.common.risk.engine.model.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Yakir
 * @since 2021-08-29T13:57:50.664
 */
@ApiModel(value = "")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("engine_pre_item")
public class PreItem extends Model<PreItem> {

	/**
	 * ID
	 */
	@TableId(value = "ID")
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
