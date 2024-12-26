package com.relaxed.test.utils.request;

import lombok.Data;

import java.util.Map;

/**
 * @author Yakir
 * @Topic UserReq
 * @Description
 * @date 2024/12/26 16:21
 * @Version 1.0
 */
@Data
public class UserReq {

	private String username;

	private Map<String, Object> extParam;

}
