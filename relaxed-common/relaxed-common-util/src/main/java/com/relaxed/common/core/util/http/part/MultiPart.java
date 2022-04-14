package com.relaxed.common.core.util.http.part;

import cn.hutool.core.collection.CollUtil;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

/**
 * 多资源组合资源<br>
 * 此资源为一个利用游标自循环资源，只有调用{@link #next()} 方法才会获取下一个资源，使用完毕后调用{@link #reset()}方法重置游标
 *
 * @author looly
 * @since 4.1.0
 */
public class MultiPart extends StreamPart implements Iterable<Part>, Iterator<Part>, Serializable {

	private final List<Part> parts;

	private int cursor;

	/**
	 * 构造
	 * @param resources 资源数组
	 */
	public MultiPart(Part... resources) {
		this(CollUtil.newArrayList(resources));
	}

	/**
	 * 构造
	 * @param resources 资源列表
	 */
	public MultiPart(Collection<Part> resources) {
		if (resources instanceof List) {
			this.parts = (List<Part>) resources;
		}
		else {
			this.parts = CollUtil.newArrayList(resources);
		}
	}

	/**
	 * 增加文件资源
	 * @param files 文件资源
	 * @return this
	 */
	public MultiPart add(File... files) {
		for (File file : files) {
			this.add(new FilePart(file.getName(), file));
		}
		return this;
	}

	/**
	 * 增加资源
	 * @param resource 资源
	 * @return this
	 */
	public MultiPart add(Part resource) {
		this.parts.add(resource);
		return this;
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public InputStream getStream() {
		return parts.get(cursor).getStream();
	}

	@Override
	public Iterator<Part> iterator() {
		return parts.iterator();
	}

	@Override
	public boolean hasNext() {
		return cursor < parts.size();
	}

	@Override
	public synchronized Part next() {
		if (cursor >= parts.size()) {
			throw new ConcurrentModificationException();
		}
		this.cursor++;
		return this;
	}

	@Override
	public void remove() {
		this.parts.remove(this.cursor);
	}

	/**
	 * 重置游标
	 */
	public synchronized void reset() {
		this.cursor = 0;
	}

}
