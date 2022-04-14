package com.relaxed.common.core.util.http;

import com.relaxed.common.core.util.http.part.FilePart;
import com.relaxed.common.core.util.http.part.TextPart;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpTest {

	/**
	 * get 请求
	 */
	@Test
	public void get() {
		String url = "http://localhost:9093/get";
		String result = HttpUtil.get(url);
		log.info("接收到结果{}", result);
	}

	/**
	 * post请求 表单
	 */
	@Test
	public void post() {
		String url = "http://localhost:9093/post";

		String result = HttpUtil.post(url, (Map) null);
		log.info("接收到结果{}", result);
	}

	/**
	 * post 请求 json
	 */
	@Test
	public void postJson() {

		String urlStr = "http://localhost:9093/json";
		String data = "{\"username\":\"zs\"}";
		HttpRequest httpRequest = HttpUtil.createPost(urlStr);
		httpRequest.header("appId", "test");
		httpRequest.body(data);
		String result = httpRequest.execute().body();
		log.info("接收到结果{}", result);

	}

	/**
	 * 上传文件
	 */
	@Test
	public void upload() {
		String urlStr = "http://localhost:9093/upload";
		String filePath1 = "D:\\total_contract.docx";
		String filePath2 = "D:\\test5.docx";
		File file = new File(filePath1);
		File file2 = new File(filePath2);
		HttpRequest httpRequest = HttpUtil.createPost(urlStr);
		httpRequest.header("appId", "test");
		// 单文件
		// httpRequest.form("file",file);
		// 多文件
		httpRequest.form("files", file, file2);
		String result = httpRequest.execute().body();
		log.info("接收到结果{}", result);
	}

	/**
	 * 上传文件+表单
	 */
	@Test
	public void uploadAndForm() {
		String urlStr = "http://localhost:9093/upload/form";
		String filePath = "D:\\total_contract.docx";
		File file = new File(filePath);
		// 文件表单数据
		Map<String, Object> parts = new HashMap<>();
		// 上传文件
		parts.put("file", file);
		// 表单项
		parts.put("stampCode", "test");
		HttpRequest httpRequest = HttpUtil.createPost(urlStr);
		httpRequest.header("appId", "test");
		httpRequest.form(parts);
		HttpResponse execute = httpRequest.execute();
		String body = execute.body();
		log.info("接收到结果{}", body);
	}

	/**
	 * 上传文件+json 后端对应RequestPart注解
	 */
	@Test
	public void uploadAndJson() {
		String urlStr = "http://localhost:9093/upload/form/json";
		String data = "{\"username\":\"zs\"}";
		String filePath = "D:\\total_contract.docx";
		File file = new File(filePath);
		// 指定文本部分数据为json类型
		TextPart part = new TextPart(data, "application/json");
		Map<String, Object> parts = new HashMap<>();
		// 添加json 对应表单
		parts.put("template", part);
		// 添加文件
		parts.put("file", file);
		HttpRequest httpRequest = HttpUtil.createPost(urlStr);
		httpRequest.header("appId", "test");
		httpRequest.form(parts);
		HttpResponse execute = httpRequest.execute();
		String body = execute.body();
		log.info("接收到结果{}", body);

	}

}
