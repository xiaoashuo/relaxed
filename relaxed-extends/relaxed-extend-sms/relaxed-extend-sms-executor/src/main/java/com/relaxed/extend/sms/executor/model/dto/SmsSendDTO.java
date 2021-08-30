package com.relaxed.extend.sms.executor.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic SmsSendDTO
 * @Description
 * @date 2021/8/27 17:42
 * @Version 1.0
 */
@Data
public class SmsSendDTO {
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("模板编码")
    private String template;
    @ApiModelProperty("签名编码")
    private String signature;
    @ApiModelProperty("参数")
    private Map<String, String> params;
    @ApiModelProperty("通道配置编码")
    private List<String> configIds;
    @ApiModelProperty("定时时间 yyyy-MM-dd HH:mm")
    private String sendTime;
    @ApiModelProperty("日志主键")
    private String logId;
    @ApiModelProperty("批次编码")
    private String batchCode;
}
