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
 * @since 2021-08-31T09:58:08.656
 */
@ApiModel(value = "")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("engine_model_conf")
public class ModelConf extends Model<ModelConf> {

	/**
	 *
	 */
	@TableId(value = "id")
	@ApiModelProperty(value = "")
	private Long id;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Long modelId;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String name;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String path;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String tag;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String operation;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private LocalDateTime updateDate;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String type;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String comment;

	/**
	 *
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@ApiModelProperty(value = "")
	private LocalDateTime createTime;

}
