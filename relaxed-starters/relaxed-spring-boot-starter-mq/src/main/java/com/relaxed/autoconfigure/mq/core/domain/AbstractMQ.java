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
package com.relaxed.autoconfigure.mq.core.domain;

import com.relaxed.autoconfigure.mq.core.enums.MQSendTypeEnum;

/**
 * 消息队列消息抽象接口。 定义消息队列消息的基本格式和操作，包括获取消息元数据和转换为消息字符串。
 *
 * @author Yakir
 * @since 1.0
 */
public interface AbstractMQ {

	/**
	 * 获取消息元数据。 包含消息的发送类型、主题、标签等配置信息。
	 * @return 消息元数据对象
	 */
	MQMeta getMQMeta();

	/**
	 * 将消息对象转换为字符串格式。 用于消息序列化和传输。
	 * @return 消息的字符串表示
	 */
	String toMessage();

}
