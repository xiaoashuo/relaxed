package com.relaxed.common.http.test;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.http.HttpSender;

import com.relaxed.common.http.core.provider.RequestHeaderProvider;
import com.relaxed.common.http.core.resource.StringResource;
import com.relaxed.common.http.domain.RequestForm;
import com.relaxed.common.http.test.custom.CustomSender;
import com.relaxed.common.http.test.example.create.CreateRequest;
import com.relaxed.common.http.test.example.create.CreateResponse;
import com.relaxed.common.http.test.example.file.FileRequest;
import com.relaxed.common.http.test.example.file.FileResponse;
import com.relaxed.common.http.test.example.part.PartRequest;
import com.relaxed.common.http.test.example.part.PartResponse;
import com.relaxed.common.http.test.example.query.StampQueryRequest;
import com.relaxed.common.http.test.example.query.StampQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic SenderTest
 * @Description
 * @date 2022/2/9 15:51
 * @Version 1.0
 */
@Slf4j
class SenderTest {

	private final String baseUrl = "http://test.lovecyy.cn";

	private RequestHeaderProvider requestHeaderProvider = (requestUrl, requestForm) -> getRequestHeader(requestUrl,
			requestForm);

	@Test
	public void testCustomSenderUpload() {

		CustomSender httpSender = new CustomSender(baseUrl, requestHeaderProvider);
		StampQueryRequest request = new StampQueryRequest();
		request.setChannelNo("test");
		request.setRequestMethod(RequestMethod.GET);
		request.setFileNo("48");
		log.info("请求参数:{}", request);
		StampQueryResponse response = httpSender.send(request);
		log.info("请求响应:{}", response);
	}

	@Test
	public void testUpload() {

		HttpSender httpSender = new HttpSender(baseUrl, requestHeaderProvider);
		StampQueryRequest request = new StampQueryRequest();
		request.setChannelNo("test");
		request.setRequestMethod(RequestMethod.GET);
		request.setFileNo("48");
		log.info("请求参数:{}", request);
		StampQueryResponse response = httpSender.send(request);
		log.info("请求响应:{}", response);
	}

	@Test
	public void testDownload() {

		HttpSender httpSender = new HttpSender(baseUrl, requestHeaderProvider);
		FileRequest request = new FileRequest();
		request.setChannelNo("test");
		request.setRequestMethod(RequestMethod.GET);
		request.setFileNo("48");
		log.info("请求参数:{}", request);
		FileResponse response = httpSender.send(request);
		log.info("请求响应:{}", response);
		String fileContent = response.getFileContent();
		File file = new File("test.pdf");
		FileUtil.writeBytes(Base64.decode(fileContent), file);
	}

	private Map<String, String> getRequestHeader(String requestUrl, RequestForm requestForm) {
		Map<String, String> map = new HashMap<>();
		map.put("appId", "trust");
		map.put("channel", "trust");
		map.put("operatorId", "1");
		map.put("operatorName", "seal");
		return map;
	}

	@Test
	public void testMultipart() {
		HttpSender httpSender = new HttpSender(baseUrl, requestHeaderProvider);

		PartRequest partRequest = new PartRequest();
		PartRequest.Content stampApplyDTO = new PartRequest.Content();
		stampApplyDTO.setBusinessId(RandomUtil.randomNumbers(16));
		stampApplyDTO.setType(1);
		stampApplyDTO.setSignCode("test0020");
		stampApplyDTO.setCallbackUrl("");
		stampApplyDTO.setProductCode("4000000");
		stampApplyDTO.setTrustPlanCode("40000");
		stampApplyDTO.setFileType("9");
		StringResource stringResource = new StringResource("apply", "application/json", "utf-8",
				JSONUtil.toJsonStr(stampApplyDTO));
		partRequest.addResource(stringResource);
		partRequest.addFile("file", new File("D:\\400701_6985565.pdf"));
		log.info("请求参数:{}", partRequest);
		PartResponse response = httpSender.send(partRequest);
		log.info("请求响应:{}", response);
	}

	@Test
	public void testCreate() {

		HttpSender httpSender = new HttpSender(baseUrl, requestHeaderProvider);
		CreateRequest request = new CreateRequest();
		request.setChannelNo("test");
		// 方法级请求头 优先级最高 会覆盖全局请求头里面相同key
		request.addHeader("username", "1");
		request.setRequestMethod(RequestMethod.POST);
		request.setTemplateCode("1526743370016247808");
		Map<String, String> data1 = new HashMap<>();
		data1.put("partnerBizNo", IdUtil.simpleUUID());
		data1.put("cardnumber", "12311");
		data1.put("bank", "12121");
		data1.put("name2", "1212");
		data1.put("email", "121121@qq.com");
		data1.put("phone", "12121");
		data1.put("address", "121");
		data1.put("code", "12");
		data1.put("number", "1212");
		data1.put("type", "121");
		data1.put("legal", "121");
		data1.put("contract", "1212");
		data1.put("share", "121");
		data1.put("bond", "121");
		data1.put("category", "121");
		data1.put("name", "121");

		List<Map<String, String>> list = new ArrayList<>();
		list.add(data1);
		request.setData(list);
		log.info("请求参数:{}", request);
		CreateResponse response = httpSender.send(request);
		log.info("请求响应:{}", response);
	}

}
