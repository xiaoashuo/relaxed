package com.relaxed.common.risk.model.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author Yakir
 * @since 2021-08-29T18:48:19.341
 */
@ApiModel(value = "")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("engine_data_list_meta")
public class DataListMeta extends Model<DataListMeta> {

	/**
	 * 主键
	 */
	@TableId(value = "ID")
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
