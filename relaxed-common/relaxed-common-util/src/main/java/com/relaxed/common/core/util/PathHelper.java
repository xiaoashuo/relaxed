package com.relaxed.common.core.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.ToString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic Path
 * @Description 路径工具类
 * @date 2023/7/20 10:50
 * @Version 1.0
 */
@ToString
public class PathHelper {

	/**
	 * 核心路径
	 */
	private String profile;

	/**
	 * 文件名称
	 */
	private String fileName;

	/**
	 * 目录路径
	 */
	private String dirPath;

	/**
	 * 全路径
	 */
	private String fullPath;

	public PathHelper() {

	}

	public PathHelper(String profile, String dirPath, String fileName, String fullPath) {
		this.profile = profile;
		this.dirPath = dirPath;
		this.fileName = fileName;
		this.fullPath = fullPath;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDirPath() {
		return dirPath;
	}

	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public static PathBuilder builder() {
		return new PathBuilder(System.getProperty("user.dir"));
	}

	public static PathBuilder builder(String profile) {
		return new PathBuilder(profile);
	}

	public static class PathBuilder {

		/**
		 * 核心路径
		 */
		private String profile;

		/**
		 * 是否携带根路径
		 */
		private boolean withRootPath = true;

		private String separator = File.separator;

		/**
		 * 安全检查
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

		private PathBuilder() {

		}

		private PathBuilder(String profile) {
			this.profile = profile;
		}

		public String getProfile() {
			return profile;
		}

		public PathBuilder separator(String separator) {
			this.separator = separator;
			return this;
		}

		public PathBuilder safeCheck(boolean safeCheck) {
			this.safeCheck = safeCheck;
			return this;
		}

		public PathBuilder addPath(String path) {
			this.dirs.add(path);
			return this;
		}

		public PathBuilder addPaths(String... paths) {
			this.dirs.addAll(CollectionUtil.toList(paths));
			return this;
		}

		public PathBuilder addPaths(List<String> paths) {
			this.dirs.addAll(paths);
			return this;
		}

		public PathBuilder filename(String fileName) {
			this.fileName = fileName;
			return this;
		}

		public String getSeparator() {
			return separator;
		}

		public boolean isSafeCheck() {
			return safeCheck;
		}

		public String getFileName() {
			return fileName;
		}

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
		 * @return
		 */
		public String getDirPath() {
			return dirPath;
		}

		/**
		 * 获取全路径
		 * @return
		 */
		public String getFullPath() {
			return StrUtil.isEmpty(fileName) ? dirPath : dirPath + separator + fileName;
		}

	}

}
