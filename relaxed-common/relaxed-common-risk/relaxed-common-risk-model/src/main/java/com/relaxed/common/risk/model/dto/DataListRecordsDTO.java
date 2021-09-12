package com.relaxed.common.risk.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据传输对象
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.131
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class DataListRecordsDTO implements Serializable {

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * 数据列表ID
	 */
	@ApiModelProperty(value = "数据列表ID")
	private Long dataListId;

	/**
	 * 数据记录
	 */
	@ApiModelProperty(value = "数据记录")
	private String dataRecord;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private LocalDateTime createTime;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private LocalDateTime updateTime;

}
