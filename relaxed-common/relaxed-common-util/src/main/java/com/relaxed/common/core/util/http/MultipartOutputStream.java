package com.relaxed.common.core.util.http;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.core.util.http.part.MultiPart;
import com.relaxed.common.core.util.http.part.Part;
import com.relaxed.common.core.util.http.part.StreamPart;
import com.relaxed.common.core.util.http.part.TextPart;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Multipart/form-data输出流封装<br>
 * 遵循RFC2388规范
 *
 * @author looly
 * @since 5.7.17
 */
public class MultipartOutputStream extends OutputStream {

	private static final String CONTENT_DISPOSITION_TEMPLATE = "Content-Disposition: form-data; name=\"{}\"\r\n";

	private static final String CONTENT_DISPOSITION_FILE_TEMPLATE = "Content-Disposition: form-data; name=\"{}\"; filename=\"{}\"\r\n";

	// 结束标记 位模板
	private static final String END_FLAG_TEMPLATE = "--{}--\r\n";

	private static final String CONTENT_TYPE_FILE_TEMPLATE = "Content-Type: {}\r\n";

	private static final String CONTENT_LENGTH_TEMPLATE = "Content-Length:  {}\r\n";

	private static final String TWO_HYPHENS = "--";

	private static final String LINE_END = "\r\n";

	private final OutputStream out;

	private final Charset charset;

	private final String boundary;

	private boolean isFinish;

	/**
	 * 构造，使用全局默认的边界字符串
	 * @param out HTTP写出流
	 * @param charset 编码
	 */
	public MultipartOutputStream(OutputStream out, Charset charset) {
		this(out, charset, com.relaxed.common.core.util.http.HttpGlobalConfig.getBoundary());
	}

	/**
	 * 构造
	 * @param out HTTP写出流
	 * @param charset 编码
	 * @param boundary 边界符
	 * @since 5.7.17
	 */
	public MultipartOutputStream(OutputStream out, Charset charset, String boundary) {
		this.out = out;
		this.charset = charset;
		this.boundary = boundary;
	}

	/**
	 * 添加Multipart表单的数据项<br>
	 * <pre>
	 *     --分隔符(boundary)[换行]
	 *     Content-Disposition: form-data; name="参数名"[换行]
	 *     [换行]
	 *     参数值[换行]
	 * </pre>
	 * <p>
	 * 或者：
	 *
	 * <pre>
	 *     --分隔符(boundary)[换行]
	 *     Content-Disposition: form-data; name="表单名"; filename="文件名"[换行]
	 *     Content-Type: MIME类型[换行]
	 *     [换行]
	 *     文件的二进制内容[换行]
	 * </pre>
	 * @param formFieldName 表单名
	 * @param value 值，可以是普通值、资源（如文件等）
	 * @return this
	 * @throws IORuntimeException IO异常
	 */
	public MultipartOutputStream write(String formFieldName, Object value) throws IORuntimeException {

		if (value instanceof MultiPart) {
			for (Part subPart : (MultiPart) value) {
				write(formFieldName, subPart);
			}
			return this;
		}
		beginPart();
		if (value instanceof Part) {
			appendPart(formFieldName, (Part) value);
		}
		else {
			appendPart(formFieldName, new TextPart(Convert.toStr(value), null, charset));
		}

		write(LINE_END);
		return this;
	}

	@Override
	public void write(int b) throws IOException {
		this.out.write(b);
	}

	/**
	 * 上传表单结束
	 * @throws IORuntimeException IO异常
	 */
	public void finish() throws IORuntimeException {
		if (false == isFinish) {
			write(StrUtil.format(END_FLAG_TEMPLATE, boundary));
			this.isFinish = true;
		}
	}

	@Override
	public void close() {
		finish();
		IoUtil.close(this.out);
	}

	private void appendPart(String formFieldName, Part part) {
		StringBuffer strBuf = new StringBuffer();
		if (part instanceof StreamPart) {
			String fileName = ((StreamPart) part).getFileName();
			// Content-Disposition
			strBuf.append(StrUtil.format(CONTENT_DISPOSITION_FILE_TEMPLATE, formFieldName, fileName));
		}
		else {
			// Content-Disposition
			strBuf.append(StrUtil.format(CONTENT_DISPOSITION_TEMPLATE, formFieldName));
		}
		String contentType = part.getContentType();
		if (StrUtil.isNotEmpty(contentType)) {
			strBuf.append(StrUtil.format(CONTENT_TYPE_FILE_TEMPLATE, contentType));
		}
		strBuf.append(LINE_END);
		String contentDescription = strBuf.toString();
		// 内容描述
		write(contentDescription);
		part.writeTo(this);

	}

	/**
	 * part开始，写出:<br>
	 * <pre>
	 *     --分隔符(boundary)[换行]
	 * </pre>
	 */
	private void beginPart() {
		// --分隔符(boundary)[换行]
		write("--", boundary, LINE_END);
	}

	/**
	 * 写出对象
	 * @param objs 写出的对象（转换为字符串）
	 */
	private void write(Object... objs) {
		IoUtil.write(this, this.charset, false, objs);
	}

}
