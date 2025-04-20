package com.relaxed.common.redis.lock.scheduled;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 锁续期队列。 用于存储需要续期的分布式锁信息，支持并发操作。
 *
 * @author Yakir
 * @since 1.0
 */
public class LockRenewalQueue {

	/**
	 * 锁续期任务队列 存储所有需要续期的锁定义持有者
	 */
	public static ConcurrentLinkedQueue<LockDefinitionHolder> holderList = new ConcurrentLinkedQueue();

}
