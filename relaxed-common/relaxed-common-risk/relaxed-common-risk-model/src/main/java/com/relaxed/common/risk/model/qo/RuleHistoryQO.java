package com.relaxed.common.risk.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 查询对象
 *
 * @author Yakir
 * @since 2021-08-31T11:30:23.317
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class RuleHistoryQO implements Serializable {

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

}