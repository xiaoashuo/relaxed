package com.relaxed.test.http.example.part;

import com.relaxed.common.http.core.response.BaseResponse;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Yakir
 * @Topic PartResponse
 * @Description
 * @date 2022/5/18 13:47
 * @Version 1.0
 */
@ToString(callSuper = true)
@Data
public class PartResponse extends BaseResponse {

	private Content data;

	@Data
	public class Content {

		/**
		 * 业务id
		 */
		private String bizId;

		/**
		 * 机构方传递 业务id
		 */
		private String businessId;

		/**
		 * 申请日期
		 */
		private LocalDateTime applyDateTime;

	}

}
