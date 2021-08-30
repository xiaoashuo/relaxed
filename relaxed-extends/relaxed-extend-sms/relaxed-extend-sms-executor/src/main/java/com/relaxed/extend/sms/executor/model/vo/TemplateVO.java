package com.relaxed.extend.sms.executor.model.vo;

import java.time.LocalDateTime;
import java.io.Serializable;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import lombok.experimental.Accessors;

/**
 * 模板表 视图层
 *
 * @author Yakir
 * @since 2021-08-27T18:18:41.302
 */
@ApiModel(value = "模板表")
@Data
@Accessors(chain = true)
public class TemplateVO implements Serializable  {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String name;
    /**
     * 模板编码
     */
    @ApiModelProperty(value = "模板编码")
    private String code;
    /**
     * 模板内容
     */
    @ApiModelProperty(value = "模板内容")
    private String content;
    /**
     * 模板类型 1：验证码，2：营销类
     */
    @ApiModelProperty(value = "模板类型 1：验证码，2：营销类")
    private Integer type;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createUser;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateUser;
    /**
     * 逻辑删除：0删除
     */
    @ApiModelProperty(value = "逻辑删除：0删除")
    private Integer isDelete;

}

