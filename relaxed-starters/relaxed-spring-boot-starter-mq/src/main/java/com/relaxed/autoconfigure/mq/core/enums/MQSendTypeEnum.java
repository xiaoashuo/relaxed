/*
 * Copyright (c) 2021-2031, 河北计全科技有限公司 (https://www.jeequan.com & jeequan@126.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.relaxed.autoconfigure.mq.core.enums;

import lombok.RequiredArgsConstructor;

/**
 * 消息发送类型枚举。 定义消息队列支持的消息发送模式，包括点对点、广播和延迟消息。
 *
 * @author Yakir
 * @since 1.0
 */
public enum MQSendTypeEnum {

	/**
	 * 点对点模式 消息只会被一个消费者消费，适用于需要确保消息只被处理一次的场景 对应ActiveMQ的queue模式
	 */
	QUEUE,

	/**
	 * 广播模式 消息会被所有订阅者接收，适用于需要通知多个消费者的场景
	 * 对应ActiveMQ的topic模式、RabbitMQ的fanout类型交换机和RocketMQ的广播模式
	 */
	BROADCAST,

	/**
	 * 延迟消息模式 消息会在指定时间后发送，适用于需要延迟处理的消息场景
	 */
	DELAY

}
