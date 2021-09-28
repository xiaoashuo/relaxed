package com.relaxed.common.risk.model.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Yakir
 * @since 2021-09-28T13:43:11.834
 */
@ApiModel(value = "")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("engine_plugin")
public class Plugin extends Model<Plugin> {

	/**
	 * 主键id
	 */
	@TableId(value = "id")
	@ApiModelProperty(value = "主键id")
	private Long id;

	/**
	 * 插件名称
	 */
	@ApiModelProperty(value = "插件名称")
	private String pluginName;

	/**
	 * 插件描述
	 */
	@ApiModelProperty(value = "插件描述")
	private String pluginDesc;

	/**
	 * 插件元数据
	 */
	@ApiModelProperty(value = "插件元数据")
	private String pluginMeta;

	/**
	 * 插件状态 1启用 0禁用
	 */
	@ApiModelProperty(value = "插件状态")
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
