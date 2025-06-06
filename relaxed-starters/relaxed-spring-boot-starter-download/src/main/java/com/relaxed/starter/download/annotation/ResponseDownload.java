package com.relaxed.starter.download.annotation;

import com.relaxed.starter.download.enums.DownTypeEnum;
import com.relaxed.starter.download.handler.DownloadHandler;

import java.lang.annotation.*;

/**
 * 响应下载注解，用于标记需要下载文件的方法。 可以配置下载渠道、内容类型、字符编码等参数。 支持本地文件、SFTP和OSS等多种下载方式。
 *
 * @author Yakir
 * @since 1.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ResponseDownload {

	/**
	 * 下载渠道类型 支持本地文件系统、SFTP服务器和OSS对象存储
	 * @return 下载渠道类型，默认为本地文件系统
	 */
	String channel() default DownTypeEnum.LOCAL;

	/**
	 * 是否内联显示 如果为true，文件可以在浏览器中直接预览，如图片或视频 需要配合contentType使用
	 * @return true 表示内联显示，false 表示作为附件下载，默认为false
	 */
	boolean inline() default false;

	/**
	 * 内容类型 如果未指定，会尝试根据文件扩展名自动获取 如果自动获取失败，则使用默认值application/octet-stream
	 * @return 内容类型，默认为application/octet-stream
	 */
	String contentType() default "application/octet-stream";

	/**
	 * 字符编码 用于处理包含中文的文本文件下载
	 * @return 字符编码，默认为UTF-8
	 */
	String charset() default "UTF-8";

	/**
	 * 自定义响应头 数组中的奇数位为响应头名称，偶数位为响应头值 例如：{"Cache-Control", "no-cache", "Pragma",
	 * "no-cache"}
	 * @return 响应头数组，默认为空数组
	 */
	String[] headers() default {};

}
