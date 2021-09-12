package com.relaxed.common.risk.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * @author Yakir
 * @since 2021-08-29T12:14:38.328
 */
@ApiModel(value = "")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("engine_field")
public class Field extends Model<Field> {

	/**
	 * 主键
	 */
	@TableId(value = "ID")
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * MODEL ID
	 */
	@ApiModelProperty(value = "MODEL ID")
	private Long modelId;

	/**
	 * 事件信息中的包含的字段
	 */
	@ApiModelProperty(value = "事件信息中的包含的字段")
	private String fieldName;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String label;

	/**
	 * 字段类型
	 */
	@ApiModelProperty(value = "字段类型")
	private String fieldType;

	/**
	 * 校验类型
	 */
	@ApiModelProperty(value = "校验类型")
	private String validateType;

	/**
	 * 校验参数
	 */
	@ApiModelProperty(value = "校验参数")
	private String validateArgs;

	/**
	 * 1 标识索引字段
	 */
	@ApiModelProperty(value = "1 标识索引字段")
	private Integer indexed;

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
