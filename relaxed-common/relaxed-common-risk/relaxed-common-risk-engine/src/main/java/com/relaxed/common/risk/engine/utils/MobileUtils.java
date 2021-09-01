package com.relaxed.common.risk.engine.utils;

/**
 * @author Yakir
 * @Topic MobileUtils
 * @Description
 * @date 2021/9/1 14:03
 * @Version 1.0
 */
public class MobileUtils {

	public static String getLocation(String mobile) {
		// TODO: 需要重新寻找手机号码段API 通道.
		String httpUrl = "http://apis.baidu.com/apistore/mobilenumber/mobilenumber";
		String httpArg = "phone=" + mobile;

		return null;
	}

}
