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
 * @since 2021-08-31T09:58:08.892
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class ModelConfParamQO implements Serializable {

	/**
	*
	*/
	@ApiModelProperty(value = "")
	private Long id;

}
