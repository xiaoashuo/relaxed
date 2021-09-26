package com.relaxed.common.risk.engine.core.subscribe.handles;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.risk.biz.distributor.event.subscribe.SubscribeEnum;
import com.relaxed.common.risk.biz.distributor.event.subscribe.SubscribeHandle;
import com.relaxed.common.risk.biz.distributor.event.subscribe.SubscribeType;
import com.relaxed.common.risk.engine.rules.script.RuleScriptHandler;
import com.relaxed.common.risk.model.vo.RuleVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic SubscribeModelHandle
 * @Description 订阅model
 * @date 2021/8/29 9:34
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SubscribeRuleScriptHandle implements SubscribeHandle {

	private final RuleScriptHandler ruleScriptHandler;

	@Override
	public SubscribeType type() {
		return SubscribeEnum.PUB_SUB_RULE_SCRIPT_CHANNEL;
	}

	@Override
	public void onMessage(String channel, String message) {
		log.info("rule script update message:{}", message);
		RuleVO vo = JSONUtil.toBean(message, RuleVO.class);
		// 删除规则脚本 防止内存溢出
		ruleScriptHandler.removeScript(ruleScriptHandler.buildContext(vo.getScripts(), null, null));
	}

}
