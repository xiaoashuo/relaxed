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
 * @since 2021-09-01T13:49:40.174
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
public class MobileInfoDTO implements Serializable {

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Long id;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private String mobile;

	/**
	 * 省
	 */
	@ApiModelProperty(value = "省")
	private String province;

	/**
	 * 市
	 */
	@ApiModelProperty(value = "市")
	private String city;

	/**
	 * 卡信息
	 */
	@ApiModelProperty(value = "卡信息")
	private String supplier;

	/**
	 * 区号
	 */
	@ApiModelProperty(value = "区号")
	private String regionCode;

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
