package com.relaxed.test.http.example.create;

import com.relaxed.common.http.core.response.BaseResponse;
import lombok.Data;

import java.util.List;

/**
 * @author Yakir
 * @Topic CreateResponse
 * @Description
 * @date 2022/5/18 13:47
 * @Version 1.0
 */
@Data
public class CreateResponse extends BaseResponse {

	private Content data;

	@Data
	public class Content {

		private Integer batchId;

		private List<String> businessIds;

	}

}
