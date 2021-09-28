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
 * @since 2021-08-29T18:48:19.435
 */
@ApiModel(value = "")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("engine_activation")
public class Activation extends Model<Activation> {

	/**
	 * 主键
	 */
	@TableId(value = "ID")
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String activationName;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String label;

	/**
	 * model id
	 */
	@ApiModelProperty(value = "model id")
	private Long modelId;

	/**
	 * 注释
	 */
	@ApiModelProperty(value = "注释")
	private String comment;

	/**
	 * 底部阀值
	 */
	@ApiModelProperty(value = "底部阀值")
	private Integer bottom;

	/**
	 * 中间阀值
	 */
	@ApiModelProperty(value = "中间阀值")
	private Integer median;

	/**
	 * 顶部阀值
	 */
	@ApiModelProperty(value = "顶部阀值")
	private Integer high;

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
