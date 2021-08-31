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
 * @since 2021-08-31T09:58:08.892
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class ModelConfParamDTO implements Serializable {

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Long id;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Long moldId;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String feed;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String expressions;

}
