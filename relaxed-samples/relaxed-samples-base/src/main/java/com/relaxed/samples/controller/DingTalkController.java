package com.relaxed.samples.controller;

import com.relaxed.common.dingtalk.message.DingTalkTextMessage;
import com.relaxed.common.dingtalk.request.DingTalkResponse;
import com.relaxed.common.dingtalk.request.DingTalkSender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yakir
 * @Topic DingTalkController
 * @Description
 * @date 2021/6/21 11:29
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestController
public class DingTalkController {

	private final DingTalkSender dingTalkSender;

	/**
	 * 测试ding talk 简单文本发送
	 * @param msg
	 * @return
	 */
	@GetMapping("ding/send")
	public DingTalkResponse dingSend(String msg) {
		DingTalkTextMessage dingTalkTextMessage = new DingTalkTextMessage();
		dingTalkTextMessage.setContent(msg);
		DingTalkResponse dingTalkResponse = dingTalkSender.sendMessage(dingTalkTextMessage);
		return dingTalkResponse;
	}

}
