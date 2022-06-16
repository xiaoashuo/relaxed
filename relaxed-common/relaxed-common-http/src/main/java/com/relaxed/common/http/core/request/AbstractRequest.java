package com.relaxed.common.http.core.request;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;

import com.relaxed.common.http.core.resource.ByteResource;
import com.relaxed.common.http.core.resource.FileResource;
import com.relaxed.common.http.core.resource.InputStreamResource;
import com.relaxed.common.http.core.resource.Resource;
import com.relaxed.common.http.core.response.IResponse;
import com.relaxed.common.http.domain.HttpResponseWrapper;
import com.relaxed.common.http.domain.IHttpResponse;
import com.relaxed.common.http.domain.RequestForm;

import com.relaxed.common.http.exception.RequestException;
import com.relaxed.common.http.util.ClassUtil;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic AbstractRequest
 * @Description
 * @date 2022/4/24 15:42
 * @Version 1.0
 */
public abstract class AbstractRequest<R extends IResponse> implements IRequest<R> {

	/**
	 * 渠道
	 */
	private transient String channelNo;

	private RequestMethod requestMethod = RequestMethod.POST;

	/**
	 * 资源文件列表
	 */
	private List<Resource> resources = new ArrayList<>();

	protected Class<R> responseClass = this.currentResponseClass();

	/**
	 * 添加资源
	 * @param resource
	 */
	public void addResource(Resource resource) {
		this.resources.add(resource);
	}

	/**
	 * 添加资源列表
	 * @param resources
	 */
	public void addResources(List<Resource> resources) {
		this.resources.addAll(resources);
	}

	/**
	 * 添加文件
	 * @author yakir
	 * @date 2022/5/23 15:22
	 * @param name
	 * @param value
	 */
	public void addFile(String name, Object value) {
		Resource resource;
		if (value instanceof File) {
			resource = new FileResource(name, (File) value);
		}
		else if (value instanceof byte[]) {
			resource = new ByteResource(name, (byte[]) value);
		}
		else if (value instanceof InputStream) {
			resource = new InputStreamResource(name, (InputStream) value);
		}
		else {
			throw new RequestException("暂不支持此种方式文件上传");
		}
		addResource(resource);
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	@Override
	public String getChannel() {
		return channelNo;
	}

	public void setRequestMethod(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
	}

	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	@Override
	public RequestForm generateRequestParam() {
		RequestForm requestForm = new RequestForm();
		requestForm.method(getRequestMethod());
		requestForm.addResources(this.getResources());
		return fillRequestParam(requestForm);
	}

	/**
	 * 填充请求参数
	 * @author yakir
	 * @date 2022/5/18 9:55
	 * @param requestForm
	 * @return com.relaxed.third.template.domain.RequestForm
	 */
	protected RequestForm fillRequestParam(RequestForm requestForm) {
		Map<String, Object> paramMap = BeanUtil.beanToMap(this, false, true);
		requestForm.form(paramMap);
		return requestForm;
	}

	/**
	 * 转换响应参数
	 * @author yakir
	 * @date 2022/5/18 13:51
	 * @param response
	 * @return R
	 */
	@Override
	public R convertToResponse(IHttpResponse response) {
		return JSONUtil.toBean(response.body(), responseClass);
	}

	protected Class<R> currentResponseClass() {
		return (Class<R>) ClassUtil.currentResponseClass(this.getClass(), 0);
	}

}
