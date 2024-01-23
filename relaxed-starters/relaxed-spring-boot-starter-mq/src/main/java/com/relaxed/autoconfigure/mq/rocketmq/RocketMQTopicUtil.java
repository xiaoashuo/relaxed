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
 * @author Yakir
 * @Topic RocketMQTopicUtil
 * @Description 创建topic 参考 https://www.freesion.com/article/57131430117/ 以及官方源代码
 * @date 2021/12/28 16:27
 * @Version 1.0
 */
@Slf4j
public class RocketMQTopicUtil {

	public static boolean createTopic(String nameSrvAddr, String clusterName, String topic, int queueNum) {
		int defaultWaitTime = 5;
		return createTopic(nameSrvAddr, clusterName, topic, queueNum, defaultWaitTime);
	}

	private static boolean checkTopicExist(DefaultMQAdminExt mqAdminExt, String topic) {
		boolean createResult = false;
		try {
			TopicStatsTable topicInfo = mqAdminExt.examineTopicStats(topic);
			createResult = !topicInfo.getOffsetTable().isEmpty();
		}
		catch (Exception e) {
		}

		return createResult;
	}

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

	private static void salfSleep(Long time) {
		try {
			Thread.sleep(time);
		}
		catch (InterruptedException var3) {
			var3.printStackTrace();
		}
	}

	protected static int topicCreateTime = 30 * 1000;

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

	public static boolean initTopic(String topic, String nsAddr, String clusterName) {
		return initTopic(topic, nsAddr, clusterName, 8);
	}

}
