package com.relaxed.autoconfigure.mq.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.BrokerConfig;
import org.apache.rocketmq.common.admin.TopicStatsTable;
import org.apache.rocketmq.common.annotation.ImportantField;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * RocketMQ主题工具类
 * <p>
 * 提供RocketMQ主题创建和初始化的工具方法。 支持创建主题、检查主题是否存在、初始化主题等功能。 参考RocketMQ官方文档和源代码实现。
 * </p>
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
public class RocketMQTopicUtil {

	/**
	 * 主题创建超时时间（毫秒）
	 */
	protected static int topicCreateTime = 30 * 1000;

	/**
	 * 创建RocketMQ主题
	 * <p>
	 * 使用默认等待时间（5秒）创建主题。
	 * </p>
	 * @param nameSrvAddr 命名服务器地址
	 * @param clusterName 集群名称
	 * @param topic 主题名称
	 * @param queueNum 队列数量
	 * @return 是否创建成功
	 */
	public static boolean createTopic(String nameSrvAddr, String clusterName, String topic, int queueNum) {
		int defaultWaitTime = 5;
		return createTopic(nameSrvAddr, clusterName, topic, queueNum, defaultWaitTime);
	}

	/**
	 * 检查主题是否存在
	 * <p>
	 * 通过检查主题的偏移量表来判断主题是否存在。
	 * </p>
	 * @param mqAdminExt RocketMQ管理客户端
	 * @param topic 主题名称
	 * @return 主题是否存在
	 */
	private static boolean checkTopicExist(DefaultMQAdminExt mqAdminExt, String topic) {
		boolean createResult = false;
		try {
			TopicStatsTable topicInfo = mqAdminExt.examineTopicStats(topic);
			createResult = !topicInfo.getOffsetTable().isEmpty();
		}
		catch (Exception e) {
			log.error("Check topic exist failed", e);
		}
		return createResult;
	}

	/**
	 * 创建RocketMQ主题
	 * <p>
	 * 创建主题并等待指定时间，直到主题创建成功或超时。
	 * </p>
	 * @param nameSrvAddr 命名服务器地址
	 * @param clusterName 集群名称
	 * @param topic 主题名称
	 * @param queueNum 队列数量
	 * @param waitTimeSec 等待时间（秒）
	 * @return 是否创建成功
	 */
	public static boolean createTopic(String nameSrvAddr, String clusterName, String topic, int queueNum,
			int waitTimeSec) {
		boolean createResult = false;
		DefaultMQAdminExt mqAdminExt = new DefaultMQAdminExt();
		mqAdminExt.setInstanceName(UUID.randomUUID().toString());
		mqAdminExt.setNamesrvAddr(nameSrvAddr);
		try {
			mqAdminExt.start();
			mqAdminExt.createTopic(clusterName, topic, queueNum);
		}
		catch (Exception e) {
			log.error("Create topic failed", e);
		}

		long startTime = System.currentTimeMillis();
		while (!createResult) {
			createResult = checkTopicExist(mqAdminExt, topic);
			if (System.currentTimeMillis() - startTime < waitTimeSec * 1000) {
				salfSleep(100L);
			}
			else {
				log.error(String.format("timeout,but create topic[%s] failed!", topic));
				break;
			}
		}

		mqAdminExt.shutdown();
		return createResult;
	}

	/**
	 * 安全休眠
	 * <p>
	 * 线程休眠指定时间，捕获并记录中断异常。
	 * </p>
	 * @param time 休眠时间（毫秒）
	 */
	private static void salfSleep(Long time) {
		try {
			Thread.sleep(time);
		}
		catch (InterruptedException e) {
			log.error("Sleep interrupted", e);
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * 初始化RocketMQ主题
	 * <p>
	 * 尝试创建主题，直到成功或超时。 使用默认队列数量（8个）。
	 * </p>
	 * @param topic 主题名称
	 * @param nsAddr 命名服务器地址
	 * @param clusterName 集群名称
	 * @return 是否初始化成功
	 */
	public static boolean initTopic(String topic, String nsAddr, String clusterName) {
		return initTopic(topic, nsAddr, clusterName, 8);
	}

	/**
	 * 初始化RocketMQ主题
	 * <p>
	 * 尝试创建主题，直到成功或超时。 可以指定队列数量。
	 * </p>
	 * @param topic 主题名称
	 * @param nsAddr 命名服务器地址
	 * @param clusterName 集群名称
	 * @param queueNumbers 队列数量
	 * @return 是否初始化成功
	 */
	public static boolean initTopic(String topic, String nsAddr, String clusterName, int queueNumbers) {
		long startTime = System.currentTimeMillis();
		boolean createResult;

		while (true) {
			createResult = createTopic(nsAddr, clusterName, topic, queueNumbers);
			if (createResult) {
				break;
			}
			else if (System.currentTimeMillis() - startTime > topicCreateTime) {
				log.error("topic[{}] is created failed after:{} ms", topic, System.currentTimeMillis() - startTime);
				break;
			}
			else {
				salfSleep(500L);
				continue;
			}
		}

		return createResult;
	}

}
