package com.relaxed.common.risk.engine.model.dto;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import lombok.experimental.Accessors;

/**
 * 数据传输对象
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.341
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class DataListMetaDTO implements Serializable {

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
	 * 字段名
	 */
	@ApiModelProperty(value = "字段名")
	private String fieldName;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String label;

	/**
	 * 字段序号
	 */
	@ApiModelProperty(value = "字段序号")
	private Integer seqNum;

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
