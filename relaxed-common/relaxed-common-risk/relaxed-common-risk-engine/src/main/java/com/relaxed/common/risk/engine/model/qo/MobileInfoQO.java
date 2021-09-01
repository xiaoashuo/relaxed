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
 * @since 2021-09-01T13:49:40.174
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class MobileInfoQO implements Serializable {

	/**
	*
	*/
	@ApiModelProperty(value = "")
	private Long id;

}
