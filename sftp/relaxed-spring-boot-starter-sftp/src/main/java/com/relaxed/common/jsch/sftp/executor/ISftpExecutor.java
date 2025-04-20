package com.relaxed.common.jsch.sftp.executor;

import com.jcraft.jsch.ChannelSftp;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * SFTP执行器接口
 *
 * @author Yakir
 */
public interface ISftpExecutor {

	/**
	 * 获取远程文件的输入流
	 * @param absoluteFilePath 绝对文件路径
	 * @return 远程文件输入流
	 */
	InputStream getInputStream(String absoluteFilePath);

	/**
	 * 获取远程文件的输入流
	 * @param dir 文件目录
	 * @param name 文件名
	 * @return 远程文件输入流
	 */
	InputStream getInputStream(String dir, String name);

	/**
	 * 下载远程文件到字节数组
	 * @param dir 远程目录
	 * @param name 远程文件名
	 * @return 文件内容字节数组
	 */
	byte[] download(String dir, String name);

	/**
	 * 下载远程文件到字节数组
	 * @param absoluteFilePath 远程文件绝对路径
	 * @return 文件内容字节数组
	 */
	byte[] download(String absoluteFilePath);

	/**
	 * 下载远程文件到指定文件
	 * @param dir 远程目录
	 * @param name 远程文件名
	 * @param file 本地文件
	 * @return 下载后的本地文件
	 */
	File download(String dir, String name, File file);

	/**
	 * 下载远程文件到指定文件
	 * @param absoluteFilePath 远程文件绝对路径
	 * @param file 本地文件
	 * @return 下载后的本地文件
	 */
	File download(String absoluteFilePath, File file);

	/**
	 * 上传文件到远程服务器
	 * @param dir 远程目录
	 * @param name 远程文件名
	 * @param in 文件输入流
	 */
	void upload(String dir, String name, InputStream in);

	/**
	 * 上传文件到远程服务器
	 * @param dir 远程目录
	 * @param name 远程文件名
	 * @param file 本地文件
	 */
	void upload(String dir, String name, File file);

	/**
	 * 上传文件到远程服务器
	 * @param dir 远程目录
	 * @param name 远程文件名
	 * @param src 本地文件路径
	 */
	void upload(String dir, String name, String src);

	/**
	 * 删除远程文件或目录
	 * @param path 远程文件或目录路径
	 */
	void delete(String path);

	/**
	 * 删除远程文件
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
	 * 查看远程目录下的文件和目录
	 * @param path 远程目录路径
	 * @param filterFunction 过滤函数 文件属性-&gt; true ; 返回true 则当前寻找到文件需要保存 返回false 则跳过当前文件目录存储
	 * @return 目录下的文件和目录名称集合
	 */
	List<String> list(String path, Function<FileAttr, Boolean> filterFunction);

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
	 * 获取文件大小
	 * @param path 文件路径
	 * @return 文件大小
	 */
	long filesize(String path);

	/**
	 * 获取SFTP通道
	 * @return SFTP通道
	 */
	ChannelSftp getChannelSftp();

}
