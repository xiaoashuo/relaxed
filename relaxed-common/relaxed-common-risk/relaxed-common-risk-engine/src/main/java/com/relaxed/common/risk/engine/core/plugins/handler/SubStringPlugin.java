package com.relaxed.common.risk.engine.core.plugins.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.risk.engine.core.plugins.PluginEnum;
import com.relaxed.common.risk.engine.core.plugins.PluginMeta;
import com.relaxed.common.risk.engine.core.plugins.PluginService;
import com.relaxed.common.risk.model.vo.PreItemVO;

import java.util.Date;
import java.util.Map;

/**
 * @author Yakir
 * @Topic SubStringPlugin
 * @Description
 * @date 2021/8/29 14:29
 * @Version 1.0
 */
public class SubStringPlugin implements PluginService {

	@Override
	public PluginMeta pluginMeta() {
		return PluginEnum.SUB_STRING_PLUGIN;
	}

	@Override
	public String pluginName() {
		return "SUBSTRING";
	}

	@Override
	public Object handle(PreItemVO preItemVO, Map<String, Object> jsonInfo, String[] sourceFields) {
		String[] args = preItemVO.getArgs().split(",");
		String field = jsonInfo.get(sourceFields[0]).toString();
		int start = Integer.parseInt(args[0]);
		int end = Integer.parseInt(args[1]);
		return StrUtil.sub(field, start, end);
	}

}
