package com.relaxed.common.log.test.biz.function;

import com.relaxed.common.log.biz.annotation.LogFunc;
import com.relaxed.common.log.biz.constant.LogRecordConstants;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic CusAnFunc
 * @Description
 * @date 2023/12/15 10:10
 * @Version 1.0
 */
@Component
@LogFunc
public class CusAnnotationFunc {

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
