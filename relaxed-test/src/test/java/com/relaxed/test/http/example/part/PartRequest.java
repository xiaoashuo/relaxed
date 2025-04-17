package com.relaxed.test.http.example.part;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.http.core.request.AbstractRequest;
import com.relaxed.common.http.domain.RequestForm;
import com.relaxed.test.http.example.create.CreateResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic PartRequest
 * @Description
 * @date 2022/5/18 13:47
 * @Version 1.0
 */
@Data
public class PartRequest extends AbstractRequest<PartResponse> {

	@Data
	public static class Content {

		/**
		 * 合同id 业务系统合同编码，系统唯一，后续签署根据此编号完成签署；具体格式和规则由请求方自行定义，但长度不能大于128字符
		 */
		@NotEmpty(message = "businessId 不能为空")
		private String businessId;

		/**
		 * 1.模板 2.关键字
		 */
		@NotNull(message = "signLocation.type can not null.")
		private Integer type;

		/**
		 * 模板 -> 模板编码 关键字-> 关键字签章编号
		 */
		private String signCode;

		/**
		 * 回调地址
		 */
		private String callbackUrl;

		/**
		 * 产品code
		 */
		private String productCode;

		/**
		 * 信托计划code
		 */
		private String trustPlanCode;

		/**
		 * 文件类型
		 */
		private String fileType;

	}

	@Override
	public String getUrl(String baseUrl) {
		return baseUrl + "/stamp/apply";
	}

}
