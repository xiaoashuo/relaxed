package com.relaxed.common.http.core.resource;

import java.io.InputStream;

/**
 * @author Yakir
 * @Topic Resource
 * @Description
 * @date 2022/5/23 15:04
 * @Version 1.0
 */
public interface Resource {

	/**
	 * 得到表单key名称
	 * @author yakir
	 * @date 2022/5/23 15:05
	 * @return java.lang.String
	 */
	String getName();

	/**
	 * 得到文件名
	 * @author yakir
	 * @date 2022/5/23 15:05
	 * @return java.lang.String
	 */
	String getFileName();

	/**
	 * 得到内容类型
	 * @author yakir
	 * @date 2022/5/23 15:06
	 * @return java.lang.String
	 */
	default String getContentType() {
		return null;
	}

	/**
	 * 获取输入流 注意此处流的释放 不然句柄会一直被占用 当前默认方案请求完 流会被释放
	 * {@Link cn.hutool.core.io.resource.InputStreamResource#writeTo}
	 * @author yakir
	 * @date 2022/5/23 15:10
	 * @return java.io.InputStream
	 */
	InputStream getStream();

}
