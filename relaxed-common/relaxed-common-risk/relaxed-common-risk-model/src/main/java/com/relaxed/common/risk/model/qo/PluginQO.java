package com.relaxed.common.risk.model.qo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询对象
 *
 * @author Yakir
 * @since 2021-09-28T13:43:11.834
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class PluginQO implements Serializable {

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;

}
