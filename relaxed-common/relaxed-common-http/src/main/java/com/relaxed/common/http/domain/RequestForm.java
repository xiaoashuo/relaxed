package com.relaxed.common.http.domain;

import com.relaxed.common.http.core.resource.Resource;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic RequestForm
 * @Description
 * @date 2022/5/18 8:49
 * @Version 1.0
 */
@Getter
public class RequestForm {

	private RequestMethod requestMethod = RequestMethod.POST;

	/**
	 * 存在body 则默认为json请求 最高优先级
	 */
	private String body;

	/** 请求表单内容 */
	private Map<String, Object> form;

	/** 上传资源文件 */
	private List<Resource> resources = new ArrayList<>();

	/**
	 * 设置请求方法
	 * @param requestMethod
	 * @return
	 */
	public RequestForm method(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
		return this;
	}

	/**
	 * json参数
	 * @param body
	 * @return
	 */
	public RequestForm body(String body) {
		this.body = body;
		return this;
	}

	/**
	 * 表单参数
	 * @param form
	 * @return
	 */
	public RequestForm form(Map<String, Object> form) {
		this.form = form;
		return this;
	}

	/**
	 * 添加资源
	 * @author yakir
	 * @date 2022/6/16 16:10
	 * @param resource
	 * @return com.relaxed.common.http.domain.RequestForm
	 */
	public RequestForm addResource(Resource resource) {
		this.resources.add(resource);
		return this;
	}

	/**
	 * 添加资源
	 * @author yakir
	 * @date 2022/6/16 16:10
	 * @param resources
	 * @return com.relaxed.common.http.domain.RequestForm
	 */
	public RequestForm addResources(List<Resource> resources) {
		this.resources.addAll(resources);
		return this;
	}

}
