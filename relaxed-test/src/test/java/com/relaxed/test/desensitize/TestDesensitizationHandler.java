package com.relaxed.test.desensitize;

import com.relaxed.common.desensitize.handler.SimpleDesensitizationHandler;

/**
 * @author Hccake 2021/1/23
 * @version 1.0
 */
public class TestDesensitizationHandler implements SimpleDesensitizationHandler {

	@Override
	public String handle(String s) {
		return "TEST-" + s;
	}

}
