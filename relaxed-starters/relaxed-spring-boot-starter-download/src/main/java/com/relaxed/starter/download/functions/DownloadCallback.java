package com.relaxed.starter.download.functions;

/**
 * 下载回调函数接口，用于在文件下载完成后执行一些收尾工作。 这是一个函数式接口，可以使用Lambda表达式实现。
 *
 * @author Yakir
 * @since 1.0
 */
@FunctionalInterface
public interface DownloadCallback {

	/**
	 * 在文件下载完成后执行的后置处理操作 例如：清理临时文件、更新下载记录等
	 */
	void postProcess();

}
