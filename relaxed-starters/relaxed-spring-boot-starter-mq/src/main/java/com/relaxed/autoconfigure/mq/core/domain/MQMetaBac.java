// package com.relaxed.autoconfigure.mq.core.domain;
//
// import com.relaxed.autoconfigure.mq.core.enums.MQSendTypeEnum;
// import lombok.Builder;
// import lombok.Data;
// import lombok.experimental.Accessors;
//
/// **
// * @author Yakir
// * @Topic MQMeta
// * @Description
// * @date 2021/12/27 10:48
// * @Version 1.0
// */
// @Data
// @Builder
// @Accessors(chain = true)
// public class MQMetaBac {
//
// private Queue queue;
// private Exchange exchange;
// private Binder binder;
//
// @Builder
// @Data
// public class Queue{
// /**
// * 队列名称
// */
// private String queueName;
// }
//
// @Data
// @Builder
// public class Exchange{
// /**
// * 交换器名称
// */
// private String exchangeName;
// /**
// * 交换机类型
// */
// private MQSendTypeEnum exchangeType;
// }
//
// @Data
// @Builder
// public class Binder{
// /**
// * 队列与交换器 是否绑定
// */
// private boolean binding;
//
// /**
// * 路由key
// * @author yakir
// * @date 2021/12/27 11:14
// */
// private String routingKey="";
// }
//
//
//
//
//
//
//
//
//
// }
