package com.relaxed.common.risk.engine.model.vo;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import lombok.experimental.Accessors;

/**
 * 视图层
 *
 * @author Yakir
 * @since 2021-08-31T09:58:08.656
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class ModelConfVO implements Serializable {

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "")
	private Long id;

	/**
	 * 模型id
	 */
	@ApiModelProperty(value = "")
	private Long modelId;

	/**
	 * 模型名称
	 */
	@ApiModelProperty(value = "")
	private String name;

	/**
	 * 模型文件路径
	 */
	@ApiModelProperty(value = "")
	private String path;

	/**
	 * tensorflow框架保存模型时设置的tag，非tensorflow模型此字段为空
	 */
	@ApiModelProperty(value = "")
	private String tag;

	/**
	 * 模型输出操作名称，predict_Y = tf.nn.softmax(softmax_before, name='predict')
	 */
	@ApiModelProperty(value = "")
	private String operation;

	/**
	 * 模型更新时间
	 */
	@ApiModelProperty(value = "")
	private LocalDateTime updateDate;

	/**
	 * 机器学习类型
	 */
	@ApiModelProperty(value = "机器学习类型")
	private String type;

	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述")
	private String comment;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 参数列表
	 */
	private List<ModelConfParamVO> params;

}
