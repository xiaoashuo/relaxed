package com.relaxed.common.http.domain;

import com.relaxed.common.http.core.resource.Resource;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic RequestForm
 * @Description
 * @date 2022/5/18 8:49
 * @Version 1.0
 */
@Accessors(chain = true)
@Data
public class RequestForm {

	private RequestMethod requestMethod = RequestMethod.POST;

	/**
	 * 存在body 则默认为json请求 最高优先级
	 */
	private String body;

	/** 请求表单内容 */
	private Map<String, Object> form;

	/** 上传资源文件 */
	private List<Resource> resources;

}
