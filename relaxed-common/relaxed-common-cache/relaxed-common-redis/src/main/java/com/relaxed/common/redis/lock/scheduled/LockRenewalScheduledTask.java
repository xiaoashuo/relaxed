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
 * @author Yakir
 * @Topic TaskRenewalScheduled
 * @Description
 * @date 2022/10/12 16:49
 * @Version 1.0
 */
@Slf4j
public class LockRenewalScheduledTask {

	private final String threadPoolName;

	private final Integer corePoolSize;

	private final Integer period;

	private final ScheduledExecutorService scheduler;

	public LockRenewalScheduledTask() {
		this("redisLock-schedule-pool", 1, 2);
	}

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
