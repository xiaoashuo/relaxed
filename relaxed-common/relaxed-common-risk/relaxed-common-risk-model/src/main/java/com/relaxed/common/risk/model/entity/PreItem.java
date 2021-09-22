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

import javax.validation.constraints.NotNull;

/**
 * @author Yakir
 * @since 2021-08-29T13:57:50.664
 */
@ApiModel(value = "")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("engine_pre_item")
public class PreItem extends Model<PreItem> {

	/**
	 * ID
	 */
	@TableId(value = "ID")
	@ApiModelProperty(value = "ID")
	private Long id;

	/**
	 * 模型ID
	 */
	@NotNull(message = "模型id不能为空")
	@ApiModelProperty(value = "模型ID")
	private Long modelId;

	/**
	 * 来源字段
	 */
	@ApiModelProperty(value = "来源字段")
	private String sourceField;

	/**
	 * 来源标签
	 */
	@ApiModelProperty(value = "来源标签")
	private String sourceLabel;

	/**
	 * 目标字段
	 */
	@ApiModelProperty(value = "目标字段")
	private String destField;

	/**
	 * 目标标签 描述
	 */
	@ApiModelProperty(value = "目标标签")
	private String label;

	/**
	 * 参数
	 */
	@ApiModelProperty(value = "参数")
	private String args;

	/**
	 * 转换插件
	 */
	@ApiModelProperty(value = "转换插件")
	private String plugin;

	/**
	 * 扩展配置信息
	 */
	@ApiModelProperty(value = "扩展配置信息")
	private String configJson;

	/**
	 * 请求方式
	 */
	@ApiModelProperty(value = "请求方式")
	private String reqType;

	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
	private Integer status;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

}
