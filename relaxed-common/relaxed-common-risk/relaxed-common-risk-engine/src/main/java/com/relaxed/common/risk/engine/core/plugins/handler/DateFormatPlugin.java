package com.relaxed.common.risk.engine.core.plugins.handler;

import com.relaxed.common.risk.engine.core.plugins.PluginService;
import com.relaxed.common.risk.engine.model.vo.PreItemVO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author Yakir
 * @Topic DateFormatPlugin
 * @Description
 * @date 2021/8/29 14:29
 * @Version 1.0
 */
public class DateFormatPlugin implements PluginService {

	@Override
	public Integer getOrder() {
		return 7;
	}

	@Override
	public String desc() {
		return "日期格式化插件";
	}

	@Override
	public Object handle(PreItemVO preItemVO, Map<String, Object> jsonInfo, String[] sourceFields) {
		String formatStr = preItemVO.getArgs();
		String sourceField = sourceFields[0];
		String value = jsonInfo.get(sourceField).toString();
		return LocalDateTime.parse(value).format(DateTimeFormatter.ofPattern(formatStr));
	}

}
