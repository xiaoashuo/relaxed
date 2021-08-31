package com.relaxed.common.risk.engine.core.plugins.handler;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.risk.engine.core.plugins.PluginService;
import com.relaxed.common.risk.engine.model.vo.PreItemVO;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yakir
 * @Topic FieldMergePlugin
 * @Description 字段合并插件
 * @date 2021/8/29 14:29
 * @Version 1.0
 */
public class FieldMergePlugin implements PluginService {

	@Override
	public Integer getOrder() {
		return 3;
	}

	@Override
	public String desc() {
		return "字段合并";
	}

	@Override
	public Class<?> getType() {
		return String.class;
	}

	@Override
	public Object handle(PreItemVO preItemVO, Map<String, Object> jsonInfo, String[] sourceFields) {
		if (ArrayUtil.isEmpty(sourceFields)) {
			return "";
		}
		return Arrays.asList(sourceFields).stream().map(f -> jsonInfo.get(f) == null ? "" : f)
				.collect(Collectors.joining("_"));
	}

}
