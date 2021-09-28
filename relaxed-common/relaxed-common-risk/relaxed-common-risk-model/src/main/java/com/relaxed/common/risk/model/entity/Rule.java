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
 * @since 2021-08-31T11:30:23.273
 */
@ApiModel(value = "")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("engine_rule")
public class Rule extends Model<Rule> {

	/**
	 * 主键
	 */
	@TableId(value = "ID")
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * 模型ID
	 */
	@ApiModelProperty(value = "模型ID")
	private Long modelId;

	/**
	 * 激活ID
	 */
	@ApiModelProperty(value = "激活ID")
	private Long activationId;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String name;

	/**
	 * 规则名称
	 */
	@ApiModelProperty(value = "规则名称")
	private String label;

	/**
	 * 检验脚本
	 */
	@ApiModelProperty(value = "检验脚本")
	private String scripts;

	/**
	 * 初始分数
	 */
	@ApiModelProperty(value = "初始分数")
	private Integer initScore;

	/**
	 * 基数
	 */
	@ApiModelProperty(value = "基数")
	private Integer baseNum;

	/**
	 * 运算符
	 */
	@ApiModelProperty(value = "运算符")
	private String operator;

	/**
	 * 抽象名称
	 */
	@ApiModelProperty(value = "抽象名称")
	private String abstractionName;

	/**
	 * 比例
	 */
	@ApiModelProperty(value = "比例")
	private Integer rate;

	/**
	 * 最大得分值
	 */
	@ApiModelProperty(value = "最大得分值")
	private Integer max;

	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
	private Integer status;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String ruleDefinition;

	/**
	 * 规则顺序
	 */
	@ApiModelProperty(value = "规则顺序")
	private Integer ruleOrder;

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
