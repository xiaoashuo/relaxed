package com.relaxed.common.risk.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 视图层
 *
 * @author Yakir
 * @since 2021-08-31T09:58:08.892
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class ModelConfParamVO implements Serializable {

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
