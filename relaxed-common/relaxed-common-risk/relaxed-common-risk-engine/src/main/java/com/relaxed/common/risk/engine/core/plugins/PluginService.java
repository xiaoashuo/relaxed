package com.relaxed.common.risk.engine.core.plugins;

import com.relaxed.common.risk.engine.model.vo.PreItemVO;
import org.springframework.core.annotation.Order;

import java.util.Map;

/**
 * @author Yakir
 * @Topic PluginService
 * @Description
 * @date 2021/8/29 14:24
 * @Version 1.0
 */
public interface PluginService {

	/**
	 * 插件名称
	 * @author yakir
	 * @date 2021/8/29 14:25
	 * @return java.lang.String
	 */
	default String pluginName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * 插件顺序
	 * @author yakir
	 * @date 2021/8/29 14:28
	 * @return java.lang.Integer
	 */
	Integer getOrder();

	/**
	 * 插件描述
	 * @author yakir
	 * @date 2021/8/29 14:26
	 * @return java.lang.String
	 */
	String desc();

	/**
	 * 结果类型
	 * @author yakir
	 * @date 2021/8/31 17:50
	 * @return java.lang.Class<?>
	 */
	Class<?> getType();

	/**
	 * 处理字段方法
	 * @author yakir
	 * @date 2021/8/29 14:25
	 * @param preItemVO
	 * @param jsonInfo
	 * @param sourceFields
	 * @return java.lang.Object
	 */
	Object handle(PreItemVO preItemVO, Map<String, Object> jsonInfo, String[] sourceFields);

}
