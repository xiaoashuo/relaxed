package com.relaxed.common.http.test.example.query;

import com.relaxed.common.http.core.request.AbstractRequest;
import lombok.Data;

/**
 * @author Yakir
 * @Topic StampQuery
 * @Description
 * @date 2022/5/18 11:39
 * @Version 1.0
 */
@Data
public class StampQueryRequest extends AbstractRequest<StampQueryResponse> {

	private String fileNo;

	@Override
	public String getUrl(String baseUrl) {
		return baseUrl + "/stamp-app/stamp/query";
	}

}
