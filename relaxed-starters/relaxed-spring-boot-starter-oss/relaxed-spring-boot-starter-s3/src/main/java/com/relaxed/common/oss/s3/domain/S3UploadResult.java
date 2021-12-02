package com.relaxed.common.oss.s3.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yakir
 * @Topic UploadResult
 * @Description
 * @date 2021/12/2 15:39
 * @Version 1.0
 */
@Builder
@Data
public class S3UploadResult {
    /**
     * eTag 标签
     */
    private String eTag;
    /**
     * 桶下目录名称
     */
    private String bucketName;
    /**
     * 相对路径
     */
    private String relativePath;
    /**
     * 下载url
     */
    private String downloadUrl;
}
