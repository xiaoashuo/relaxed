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
 * 签名表
 *
 * @author Yakir
 * @since 2021-08-27T18:18:41.224
 */
@ApiModel(value = "签名表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("signature")
public class Signature extends Model<Signature> {

    /**
     * 主键
     */
           @TableId(value="id")
        @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 签名名称
     */
        @ApiModelProperty(value = "签名名称")
    private String name;
    /**
     * 签名编码
     */
        @ApiModelProperty(value = "签名编码")
    private String code;
    /**
     * 签名内容
     */
        @ApiModelProperty(value = "签名内容")
    private String content;
    /**
     * 备注
     */
        @ApiModelProperty(value = "备注")
    private String remark;
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

