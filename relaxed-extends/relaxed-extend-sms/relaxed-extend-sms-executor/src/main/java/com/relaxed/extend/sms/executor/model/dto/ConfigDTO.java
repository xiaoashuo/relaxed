package com.relaxed.extend.sms.executor.model.dto;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import lombok.experimental.Accessors;

/**
 * 配置表 数据传输对象
 *
 * @author Yakir
 * @since 2021-08-27T14:55:25.330
 */
@ApiModel(value = "配置表")
@Data
@Accessors(chain = true)
public class ConfigDTO implements Serializable  {

    /**
     * 主键
     */
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
    private Map<String,String> extend;


}

