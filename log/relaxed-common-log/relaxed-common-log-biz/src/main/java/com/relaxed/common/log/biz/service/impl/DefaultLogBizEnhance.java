package com.relaxed.common.log.biz.service.impl;

import cn.hutool.core.net.Ipv4Util;
import com.relaxed.common.log.biz.model.LogBizInfo;
import com.relaxed.common.log.biz.service.ILogBizEnhance;
import com.relaxed.common.log.biz.spel.LogSpelEvaluationContext;

import java.util.Map;

/**
 * 默认日志业务增强实现类 该实现类提供了日志业务信息的默认增强处理 主要功能包括： 1. 对日志业务信息进行额外的处理或补充 2. 支持自定义扩展，可以根据业务需求重写
 * enhance 方法 3. 提供默认的空实现，方便业务方按需扩展
 *
 * @author Yakir
 */
public class DefaultLogBizEnhance implements ILogBizEnhance {

	/**
	 * 增强日志业务信息 默认实现为空，业务方可以根据需要重写此方法 例如： 1. 补充额外的业务信息 2. 对现有信息进行转换或格式化 3. 添加自定义的统计或分析数据
	 * @param logBizInfo 日志业务信息
	 * @param spelContext SpEL 上下文
	 */
	@Override
	public void enhance(LogBizInfo logBizInfo, LogSpelEvaluationContext spelContext) {
		// 默认实现为空，业务方可以根据需要重写此方法
	}

}
