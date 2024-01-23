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
 * 定义MQ消息格式
 *
 * @author yakir
 * @date 2021/12/23 16:00
 */
public interface AbstractMQ {

	/**
	 * MQ 源信息
	 * @return MQMeta
	 */
	MQMeta getMQMeta();

	/**
	 * 构造MQ消息体 String类型
	 * @return
	 */
	String toMessage();

}
