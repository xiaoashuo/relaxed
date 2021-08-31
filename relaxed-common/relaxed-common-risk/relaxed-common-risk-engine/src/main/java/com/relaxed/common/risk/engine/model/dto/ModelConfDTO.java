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
 * @since 2021-08-31T09:58:08.656
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class ModelConfDTO implements Serializable {

	/**
	 *
	 */
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
	@ApiModelProperty(value = "")
	private LocalDateTime createTime;

}
