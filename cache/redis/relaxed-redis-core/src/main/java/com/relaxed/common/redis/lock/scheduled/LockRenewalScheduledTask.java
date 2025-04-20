package com.relaxed.common.redis.lock.scheduled;

import com.relaxed.common.redis.RedisHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PreDestroy;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁续期定时任务。 负责定期检查并续期分布式锁，防止锁过期导致业务中断。
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
public class LockRenewalScheduledTask {

	/**
	 * 线程池名称
	 */
	private final String threadPoolName;

	/**
	 * 核心线程数
	 */
	private final Integer corePoolSize;

	/**
	 * 执行周期（秒）
	 */
	private final Integer period;

	/**
	 * 定时任务调度器
	 */
	private final ScheduledExecutorService scheduler;

	/**
	 * 默认构造函数 使用默认配置：线程池名称为"redisLock-schedule-pool"，核心线程数为1，执行周期为2秒
	 */
	public LockRenewalScheduledTask() {
		this("redisLock-schedule-pool", 1, 2);
	}

	/**
	 * 构造函数
	 * @param threadPoolName 线程池名称
	 * @param corePoolSize 核心线程数
	 * @param period 执行周期（秒）
	 */
	public LockRenewalScheduledTask(String threadPoolName, Integer corePoolSize, Integer period) {
		this.threadPoolName = threadPoolName;
		this.corePoolSize = corePoolSize;
		this.period = period;
		/**
		 * 线程池，维护keyAliveTime
		 */
		scheduler = new ScheduledThreadPoolExecutor(this.corePoolSize,
				new BasicThreadFactory.Builder().namingPattern(this.threadPoolName).daemon(true).build());
		initWatchDog();
		log.info("start daemon thread pool {}  success", threadPoolName);
	}

	/**
	 * 销毁方法 在应用关闭时关闭线程池
	 */
	@PreDestroy
	public void destroy() {
		try {
			log.info("close daemon thread pool  {}", threadPoolName);
			this.scheduler.shutdown();
		}
		catch (Exception e) {
			log.error("close daemon thread pool exception", e);
		}
	}

	/**
	 * 初始化看门狗任务 定期检查并续期分布式锁
	 */
	private void initWatchDog() {
		// 两秒执行一次「续时」操作
		this.scheduler.scheduleAtFixedRate(() -> {
			ConcurrentLinkedQueue<LockDefinitionHolder> holderList = LockRenewalQueue.holderList;
			// 这里记得加 try-catch，否者报错后定时任务将不会再执行=-=
			Iterator<LockDefinitionHolder> iterator = holderList.iterator();
			while (iterator.hasNext()) {
				LockDefinitionHolder holder = iterator.next();
				// 判空
				if (holder == null) {
					iterator.remove();
					continue;
				}
				// 判断 key 是否还有效，无效的话进行移除
				if (RedisHelper.get(holder.getLockKey()) == null) {
					iterator.remove();
					continue;
				}
				// 超时重试次数，超过时给线程设定中断 3次续期还未执行完毕 则需要检查代码逻辑
				if (holder.getCurrentCount() > holder.getTryCount()) {
					log.info("lockKey : [" + holder.getLockKey() + "], try count : " + holder.getCurrentCount()
							+ " exceed max times ");
					iterator.remove();
					continue;
				}
				// 判断是否进入最后三分之一时间
				long curTime = System.currentTimeMillis();
				boolean shouldExtend = (holder.getLastModifyTime() + holder.getModifyPeriod()) <= curTime;
				if (shouldExtend) {
					holder.setLastModifyTime(curTime);
					RedisHelper.expire(holder.getLockKey(), holder.getLockTime(), holder.getTimeUnit());
					log.info("lockKey : [" + holder.getLockKey() + "], try count : " + (holder.getCurrentCount() + 1));
					holder.setCurrentCount(holder.getCurrentCount() + 1);
				}
			}
		}, 0, this.period, TimeUnit.SECONDS);

	}

}
