package com.relaxed.extend.sms.executor.model.dto;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import lombok.experimental.Accessors;

/**
 * 日志表 数据传输对象
 *
 * @author Yakir
 * @since 2021-08-27T18:18:41.122
 */
@ApiModel(value = "日志表")
@Data
@Accessors(chain = true)
public class SendLogDTO implements Serializable  {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 配置主键
     */
    @ApiModelProperty(value = "配置主键")
    private String configId;
    /**
     * 配置平台
     */
    @ApiModelProperty(value = "配置平台")
    private String configPlatform;
    /**
     * 配置名称
     */
    @ApiModelProperty(value = "配置名称")
    private String configName;
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
     * 返回参数
     */
    @ApiModelProperty(value = "返回参数")
    private String response;
    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String error;
    /**
     * 耗时
     */
    @ApiModelProperty(value = "耗时")
    private Long useTime;
    /**
     * 状态：0失败，1成功
     */
    @ApiModelProperty(value = "状态：0失败，1成功")
    private Integer status;
    /**
     * api日志主键
     */
    @ApiModelProperty(value = "api日志主键")
    private String apiLogId;
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

