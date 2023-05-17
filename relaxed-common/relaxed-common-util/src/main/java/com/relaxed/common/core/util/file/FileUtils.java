package com.relaxed.common.core.util.file;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;

import com.relaxed.common.core.util.file.exception.FileNameLengthLimitExceededException;
import com.relaxed.common.core.util.file.exception.FileResultCode;
import com.relaxed.common.core.util.file.exception.FileSizeLimitExceededException;
import com.relaxed.common.core.util.file.exception.InvalidExtensionException;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author Yakir
 * @Topic FileUtils
 * @Description
 * @date 2022/11/27 12:14
 * @Version 1.0
 */
@UtilityClass
public class FileUtils {

	/**
	 * 文件上传
	 * @param basePath
	 * @param relativePath
	 * @param file
	 * @param fileConfig
	 * @return
	 */
	@SneakyThrows
	public FileMeta upload(String basePath, String relativePath, MultipartFile file, FileConfig fileConfig) {
		return upload(FileConstants.DEFAULT_HANDLE_TYPE, basePath, relativePath, file, fileConfig);
	}

	/**
	 * 上传文件
	 * @author yakir
	 * @date 2022/11/27 15:21
	 * @param handleType 文件处理类型 默认local 支持扩展
	 * @param basePath 基础存储路径 eg： /mnt/profile
	 * @param relativePath 相对文件存储路径 upload
	 * @param file
	 * @param fileConfig
	 * @return FileMeta 上传文件相关信息
	 */
	@SneakyThrows
	public FileMeta upload(String handleType, String basePath, String relativePath, MultipartFile file,
			FileConfig fileConfig) {
		String originalFilename = file.getOriginalFilename();
		int fileNameLength = originalFilename.length();
		if (fileNameLength > fileConfig.getMaxFilenameLength()) {
			throw new FileNameLengthLimitExceededException(FileResultCode.FILE_PARAM_ERROR.getCode(),
					fileConfig.getMaxFilenameLength());
		}

		assertAllowed(file, fileConfig);
		// 转换后 文件名称
		FileNameConverter fileNameConverter = Optional.ofNullable(fileConfig.getFileNameConverter())
				.orElse(FileUtils::extractFileName);
		String fileName = fileNameConverter.extractFileName(originalFilename);
		boolean splitDate = fileConfig.isSplitDate();
		String separator = fileConfig.getSeparator();
		String relativeFilePath;
		if (splitDate) {
			String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATE_PATTERN));
			relativeFilePath = relativePath + separator + dateStr;
		}
		else {
			relativeFilePath = relativePath;
		}
		String absolutePath = basePath + separator + relativeFilePath;
		String fileId = getFileHandler(handleType).upload(absolutePath, fileName, separator, file);
		FileMeta fileMeta = new FileMeta().setOriginalFilename(originalFilename).setFilename(fileName)
				.setSeperator(separator).setFileId(fileId).setBasePath(basePath).setRelativePath(relativeFilePath);
		return fileMeta;
	}

	/**
	 * 文件删除
	 * @param basePath
	 * @param relativePath
	 * @return
	 */
	public static boolean delete(String basePath, String relativePath) {
		return delete(FileConstants.DEFAULT_HANDLE_TYPE, basePath, relativePath);
	}

	/**
	 * 文件删除
	 * @param handleType 文件处理类型 默认local 支持扩展
	 * @param basePath 基础路径 /mnt
	 * @param relativePath 相对文件路径 /child/123.pdf
	 * @return
	 */
	public static boolean delete(String handleType, String basePath, String relativePath) {
		return getFileHandler(handleType).delete(basePath, relativePath);
	}

	/**
	 * 下载文件
	 * @param basePath
	 * @param relativePath
	 * @return
	 */
	public static void writeToStream(String basePath, String relativePath, OutputStream outputStream) {
		writeToStream(FileConstants.DEFAULT_HANDLE_TYPE, basePath, relativePath, outputStream);
	}

	/**
	 * 下载文件
	 * @param handleType 文件类型处理器
	 * @param basePath 基础路径
	 * @param relativePath 相对文件路径
	 */
	public static void writeToStream(String handleType, String basePath, String relativePath,
			OutputStream outputStream) {
		getFileHandler(handleType).writeToStream(basePath, relativePath, outputStream);
	}

	/**
	 * 下载字节
	 * @param basePath
	 * @param relativePath
	 * @return
	 */
	public static byte[] downloadByte(String basePath, String relativePath) {
		return downloadByte(FileConstants.DEFAULT_HANDLE_TYPE, basePath, relativePath);
	}

	/**
	 * 下载字节
	 * @param handleType 文件类型处理器
	 * @param basePath 基础路径
	 * @param relativePath 相对文件路径
	 * @return 字节数组
	 */
	public static byte[] downloadByte(String handleType, String basePath, String relativePath) {
		return getFileHandler(handleType).downloadByte(basePath, relativePath);
	}

	/**
	 * 编码文件名
	 */
	private String extractFileName(String originalFilename) {
		String extName = FileNameUtil.getSuffix(originalFilename);
		String filename = IdUtil.nanoId() + "." + extName;
		return filename;
	}

	/**
	 * 文件大小校验
	 * @param file 上传的文件
	 * @return
	 * @throws FileSizeLimitExceededException 如果超出最大大小
	 */
	private void assertAllowed(MultipartFile file, FileConfig fileConfig) throws FileSizeLimitExceededException {
		// 文件大小效验
		long size = file.getSize();
		Assert.isTrue(size <= fileConfig.getMaxSize(),
				() -> new FileSizeLimitExceededException(FileResultCode.FILE_PARAM_ERROR.getCode(),
						fileConfig.getMaxSize() / 1024 / 1024));
		String fileName = file.getOriginalFilename();
		// 扩展名效验
		String extension = FileUtil.getSuffix(fileName);
		String[] allowedExtension = fileConfig.getAllowedExtension();
		Assert.isTrue(ArrayUtil.contains(allowedExtension, extension),
				() -> new InvalidExtensionException(FileResultCode.FILE_PARAM_ERROR.getCode(), extension));
	}

	private FileHandler getDefaultFileHandler() {
		return getFileHandler(FileConstants.DEFAULT_HANDLE_TYPE);
	}

	private FileHandler getFileHandler(String supportType) {
		FileHandler load = FileHandlerLoader.load(supportType);
		Assert.notNull(load, "[" + supportType + "]文件处理器不存在");
		return load;
	}

}
