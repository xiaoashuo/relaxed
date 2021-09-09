package com.relaxed.common.risk.engine.core.plugins.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import com.relaxed.common.risk.engine.core.plugins.PluginEnum;
import com.relaxed.common.risk.engine.core.plugins.PluginMeta;
import com.relaxed.common.risk.engine.core.plugins.PluginService;
import com.relaxed.common.risk.engine.model.vo.PreItemVO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

/**
 * @author Yakir
 * @Topic SensitiveTimePlugin
 * @Description
 * @date 2021/8/29 14:29
 * @Version 1.0
 */
public class SensitiveTimePlugin implements PluginService {

	@Override
	public PluginMeta pluginMeta() {
		return PluginEnum.SENSITIVE_TIME_PLUGIN;
	}

	@Override
	public String pluginName() {
		return "SENSITIVE_TIME";
	}

	@Override
	public Object handle(PreItemVO preItemVO, Map<String, Object> jsonInfo, String[] sourceFields) {
		long millis = Long.parseLong(jsonInfo.get(sourceFields[0]).toString());

		return DateUtil.hour(new Date(millis), true) + "";
	}

}
