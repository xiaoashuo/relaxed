package com.relaxed.extend.sms.executor.model.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 配置表
 *
 * @author Yakir
 * @since 2021-08-27T14:55:25.330
 */
@ApiModel(value = "配置表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("config")
public class Config extends Model<Config> {

    /**
     * 主键
     */
           @TableId(value="id")
        @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 名称
     */
        @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 平台
     */
        @ApiModelProperty(value = "平台")
    private String platform;
    /**
     * 域名
     */
        @ApiModelProperty(value = "域名")
    private String domain;
    /**
     * 
     */
        @ApiModelProperty(value = "")
    private String accessKeyId;
    /**
     * 
     */
        @ApiModelProperty(value = "")
    private String accessKeySecret;
    /**
     * 其他配置 json格式
     */
        @ApiModelProperty(value = "其他配置 json格式")
    private String other;
    /**
     * 是否可用：0不可用
     */
        @ApiModelProperty(value = "是否可用：0不可用")
    private Integer isActive;
    /**
     * 是否正常：0不正常
     */
        @ApiModelProperty(value = "是否正常：0不正常")
    private Integer isEnable;
    /**
     * 备注
     */
        @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 级别
     */
        @ApiModelProperty(value = "级别")
    private Integer level;
    /**
     * 通道类型，1：文字，2：语音，3：推送
     */
        @ApiModelProperty(value = "通道类型，1：文字，2：语音，3：推送")
    private Integer channelType;
    /**
     * 创建时间
     */
           @TableField(value = "create_time" , fill = FieldFill.INSERT)
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
           @TableField(value = "update_time" , fill = FieldFill.INSERT_UPDATE)
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

