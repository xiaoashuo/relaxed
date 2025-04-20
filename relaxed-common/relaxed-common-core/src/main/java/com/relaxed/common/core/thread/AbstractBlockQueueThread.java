package com.relaxed.common.core.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 基于阻塞队列实现的线程处理框架抽象类。 该类提供了一个基于 {@link LinkedBlockingQueue} 的线程处理实现，用于处理异步任务。 继承自
 * {@link AbstractQueueThread}，实现了队列的基本操作，包括元素的添加和获取。
 *
 * <p>
 * 使用示例: <pre>{@code
 * public class CustomThread extends AbstractBlockQueueThread<String> {
 *     &#64;Override
 *     public void process(List<String> list) {
 *         // 处理队列中的元素
 *     }
 * }
 * }</pre>
 *
 * @param <T> 队列中元素的类型
 * @author Yakir
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractBlockQueueThread<T> extends AbstractQueueThread<T> {

	/**
	 * 底层阻塞队列，用于存储待处理的元素
	 */
	private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

	/**
	 * 将元素放入队列中。 如果队列已满，该方法会阻塞直到有空间可用。
	 * @param t 要添加到队列中的元素，如果为null则不会被添加
	 */
	@Override
	public void put(T t) {
		if (t != null) {
			try {
				queue.put(t);
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			catch (Exception e) {
				log.error("{} put Object error, param: {}", this.getClass().toString(), t, e);
			}
		}
	}

	/**
	 * 从队列中获取元素，如果队列为空则等待指定的时间。
	 * @param time 等待的时间（毫秒）
	 * @return 队列中的元素，如果超时则返回null
	 * @throws InterruptedException 如果等待过程中线程被中断
	 */
	@Override
	@Nullable
	public T poll(long time) throws InterruptedException {
		return queue.poll(time, TimeUnit.MILLISECONDS);
	}

}
