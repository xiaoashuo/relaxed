package com.relaxed.test.utils.request;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Yakir
 * @Topic ReqRecord
 * @Description
 * @date 2024/3/5 11:53
 * @Version 1.0
 */
@Slf4j
public class RequestFactory {

	private Map<String, Object> paramMap = new HashMap<>();

	private String body;

	private Method method;

	private String url;

	private String result;

	/**
	 * 请求开始时间
	 */
	private Long startTime;

	/**
	 * 请求结束时间
	 */
	private Long endTime;

	/**
	 * 方法功能描述
	 */
	private String desc;

	/**
	 * 请求头
	 */
	protected Map<String, List<String>> headers = new HashMap();

	/**
	 * 异常
	 */
	private Throwable throwable;

	/**
	 * 是否记录 异常 记录 不排除 否则抛出
	 */
	private boolean isThrowableExp = true;

	private RequestConfig requestConfig = new RequestConfig(this);

	private static final PostCheck DEFAULT_POST_CHECK = (factory, result) -> false;

	public interface RetConvert<T> {

		T convert(String result);

	}

	public interface PostCheck {

		/**
		 * 是否重新执行方法
		 * @param result
		 * @return true 重试 false 不重试
		 */
		boolean isRetryRun(RequestFactory factory, String result);

		/**
		 * 等待重试毫秒数
		 * @return
		 */
		default long getWaitRetryMills() {
			return 1000L;
		}

		/**
		 * 等待重试次数
		 * @return
		 */
		default int getRetryCount() {
			return 3;
		}

	}

	private RequestFactory(String url, Method method) {
		this.url = url;
		this.method = method;
	}

	public static RequestFactory create(String url, Method method) {
		return new RequestFactory(url, method);
	}

	public RequestFactory body(String body) {
		this.body = body;
		return this;
	}

	public RequestFactory url(String url) {
		this.url = url;
		return this;
	}

	public RequestFactory form(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
		return this;
	}

	public Map<String, Object> form() {
		return this.paramMap;
	}

	public RequestFactory result(String result) {
		this.result = result;
		return this;
	}

	public RequestFactory desc(String desc) {
		this.desc = desc;
		return this;
	}

	public RequestFactory isThrowableExp(boolean isThrowableExp) {
		this.isThrowableExp = isThrowableExp;
		return this;
	}

	public RequestConfig config() {
		return this.requestConfig;
	}

	public String getReqParam() {
		if (StrUtil.isEmpty(body) && MapUtil.isEmpty(paramMap)) {
			return null;
		}
		if (StrUtil.isEmpty(body)) {
			return paramMap.toString();
		}
		return body;
	}

	public String getResult() {
		return this.result;
	}

	public <T> T wrapExecute(RetConvert<T> retConvert) {
		return wrapExecute(retConvert, DEFAULT_POST_CHECK);
	}

	public <T> T wrapExecute(RetConvert<T> retConvert, PostCheck postCheck) {
		String result = this.execute();
		// 后置检查 如果返回true 再次运行一次 则 不在重新执行

		boolean retryRun = postCheck.isRetryRun(this, result);
		if (!retryRun) {
			return retConvert.convert(result);
		}
		long waitRetryMills = postCheck.getWaitRetryMills();
		int retryCount = postCheck.getRetryCount();
		int index = 0;
		while (index < retryCount) {
			ThreadUtil.safeSleep(waitRetryMills);
			index++;
			log.info("开始执行{}次重试", index);
			result = this.execute();
			if (!postCheck.isRetryRun(this, result)) {
				break;
			}

		}
		return retConvert.convert(result);
	}

	/**
	 * 设置一个header<br>
	 * 如果覆盖模式，则替换之前的值，否则加入到值列表中
	 * @param name Header名
	 * @param value Header值
	 * @param isOverride 是否覆盖已有值
	 * @return T 本身
	 */
	public RequestFactory header(Header name, String value, boolean isOverride) {
		return header(name.toString(), value, isOverride);
	}

	/**
	 * 设置一个header<br>
	 * 覆盖模式，则替换之前的值
	 * @param name Header名
	 * @param value Header值
	 * @return T 本身
	 */
	public RequestFactory header(Header name, String value) {
		return header(name.toString(), value, true);
	}

	/**
	 * 设置一个header<br>
	 * 覆盖模式，则替换之前的值
	 * @param name Header名
	 * @param value Header值
	 * @return T 本身
	 */
	public RequestFactory header(String name, String value) {
		return header(name, value, true);
	}

