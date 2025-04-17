package com.relaxed.test.http.ocr;

import com.relaxed.common.http.core.request.AbstractRequest;
import com.relaxed.common.http.domain.RequestForm;

/**
 * @author Yakir
 * @Topic BaseRequest
 * @Description
 * @date 2022/6/20 17:46
 * @Version 1.0
 */
public abstract class BaseRequest extends AbstractRequest<DetectAuthResponse> {

	/**
	 * 操作动作
	 */
	private String action;

	@Override
	public RequestForm generateRequestParam() {
		RequestForm requestForm = new RequestForm();
		requestForm.method(getRequestMethod());
		requestForm.addResources(this.getResources());
		return fillRequestParam(requestForm);
	}

}
