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
	 * 上传文件
	 * @author yakir
	 * @date 2022/11/27 15:21
	 * @param basePath 基础存储路径 eg： /mnt/profile
	 * @param relativePath 相对文件存储路径 upload
	 * @param file
	 * @param fileConfig
	 * @return FileMeta 上传文件相关信息
	 */
	@SneakyThrows
	public FileMeta upload(String basePath, String relativePath, MultipartFile file, FileConfig fileConfig) {
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
			relativeFilePath = relativePath +separator + dateStr;
		}
		else {
			relativeFilePath = relativePath;
		}
		String absolutePath = basePath + separator+ relativeFilePath;
		File desc = getAbsoluteFile(separator,absolutePath, fileName);
		file.transferTo(desc);
		//fileConfig.getFileHandler().upload(absolutePath, fileName,separator,file);
		String fileId = IdUtil.getSnowflakeNextId() + "";
		FileMeta fileMeta = new FileMeta().setOriginalFilename(originalFilename).setFilename(fileName)
				.setSeperator(separator).setFileId(fileId)
				.setBasePath(basePath).setRelativePath(relativeFilePath);
		return fileMeta;
	}

	private File getAbsoluteFile(String separator,String dirPath, String fileName) {
		File desc = new File(dirPath + separator + fileName);

		if (!desc.exists()) {
			if (!desc.getParentFile().exists()) {
				desc.getParentFile().mkdirs();
			}
		}
		return desc;
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
		//文件大小效验
		long size = file.getSize();
		Assert.isTrue(size <= fileConfig.getMaxSize(),
				() -> new FileSizeLimitExceededException(FileResultCode.FILE_PARAM_ERROR.getCode(),
						fileConfig.getMaxSize() / 1024 / 1024));
		String fileName = file.getOriginalFilename();
		//扩展名效验
		String extension = FileUtil.getSuffix(fileName);
		String[] allowedExtension = fileConfig.getAllowedExtension();
		Assert.isTrue(ArrayUtil.contains(allowedExtension,extension),
				() -> new InvalidExtensionException(FileResultCode.FILE_PARAM_ERROR.getCode(),
						extension));


	}

	public static File download(String basePath, String relativePath) {
		return new File(basePath, relativePath);
	}

	public static boolean delete(String basePath, String relativePath) {
		return FileUtil.del(basePath + relativePath);
	}

}
