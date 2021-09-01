package com.relaxed.common.risk.engine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Yakir
 * @Topic EngineProperties
 * @Description
 * @date 2021/8/31 9:35
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "relaxed.engine")
public class EngineProperties {

	/**
	 * 是否允许重读插入 覆盖原模型构建
	 */
	private static boolean duplicate = false;

	/**
	 * 机器学习 开关
	 */
	private static boolean machineLearning = true;

	/**
	 * 机器学习工作目录
	 */
	private static String machineWorkDir;

	/**
	 * ip local path
	 */
	private static String ipRegionDb;

	/**
	 * mobile path
	 */
	private static String mobilePath;

	public static boolean getMachineLearning() {
		return machineLearning;
	}

	public static boolean getDuplicate() {
		return duplicate;
	}

	public void setIsDuplicate(boolean isDuplicate) {
		EngineProperties.duplicate = isDuplicate;
	}

	public void setMachineLearning(Boolean machineLearning) {
		EngineProperties.machineLearning = machineLearning;
	}

	public static String getIpRegionDb() {
		return ipRegionDb;
	}

	public void setIpRegionDb(String ipRegionDb) {
		EngineProperties.ipRegionDb = ipRegionDb;
	}

	public static String getMobilePath() {
		return mobilePath;
	}

	public void setMobilePath(String mobilePath) {
		EngineProperties.mobilePath = mobilePath;
	}

	public static String getMachineWorkDir() {
		return machineWorkDir;
	}

}
