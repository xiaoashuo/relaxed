package com.relaxed.common.core.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.ToString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 路径处理工具类，提供灵活的文件路径构建和管理功能。
 *
 * 支持以下特性： 1. 基于构建器模式的路径组装 2. 自动创建目录 3. 路径安全检查 4. 灵活的分隔符配置 5. 支持相对和绝对路径处理
 *
 * 使用示例： <pre>
 * // 创建路径构建器
 * PathHelper.PathBuilder builder = PathHelper.builder();
 *
 * // 添加路径组件
 * builder.addPath("dir1").addPath("dir2");
 *
 * // 设置文件名
 * builder.filename("file.txt");
 *
 * // 构建路径
 * PathHelper path = builder.build();
 * </pre>
 *
 * @author Yakir
 * @since 1.0
 */
@ToString
public class PathHelper {

	/**
	 * 核心路径，通常为应用根目录
	 */
	private String profile;

	/**
	 * 文件名称，表示路径末尾的文件名
	 */
	private String fileName;

	/**
	 * 目录路径，不包含文件名的完整目录路径
	 */
	private String dirPath;

	/**
	 * 全路径，包含目录路径和文件名的完整路径
	 */
	private String fullPath;

	/**
	 * 默认构造函数
	 */
	public PathHelper() {
	}

	/**
	 * 带参数的构造函数
	 * @param profile 核心路径
	 * @param dirPath 目录路径
	 * @param fileName 文件名称
	 * @param fullPath 全路径
	 */
	public PathHelper(String profile, String dirPath, String fileName, String fullPath) {
		this.profile = profile;
		this.dirPath = dirPath;
		this.fileName = fileName;
		this.fullPath = fullPath;
	}

	/**
	 * 获取核心路径
	 * @return 核心路径
	 */
	public String getProfile() {
		return profile;
	}

	/**
	 * 设置核心路径
	 * @param profile 核心路径
	 */
	public void setProfile(String profile) {
		this.profile = profile;
	}

	/**
	 * 获取文件名称
	 * @return 文件名称
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 设置文件名称
	 * @param fileName 文件名称
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 获取目录路径
	 * @return 目录路径
	 */
	public String getDirPath() {
		return dirPath;
	}

	/**
	 * 设置目录路径
	 * @param dirPath 目录路径
	 */
	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

	/**
	 * 获取全路径
	 * @return 全路径
	 */
	public String getFullPath() {
		return fullPath;
	}

	/**
	 * 设置全路径
	 * @param fullPath 全路径
	 */
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	/**
	 * 创建路径构建器，使用当前工作目录作为核心路径
	 * @return 路径构建器
	 */
	public static PathBuilder builder() {
		return new PathBuilder(System.getProperty("user.dir"));
	}

	/**
	 * 创建路径构建器，使用指定的核心路径
	 * @param profile 核心路径
	 * @return 路径构建器
	 */
	public static PathBuilder builder(String profile) {
		return new PathBuilder(profile);
	}

	/**
	 * 路径构建器，用于构建文件路径
	 */
	public static class PathBuilder {

		/**
		 * 核心路径
		 */
		private String profile;

		/**
		 * 是否携带根路径
		 */
		private boolean withRootPath = true;

		/**
		 * 路径分隔符
		 */
		private String separator = File.separator;

		/**
		 * 是否进行安全检查
		 */
		private boolean safeCheck = true;

		/**
		 * 合成路径
		 */
		private List<String> dirs = new ArrayList<>();

		/**
		 * 文件名称
		 */
		private String fileName;

		/**
		 * 目录路径
		 */
		private String dirPath;

		/**
		 * 默认构造函数
		 */
		private PathBuilder() {
		}

		/**
		 * 带参数的构造函数
		 * @param profile 核心路径
		 */
		private PathBuilder(String profile) {
			this.profile = profile;
		}

		/**
		 * 获取核心路径
		 * @return 核心路径
		 */
		public String getProfile() {
			return profile;
		}

		/**
		 * 设置路径分隔符
		 * @param separator 路径分隔符
		 * @return 当前构建器
		 */
		public PathBuilder separator(String separator) {
			this.separator = separator;
			return this;
		}

		/**
		 * 设置是否进行安全检查
		 * @param safeCheck 是否进行安全检查
		 * @return 当前构建器
		 */
		public PathBuilder safeCheck(boolean safeCheck) {
			this.safeCheck = safeCheck;
			return this;
		}

		/**
		 * 添加路径组件
		 * @param path 路径组件
		 * @return 当前构建器
		 */
		public PathBuilder addPath(String path) {
			this.dirs.add(path);
			return this;
		}

		/**
		 * 添加多个路径组件
		 * @param paths 路径组件数组
		 * @return 当前构建器
		 */
		public PathBuilder addPaths(String... paths) {
			this.dirs.addAll(CollectionUtil.toList(paths));
			return this;
		}

		/**
		 * 添加多个路径组件
		 * @param paths 路径组件列表
		 * @return 当前构建器
		 */
		public PathBuilder addPaths(List<String> paths) {
			this.dirs.addAll(paths);
			return this;
		}

		/**
		 * 设置文件名称
		 * @param fileName 文件名称
		 * @return 当前构建器
		 */
		public PathBuilder filename(String fileName) {
			this.fileName = fileName;
			return this;
		}

		/**
		 * 获取路径分隔符
		 * @return 路径分隔符
		 */
		public String getSeparator() {
			return separator;
		}

		/**
		 * 获取是否进行安全检查
		 * @return 是否进行安全检查
		 */
		public boolean isSafeCheck() {
			return safeCheck;
		}

		/**
		 * 获取文件名称
		 * @return 文件名称
		 */
		public String getFileName() {
			return fileName;
		}

		/**
		 * 构建路径
		 * @return 路径对象
		 */
		public PathHelper build() {
			if (CollectionUtil.isEmpty(dirs)) {
				this.dirPath = withRootPath ? profile : "";
			}
			else {
				StringBuilder sb = new StringBuilder();
				for (String dir : dirs) {
					String realDir = StrUtil.removeSuffixIgnoreCase(dir, separator);
					sb.append(realDir).append(separator);
				}
				String suffixDir = sb.deleteCharAt(sb.length() - 1).toString();
				this.dirPath = withRootPath ? profile + separator + suffixDir : suffixDir;
			}
			if (safeCheck) {
				FileUtil.mkdir(this.dirPath);
			}
			return new PathHelper(this.profile, this.dirPath, this.fileName, this.getFullPath());
		}

		/**
		 * 获取目录路径
		 * @return 目录路径
		 */
		public String getDirPath() {
			return dirPath;
		}

		/**
		 * 获取全路径
		 * @return 全路径
		 */
		public String getFullPath() {
			return StrUtil.isEmpty(fileName) ? dirPath : dirPath + separator + fileName;
		}

	}

}
