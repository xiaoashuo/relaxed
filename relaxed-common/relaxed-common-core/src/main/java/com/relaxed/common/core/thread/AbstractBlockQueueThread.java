package com.relaxed.common.core.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic AbstractBlockQueueThread
 * @Description
 * @date 2021/6/27 22:13
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractBlockQueueThread<T> extends AbstractQueueThread<T> {

	private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

	@Override
	public void put(T t) {
		if (t != null) {
			try {
				queue.put(t);
			}
			catch (Exception e) {
				log.error("{} put Object error, param: {}", this.getClass().toString(), t, e);
			}
		}
	}

	@Override
	@Nullable
	public T poll(long time) throws InterruptedException {
		return queue.poll(time, TimeUnit.MILLISECONDS);
	}

}
