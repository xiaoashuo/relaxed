package com.relaxed.test.http.example.query;

import com.relaxed.common.http.core.response.BaseResponse;
import lombok.Data;

/**
 * @author Yakir
 * @Topic StampQueryResponse
 * @Description
 * @date 2022/5/18 11:39
 * @Version 1.0
 */
@Data
public class StampQueryResponse extends BaseResponse {

	private Content data;

	@Data
	public class Content {

		private Integer batchId;

		private String fileNo;

		private String stampBizNo;

	}

}
