package com.relaxed.test.log.biz.function;

import cn.hutool.core.util.StrUtil;
import com.relaxed.common.log.biz.function.IParseFunc;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic CusIFunc
 * @Description
 * @date 2023/12/15 10:09
 * @Version 1.0
 */
@Component
public class CustomInterfaceFunc implements IParseFunc {

	@Override
	public String namespace() {
		return "ifunc";
	}

	@Override
	public String name() {
		return "test";
	}

	@Override
	public String around() {
		return "";
	}

	@Override
	public String apply(Object[] args) {
		return "i func test success,params " + StrUtil.join(",", args);
	}

}
