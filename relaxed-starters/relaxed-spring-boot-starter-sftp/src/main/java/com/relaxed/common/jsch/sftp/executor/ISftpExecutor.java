package com.relaxed.common.jsch.sftp.executor;

import com.jcraft.jsch.ChannelSftp;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author Yakir
 * @Topic ISftp
 * @Description
 * @date 2022/5/25 10:07
 * @Version 1.0
 */
public interface ISftpExecutor {

	/**
	 * 获取远程文件的输入流
	 * @param absoluteFilePath 绝对文件路径
	 * @return 远程文件流
	 */
	InputStream getInputStream(String absoluteFilePath);

	/**
	 * 获取远程文件的输入流
	 * @param dir 文件目录
	 * @param name 文件名
	 * @return 远程文件流
	 */
	InputStream getInputStream(String dir, String name);

	/**
	 * 下载远程文件
	 * @param dir 文件目录
	 * @param name 文件名
	 * @return 文件字节数组
	 */
	byte[] download(String dir, String name);

	/**
	 * 下载远程文件 到指定文件
	 * @param dir 远程目录
	 * @param name 远程文件名
	 * @param file 写入文件
	 * @return
	 */
	File download(String dir, String name, File file);

	/**
	 * 下载远程文件 到指定文件
	 * @param absoluteFilePath 全名称文件路径
	 * @param file 写入文件
	 * @return
	 */
	File download(String absoluteFilePath, File file);

	/**
	 * 上传文件
	 * @param dir 远程目录
	 * @param name 远程文件名
	 * @param in 输入流
	 */
	void upload(String dir, String name, InputStream in);

	/**
	 * 上传文件
	 * @param dir 远程目录
	 * @param name 远程文件名
	 * @param file 文件句柄
	 */
	void upload(String dir, String name, File file);

	/**
	 * 上传文件
	 * @param dir 远程目录
	 * @param name 远程文件名
	 * @param src 本地文件路径
	 */
	void upload(String dir, String name, String src);

	/**
	 * 删除文件 若为文件夹 则清空文件夹 若为文件路径 则删除文件
	 * @param path 文件路径
	 */
	void delete(String path);

	/**
	 * 删除文件
	 * @param dir 远程目录
	 * @param name 远程文件名
	 */
	void delete(String dir, String name);

	/**
	 * 递归创建目录
	 * @param dir 目录绝对路径
	 */
	void mkdirs(String dir);

	/**
	 * 判断文件或目录是否存在
	 * @param path 文件或目录路径
	 * @return {@code true} 存在 {@code false} 不存在
	 */
	boolean isExist(String path);

	/**
	 * 判断是否目录
	 * @param path 待判断的路径
	 * @return {@code true} 是目录 {@code false} 不是目录
	 */
	boolean isDir(String path);

	/**
	 * 查看远程目录下的文件和目录
	 * @param path 远程目录路径
	 * @return 目录下的文件和目录名称集合
	 */
	List<String> list(String path);

	/**
	 * 移动或重命名文件
	 * @param src 源文件
	 * @param target 目标文件
	 */
	void move(String src, String target);

	/**
	 * 移动文件或重命名
	 * @param src 源文件
	 * @param target 目标路径
	 * @param fileName 目标文件名称
	 */
	void move(String src, String target, String fileName);

	/**
	 * 修改权限
	 * @param permissions 权限，三位0-7的数字
	 * @param path 绝对路径
	 */
	void chmod(String permissions, String path);

	/**
	 * 获取channel sftp 仅允许 同包路径或子类调用
	 * @return
	 */
	ChannelSftp getChannelSftp();

}
