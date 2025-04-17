package com.relaxed.test.mq.rocketmq;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Sets;
import com.relaxed.autoconfigure.mq.core.IMQSender;
import com.relaxed.autoconfigure.mq.core.annotation.EnableMQ;
import com.relaxed.autoconfigure.mq.core.enums.MQTypeEnum;
import com.relaxed.autoconfigure.mq.rocketmq.RocketMQTopicUtil;
import com.relaxed.test.mq.rocketmq.domain.simple.SimpleMQ;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.MQAdmin;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.MQAdminImpl;
import org.apache.rocketmq.common.BrokerConfig;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.TopicConfig;
import org.apache.rocketmq.common.admin.TopicStatsTable;
import org.apache.rocketmq.common.protocol.body.ClusterInfo;
import org.apache.rocketmq.remoting.exception.RemotingConnectException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.remoting.exception.RemotingSendRequestException;
import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;
import org.apache.rocketmq.srvutil.ServerUtil;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExtImpl;
import org.apache.rocketmq.tools.admin.MQAdminExt;
import org.apache.rocketmq.tools.command.CommandUtil;
import org.apache.rocketmq.tools.command.SubCommandException;
import org.apache.rocketmq.tools.command.topic.UpdateTopicSubCommand;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.utils.test.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

/**
 * @author Yakir
 * @Topic RabbitMQSenderTest
 * @Description 1.创建MQ消息
 * @date 2021/12/23 16:12
 * @Version 1.0
 */
@Slf4j
@EnableMQ(mqType = MQTypeEnum.ROCKET_MQ, basePackages = "com.relaxed.autoconfigure.mq.rocketmq.domain.simple")
@SpringBootApplication
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("rocketmq")
class RocketMQSenderTest {

	@Autowired
	private IMQSender mqSender;

	// @Autowired
	// private ApplicationContext applicationContext;

	@Test
	public void testRocketMq() {
		ArrayList<Long> longs = ListUtil.toList(1L);
		SimpleMQ build = SimpleMQ.build(longs);
		mqSender.send(build);

	}

	private String brokerName = BrokerConfig.localHostName();

	private String brokerClusterName = "DefaultCluster";

	/**
	 * 创建topic 参考 https://www.freesion.com/article/57131430117/
	 * @author yakir
	 * @date 2021/12/28 15:47
	 */
	@SneakyThrows
	@Test
	public void createTopicTest() {
		BrokerConfig brokerConfig = new BrokerConfig();

		String namesrvAddr = "127.0.0.1:9876";
		String topic = "rocketmq45";
		brokerConfig.setNamesrvAddr(namesrvAddr);
		brokerConfig.setBrokerIP1("127.0.0.1");

		boolean result = RocketMQTopicUtil.initTopic(topic, namesrvAddr, brokerName);
		System.out.println(result);

	}

	@Test
	public void testRabbbitMQ()
			throws InterruptedException, SubCommandException, RemotingException, MQBrokerException, MQClientException {
		ArrayList<Long> longs = ListUtil.toList(1L);
		// 1.测试直接交换机
		// DirectMQ build = DirectMQ.build(longs);
		// 2.测试广播交换机
		// FanoutMQ build = FanoutMQ.build(longs);
		// 3.测试延迟交换机
		// DelayMQ build = DelayMQ.build(longs);
		// // mqSender.send(build);
		// mqSender.send(build, 5);
		String[] subargs = new String[] { "-b 127.0.0.1:10911", "-t unit-test-from-java-1111",
				// "-r 8",
				// "-w 8",
				// "-p 6",
				// "-o false",
				// "-u false",
				// "-s false"
		};

		UpdateTopicSubCommand cmd = new UpdateTopicSubCommand();
		Options options = ServerUtil.buildCommandlineOptions(new Options());
		Options updateTopicOptions = cmd.buildCommandlineOptions(options);
		CommandLine commandLine = ServerUtil.parseCmdLine("mqAdmin", subargs, updateTopicOptions, new PosixParser());
		cmd.execute(commandLine, updateTopicOptions, null);

		// Thread.sleep(500000);
	}

}
