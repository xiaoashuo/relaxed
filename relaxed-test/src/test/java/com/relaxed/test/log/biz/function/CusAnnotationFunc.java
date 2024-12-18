package com.relaxed.test.log.biz.function;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.biz.annotation.LogFunc;
import com.relaxed.common.log.biz.constant.LogRecordConstants;
import com.relaxed.common.log.biz.context.LogRecordContext;
import com.relaxed.common.log.biz.model.AttributeModel;
import com.relaxed.common.log.biz.model.DiffMeta;
import com.relaxed.common.log.biz.service.IDataHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Yakir
 * @Topic CusAnFunc
 * @Description
 * @date 2023/12/15 10:10
 * @Version 1.0
 */
@RequiredArgsConstructor
@Component
@LogFunc
public class CusAnnotationFunc {

	private final IDataHandler dataHandler;

	@LogFunc(namespace = "_", funcName = "DIFF")
	public String diffFunc(Object source, Object target) {
		// TODO 此处提取出diff差异 但未加入diff 上下文 比较 需要有全局缓存 存储diff结果 方便下次不在重复解析
		// 改进diff方法 不同比对 返回统一结构
		List<AttributeModel> test = dataHandler.diffObject(new DiffMeta("test", source, target));
		return JSONUtil.toJsonStr(test);

	}

	@LogFunc
	public static String testAnnotation(Integer arg) {
		return "test annotation method success" + arg;
	}

	@LogFunc
	public String testAnnotationNoStatic() {
		return "test annotation non static method success";
	}

	/**
	 * 标识为前置函数
	 * @param arg
	 * @return
	 */
	@LogFunc(around = LogRecordConstants.BEFORE_FUNC)
	public static String testBeforeFunc(Integer arg) {
		return "test annotation before method success" + arg;
	}

}
