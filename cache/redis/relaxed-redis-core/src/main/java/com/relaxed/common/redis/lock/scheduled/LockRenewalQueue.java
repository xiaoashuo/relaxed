package com.relaxed.common.redis.lock.scheduled;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Yakir
 * @Topic LockRenewalQueue
 * @Description
 * @date 2022/10/12 19:02
 * @Version 1.0
 */
public class LockRenewalQueue {

	/**
	 * 任务队列
	 */
	public static ConcurrentLinkedQueue<LockDefinitionHolder> holderList = new ConcurrentLinkedQueue();

}
