package com.relaxed.common.core.util.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

/**
 * @author Yakir
 * @Topic FileHandler
 * @Description
 * @date 2023/5/17 11:39
 * @Version 1.0
 */
public interface FileHandler {

	/**
	 * 支持处理类型
	 * @return
	 */
	String supportType();

	/**
	 * 上传文件
	 * @date 2023/5/17 11:49
	 * @param dirPath 处理后目录路径 <pre>
	 *    basePath: /mnt
	 *    relativePath: child
	 *    splitDate: true|false
	 *    dirPath: 若splitDate => true 则 /mnt/child/20220301
	 *             若splitDate => false则 /mnt/child
	 * </pre> /mnt/test
	 * @param filename 处理后文件名称 eg: test.pdf
	 * @param separator 路径分隔符 /
	 * @param file 文件
	 * @return java.lang.String 唯一文件id标识 eg:11111122444
	 */
	String upload(String dirPath, String filename, String separator, MultipartFile file);

	/**
	 * 判断文件是否存在
	 * @param rootPath 根路径/mnt
	 * @param relativePath 文件全相对路径/child/test.pdf
	 * @param separator 路径分隔符
	 * @return 存在true 否则false
	 */
	boolean isExist(String rootPath, String relativePath, String separator);

	/**
	 * 文件删除
	 * @param rootPath 根目录路径 /mnt
	 * @param relativePath 文件全相对路径 /child/test.pdf
	 * @param separator
	 * @return boolean 是否成功true|false
	 * @date 2023/5/17 11:52
	 */
	boolean delete(String rootPath, String relativePath, String separator);

	/**
	 * 写入到流
	 * @param rootPath 根目录路径 /mnt
	 * @param relativePath 文件全相对路径 /child/test.pdf
	 * @param outputStream
	 * @param separator
	 */
	void writeToStream(String rootPath, String relativePath, OutputStream outputStream, String separator);

	/**
	 * 写入到字节
	 * @param rootPath
	 * @param relativePath
	 * @param separator
	 * @return
	 */
	byte[] downloadByte(String rootPath, String relativePath, String separator);

	/**
	 * 获取目标对象
	 * @return 获取代理真实对象
	 */
	Object getTargetObject();

}
