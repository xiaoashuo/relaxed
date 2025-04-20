package com.relaxed.pool.monitor;

import lombok.extern.slf4j.Slf4j;

/**
 * 告警服务接口，用于发送线程池监控相关的告警信息。 实现类可以根据不同的告警渠道需求，提供相应的告警实现方式。
 *
 * @author Yakir
 * @since 1.0.0
 */
public interface AlertService {

	/**
	 * 发送告警信息到指定的渠道。
	 * @param finalMsg 告警消息内容
	 * @param channels 告警发送渠道数组，可以同时发送到多个渠道
	 */
	void sendAlert(String finalMsg, String[] channels);

}
