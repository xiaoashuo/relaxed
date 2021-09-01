package com.relaxed.common.risk.engine.model.entity;

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
 * @since 2021-09-01T13:49:40.174
 */
@ApiModel(value = "")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("data_mobile_info")
public class MobileInfo extends Model<MobileInfo> {

	/**
	 *
	 */
	@TableId(value = "ID")
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
