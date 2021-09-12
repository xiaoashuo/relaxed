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
 * @since 2021-08-29T13:57:50.664
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class PreItemQO implements Serializable {

	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private Long id;

}
