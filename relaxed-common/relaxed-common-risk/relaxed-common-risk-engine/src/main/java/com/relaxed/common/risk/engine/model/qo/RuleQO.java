package com.relaxed.common.risk.engine.model.qo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询对象
 *
 * @author Yakir
 * @since 2021-08-31T11:30:23.273
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class RuleQO implements Serializable {

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * 评分id
	 */
	private Long activationId;

}
