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
 * @since 2021-08-29T09:04:20.388
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class ModelQO implements Serializable {

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

}