	/**
	 * 设置请求头
	 * @param headers 请求头
	 * @param isOverride 是否覆盖已有头信息
	 * @return this
	 * @since 4.6.3
	 */
	public RequestFactory headerMap(Map<String, String> headers, boolean isOverride) {
		if (MapUtil.isEmpty(headers)) {
			return this;
		}

		for (Map.Entry<String, String> entry : headers.entrySet()) {
			this.header(entry.getKey(), StrUtil.nullToEmpty(entry.getValue()), isOverride);
		}
		return this;
	}

	/**
	 * 设置请求头<br>
	 * 不覆盖原有请求头
	 * @param headers 请求头
	 * @return this
	 */
	public RequestFactory header(Map<String, List<String>> headers) {
		return header(headers, false);
	}

	/**
	 * 设置请求头
	 * @param headers 请求头
	 * @param isOverride 是否覆盖已有头信息
	 * @return this
	 * @since 4.0.8
	 */
	public RequestFactory header(Map<String, List<String>> headers, boolean isOverride) {
		if (MapUtil.isEmpty(headers)) {
			return this;
		}

		String name;
		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
			name = entry.getKey();
			for (String value : entry.getValue()) {
				this.header(name, StrUtil.nullToEmpty(value), isOverride);
			}
		}
		return this;
	}

	/**
	 * 设置一个header<br>
	 * 如果覆盖模式，则替换之前的值，否则加入到值列表中
	 * @param name Header名
	 * @param value Header值
	 * @param isOverride 是否覆盖已有值
	 * @return T 本身
	 */
	public RequestFactory header(String name, String value, boolean isOverride) {
		if (null != name && null != value) {
			final List<String> values = headers.get(name.trim());
			if (isOverride || CollectionUtil.isEmpty(values)) {
				final ArrayList<String> valueList = new ArrayList<>();
				valueList.add(value);
				headers.put(name.trim(), valueList);
			}
			else {
				values.add(value.trim());
			}
		}
		return this;
	}

	public interface RetStreamHandler<T> {

		T handle(InputStream inputStream);

	}

	public <T> T download(RetStreamHandler<T> retStreamHandler) {
		HttpRequest httpRequest = this.buildHttpClient();
		HttpResponse httpResponse = httpRequest.execute();
		InputStream inputStream = httpResponse.bodyStream();
		T ret = retStreamHandler.handle(inputStream);
		return ret;
	}

	public byte[] downloadBytes() {
		return this.download(IoUtil::readBytes);
	}

	public String execute() {
		HttpRequest httpRequest = this.buildHttpClient();
		String result = null;
		try {
			startTime = System.currentTimeMillis();
			HttpResponse httpResponse = httpRequest.execute();
			result = httpResponse.body();
			this.result = result;
		}
		catch (Exception exception) {
			this.throwable = exception;
			if (this.isThrowableExp) {
				throw exception;
			}
		}
		finally {
			endTime = System.currentTimeMillis();
			if (this.requestConfig.isPrintLog) {
				this.printRecord();
			}
		}
		return result;
	}

	private HttpRequest buildHttpClient() {
		HttpRequest httpRequest = HttpRequest.of(this.url);
		httpRequest.setConnectionTimeout(this.requestConfig.connectionTimeout);
		httpRequest.setReadTimeout(this.requestConfig.readTimeout);
		httpRequest.method(this.method);
		httpRequest.header(headers);
		if (StrUtil.isNotBlank(this.body)) {
			httpRequest.body(this.body);
		}
		if (MapUtil.isNotEmpty(this.paramMap)) {
			httpRequest.form(this.paramMap);
		}
		return httpRequest;
	}

	private void printRecord() {
		StringBuilder sb = new StringBuilder("\n");
		sb.append("请求接口:").append(this.url).append("\n");
		sb.append("请求方式:").append(this.method).append("\n");

		Optional.ofNullable(this.desc).ifPresent(item -> sb.append("请求描述:").append(item).append("\n"));
		sb.append("耗时:").append(this.endTime - this.startTime).append("\n");

		Optional.ofNullable(this.getReqParam()).ifPresent(item -> sb.append("请求参数:").append(item).append("\n"));
		if (!this.requestConfig.ignoreResponseLog) {
			Optional.ofNullable(this.getResult()).ifPresent(item -> sb.append("请求结果:").append(item).append("\n"));
		}
		Optional.ofNullable(this.throwable).ifPresent(item -> sb.append("请求异常:").append(item).append("\n"));
		log.info(sb.toString());

	}

}
