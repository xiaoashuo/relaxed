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
 * @since 2021-08-29T18:48:19.389
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class DataListsDTO implements Serializable {

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * 列表名
	 */
	@ApiModelProperty(value = "列表名")
	private String name;

	/**
	 * 列表名中文描叙
	 */
	@ApiModelProperty(value = "列表名中文描叙")
	private String label;

	/**
	 * 模型ID
	 */
	@ApiModelProperty(value = "模型ID")
	private Long modelId;

	/**
	 * 注释
	 */
	@ApiModelProperty(value = "注释")
	private String comment;

	/**
	 * 列表类型
	 */
	@ApiModelProperty(value = "列表类型")
	private String listType;

	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
	private Integer status;

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
