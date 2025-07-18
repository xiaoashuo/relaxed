package com.relaxed.common.core.util.file;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;

import cn.hutool.core.util.StrUtil;
import com.relaxed.common.core.util.PathHelper;
import com.relaxed.common.core.util.file.exception.FileNameLengthLimitExceededException;
import com.relaxed.common.core.util.file.exception.FileResultCode;
import com.relaxed.common.core.util.file.exception.FileSizeLimitExceededException;
import com.relaxed.common.core.util.file.exception.InvalidExtensionException;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
public class FileStoreUtils {

	/**
	 * 文件上传
	 * @param basePath
	 * @param relativePath
	 * @param file
	 * @return
	 */
	public FileMeta upload(String basePath, String relativePath, MultipartFile file) {
		return upload(FileConstants.DEFAULT_HANDLE_TYPE, basePath, relativePath, file);
	}

	/**
	 * 文件上传
	 * @param basePath
	 * @param relativePath
	 * @param file
	 * @param fileConfig
	 * @return
	 */
	public FileMeta upload(String basePath, String relativePath, MultipartFile file, FileConfig fileConfig) {
		return upload(FileConstants.DEFAULT_HANDLE_TYPE, basePath, relativePath, file, fileConfig);
	}

	/**
	 * 文件上传
	 * @param handleType
	 * @param basePath
	 * @param relativePath
	 * @param file
	 * @return
	 */
	public FileMeta upload(String handleType, String basePath, String relativePath, MultipartFile file) {
		return upload(handleType, basePath, relativePath, file, FileConfig.create());
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

		if (fileConfig.isFileCheck()) {
			assertAllowed(file, fileConfig);
		}
		// 转换后 文件名称
		FileNameConverter fileNameConverter = Optional.ofNullable(fileConfig.getFileNameConverter())
				.orElse(FileStoreUtils::extractFileName);
		String fileName = fileNameConverter.extractFileName(originalFilename);
		boolean splitDate = fileConfig.isSplitDate();
		String separator = fileConfig.getSeparator();
		String relativeFilePath;
		if (splitDate) {
			String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATE_PATTERN));
			if (StrUtil.isBlank(relativePath)) {
				relativeFilePath = dateStr;
			}
			else {
				relativeFilePath = relativePath + separator + dateStr;
			}
		}
		else {
			relativeFilePath = relativePath;
		}

		String absolutePath = StrUtil.isBlank(relativeFilePath) ? basePath : basePath + separator + relativeFilePath;
		String fileId = getFileHandler(handleType).upload(absolutePath, fileName, separator, file);
		FileMeta fileMeta = new FileMeta().setOriginalFilename(originalFilename).setFilename(fileName)
				.setSeperator(separator).setFileId(fileId).setBasePath(basePath).setRelativePath(relativeFilePath);
		return fileMeta;
	}

	/**
	 * 文件是否存在 本地处理器
	 * @param handleType 处理类型
	 * @param rootPath 根路径 /mnt
	 * @param relativePath 相对路径 /test.pdf
	 * @return 存在true 否则false
	 */
	public static boolean isExist(String handleType, String rootPath, String relativePath) {
		return isExist(handleType, rootPath, relativePath, File.separator);
	}

	/**
	 * 文件是否存在
	 * @param handleType 处理类型
	 * @param rootPath 根路径 /mnt
	 * @param relativePath 相对路径 /test.pdf
	 * @return 存在true 否则false
	 */
	public static boolean isExist(String handleType, String rootPath, String relativePath, String separator) {
		return getFileHandler(handleType).isExist(rootPath, relativePath, separator);
	}

	/**
	 * 文件删除
	 * @param handleType 文件处理类型 默认local 支持扩展
	 * @param basePath
	 * @param relativePath
	 * @return
	 */
	public static boolean delete(String handleType, String basePath, String relativePath) {
		return delete(handleType, basePath, relativePath, File.separator);
	}

	/**
	 * 文件删除
	 * @param handleType 文件处理类型 默认local 支持扩展
	 * @param basePath 基础路径 /mnt
	 * @param relativePath 相对文件路径 /child/123.pdf
	 * @param separator 路径分隔符
	 * @return
	 */
	public static boolean delete(String handleType, String basePath, String relativePath, String separator) {
		return getFileHandler(handleType).delete(basePath, relativePath, separator);
	}

	/**
	 * 下载文件
	 * @param handleType 文件类型处理器
	 * @param basePath
	 * @param relativePath
	 * @param outputStream 输出流
	 * @return
	 */
	public static void writeToStream(String handleType, String basePath, String relativePath,
			OutputStream outputStream) {
		writeToStream(handleType, basePath, relativePath, outputStream, File.separator);
	}

	/**
	 * 下载文件
	 * @param handleType 文件类型处理器
	 * @param basePath 基础路径
	 * @param relativePath 相对文件路径
	 * @param outputStream 输入流
	 * @param separator 路径分隔符
	 */
	public static void writeToStream(String handleType, String basePath, String relativePath, OutputStream outputStream,
			String separator) {
		getFileHandler(handleType).writeToStream(basePath, relativePath, outputStream, separator);
	}

	/**
	 * 下载字节
	 * @param handleType 文件类型处理器
	 * @param basePath
	 * @param relativePath
	 * @return
	 */
	public static byte[] downloadByte(String handleType, String basePath, String relativePath) {
		return downloadByte(handleType, basePath, relativePath, File.separator);
	}

	/**
	 * 下载字节
	 * @param handleType 文件类型处理器
	 * @param basePath 基础路径
	 * @param relativePath 相对文件路径
	 * @return 字节数组
	 */
	public static byte[] downloadByte(String handleType, String basePath, String relativePath, String separator) {
		return getFileHandler(handleType).downloadByte(basePath, relativePath, separator);
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
				() -> new InvalidExtensionException(FileResultCode.FILE_PARAM_ERROR.getCode(), "不支持的扩展名:" + extension));
	}

 

	private FileHandler getFileHandler(String supportType) {
		FileHandler load = FileHandlerLoader.load(supportType);
		Assert.notNull(load, "[" + supportType + "]文件处理器不存在");
		return load;
	}

	/**
	 * 下载文件名重新编码
	 * @param response 响应对象
	 * @param isPreview 是否预览模式 true 预览 false 下载
	 * @param realFileName 真实文件名
	 */
	public static void setAttachmentResponseHeader(HttpServletResponse response, boolean isPreview, String realFileName)
			throws UnsupportedEncodingException {
		String percentEncodedFileName = percentEncode(realFileName);

		StringBuilder contentDispositionValue = new StringBuilder();
		contentDispositionValue.append(isPreview ? "inline" : "attachment").append("; filename=")
				.append(percentEncodedFileName).append(";").append("filename*=").append("utf-8''")
				.append(percentEncodedFileName);

		response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
		response.setHeader("Content-disposition", contentDispositionValue.toString());
		response.setHeader("download-filename", percentEncodedFileName);
	}

	/**
	 * 百分号编码工具方法
	 * @param s 需要百分号编码的字符串
	 * @return 百分号编码后的字符串
	 */
	public static String percentEncode(String s) throws UnsupportedEncodingException {
		String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
		return encode.replaceAll("\\+", "%20");
	}

	/**
	 * 获取目标对象
	 * @param handleType 处理类型
	 * @param <T> 真实对象类型
	 * @param returnType 期望获取类型
	 * @return 获取代理真实对象
	 */
	public static <T> T getTargetObject(String handleType, Class<T> returnType) {
		FileHandler fileHandler = getFileHandler(handleType);
		Object targetObject = fileHandler.getTargetObject();

		Class<?> targetObjectClass = targetObject.getClass();
		Assert.isTrue(returnType.isAssignableFrom(targetObjectClass), "对象类型不匹配,期望:{},实际:{}", returnType.getName(),
				targetObjectClass.getName());
		return (T) targetObject;
	}

}
