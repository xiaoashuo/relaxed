package com.relaxed.common.core.util.http.part;

import java.io.File;
import java.util.Collection;

public class MultiFilePart extends MultiPart {

	/**
	 * 构造
	 * @param files 文件资源列表
	 */
	public MultiFilePart(Collection<File> files) {
		add(files);
	}

	/**
	 * 构造
	 * @param files 文件资源列表
	 */
	public MultiFilePart(File... files) {
		add(files);
	}

	/**
	 * 增加文件资源
	 * @param files 文件资源
	 * @return this
	 */
	public MultiFilePart add(File... files) {
		for (File file : files) {
			this.add(new FilePart(file));
		}
		return this;
	}

	/**
	 * 增加文件资源
	 * @param files 文件资源
	 * @return this
	 */
	public MultiFilePart add(Collection<File> files) {
		for (File file : files) {
			this.add(new FilePart(file));
		}
		return this;
	}

	@Override
	public MultiFilePart add(Part part) {
		return (MultiFilePart) super.add(part);
	}

}
