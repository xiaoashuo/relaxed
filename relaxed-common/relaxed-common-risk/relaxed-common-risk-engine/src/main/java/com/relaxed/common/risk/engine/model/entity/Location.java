package com.relaxed.common.risk.engine.model.entity;

import lombok.Data;

import java.util.StringJoiner;

/**
 * 区域
 *
 * @author Yakir
 */
@Data
public class Location {

	private String country = "中国";

	private String region = "";

	private String province = "";

	private String city = "";

	private String address = "";

}
