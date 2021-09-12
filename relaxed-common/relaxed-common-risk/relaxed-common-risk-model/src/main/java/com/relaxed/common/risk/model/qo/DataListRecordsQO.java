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
 * @since 2021-08-29T18:48:19.131
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class DataListRecordsQO implements Serializable {

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

}