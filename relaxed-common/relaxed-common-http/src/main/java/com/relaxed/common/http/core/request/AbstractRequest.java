package com.relaxed.common.http.core.request;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;

import com.relaxed.common.http.core.response.IResponse;
import com.relaxed.common.http.domain.HttpResponseWrapper;
import com.relaxed.common.http.domain.IHttpResponse;
import com.relaxed.common.http.domain.RequestForm;

import com.relaxed.common.http.domain.UploadFile;
import com.relaxed.common.http.util.ClassUtil;
import org.springframework.web.bind.annotation.RequestMethod;

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
	 * 上传文件
	 */
	private List<UploadFile> files;

	protected Class<R> responseClass = this.currentResponseClass();

	/**
	 * 添加上传文件
	 * @param file
	 */
	public void addFile(UploadFile file) {
		if (this.files == null) {
			this.files = new ArrayList<>();
		}
		this.files.add(file);
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
		requestForm.setRequestMethod(getRequestMethod());
		requestForm.setFiles(requestForm.getFiles());
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
		requestForm.setForm(paramMap);
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
