package com.relaxed.starter.download.annotation;

import com.relaxed.starter.download.enums.DownTypeEnum;
import com.relaxed.starter.download.handler.DownloadHandler;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic Download
 * @Description
 * @date 2022/2/17 11:39
 * @Version 1.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ResponseDownload {

	/**
	 * 下载渠道 支持 local sftp oss
	 * @return
	 */
	DownTypeEnum channel() default DownTypeEnum.LOCAL;

	/**
	 * 自定义处理器
	 */
	Class<? extends DownloadHandler> customHandler() default DownloadHandler.class;

	/**
	 * 如果为true，可以直接在浏览器预览 需要配合contentType，如图片或视频，默认false
	 */
	boolean inline() default false;

	/**
	 * 如果未指定，会尝试获取 如果尝试获取失败，则默认application/octet-stream
	 */
	String contentType() default "application/octet-stream";

	/**
	 * 如果下载包含中文的文本文件出现乱码，可以尝试指定编码
	 */
	String charset() default "UTF-8";

	/**
	 * 响应头 基数位为key 偶数位 为value
	 */
	String[] headers() default {};

}
