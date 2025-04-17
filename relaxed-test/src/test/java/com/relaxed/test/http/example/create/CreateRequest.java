package com.relaxed.test.http.example.create;

import cn.hutool.json.JSONUtil;

import com.relaxed.common.http.core.request.AbstractRequest;
import com.relaxed.common.http.domain.RequestForm;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic CreateRequest
 * @Description
 * @date 2022/5/18 13:47
 * @Version 1.0
 */
@Data
public class CreateRequest extends AbstractRequest<CreateResponse> {

	private String templateCode;

	private List<Map<String, String>> data;

	@Override
	protected RequestForm fillRequestParam(RequestForm requestForm) {
		String json = JSONUtil.toJsonStr(this);
		requestForm.body(json);
		return requestForm;
	}

	@Override
	public String getUrl(String baseUrl) {
		return baseUrl + "/stamp-app/stamp/create/data";
	}

}
