package com.relaxed.extend.sms.executor.model.dto;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import lombok.experimental.Accessors;

/**
 * 人工处理任务表 数据传输对象
 *
 * @author Yakir
 * @since 2021-08-27T18:18:41.264
 */
@ApiModel(value = "人工处理任务表")
@Data
@Accessors(chain = true)
public class ManualProcessDTO implements Serializable  {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 模板
     */
    @ApiModelProperty(value = "模板")
    private String template;
    /**
     * 签名
     */
    @ApiModelProperty(value = "签名")
    private String signature;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;
    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    private String request;
    /**
     * 通道id集合
     */
    @ApiModelProperty(value = "通道id集合")
    private String configIds;
    /**
     * 状态 0新建，1处理中，2处理成功，3处理失败
     */
    @ApiModelProperty(value = "状态 0新建，1处理中，2处理成功，3处理失败")
    private Integer status;
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

