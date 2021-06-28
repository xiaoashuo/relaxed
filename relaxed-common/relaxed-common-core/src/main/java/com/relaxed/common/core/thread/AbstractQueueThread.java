package com.relaxed.common.core.thread;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic AbstractQueueThread
 * @Description
 * @date 2021/6/27 20:25
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractQueueThread<T> extends Thread
		implements InitializingBean, ApplicationListener<ContextClosedEvent> {

	/**
	 * 默认缓存数据数量
	 */
	private static final int DEFAULT_BATCH_SIZE = 500;

	/**
	 * 默认等待时长 30秒；单位 毫秒
	 */
	private static final long DEFAULT_BATCH_TIMEOUT_MS = 30 * 1000L;

	/**
	 * 默认获取数据时的超时时间
	 */
	private static final long POLL_TIMEOUT_MS = 5 * 1000L;

	/**
	 * 往队列插入数据
	 * @param e 数据
	 */
	public abstract void put(@NotNull T e);

	/**
	 * 从队列中取值
	 * @param time 等待时长, 单位 毫秒
	 * @return E
	 * @throws InterruptedException 线程中断
	 */
	@Nullable
	public abstract T poll(long time) throws InterruptedException;

	/**
	 * 线程是否中断
	 * @return boolean true 未中断
	 */
	public boolean isRunning() {
		// 未被中断表示可以继续运行
		return !isInterrupted();
	}

	/**
	 * 预处理数据
	 */
	public void preProcess() {
	}

	/**
	 * 处理数据
	 * @param list 列表
	 * @throws Exception
	 */
	public abstract void process(List<T> list) throws Exception;

	@Override
	public void run() {
		List<T> list;
		while (isRunning()) {
			list = new ArrayList<>(getBatchSize());
			try {
				// 预处理
				preProcess();
				// 填充数据
				fillList(list);
				// 处理数据
				if (isRunning()) {
					process(list);
				}
				else {
					shutdownHandler(list);
				}
			}
			catch (Throwable ex) {
				errorHandle(ex, list);
			}
		}
	}

	protected void fillList(List<T> list) {
		long timestamp = 0;
		int count = 0;

		while (count < getBatchSize()) {
			T e = get();

			if (e != null) {
				// 第一次插入数据
				if (count++ == 0) {
					// 记录时间
					timestamp = System.currentTimeMillis();
				}
				fillData(list, e);
			}

			// 无法继续运行 或 已有数据且超过设定的等待时间
			boolean isBreak = !isRunning()
					|| (!CollectionUtils.isEmpty(list) && System.currentTimeMillis() - timestamp >= getBatchTimeout());
			if (isBreak) {
				break;
			}
		}
	}

	/**
	 * 填充数据 可以在这里对数据进行一次包装
	 * @param list
	 * @param e
	 */
	private void fillData(List<T> list, T e) {
		list.add(e);
	}

	/**
	 * 从队列中取出数据
	 * @return T
	 */
	private T get() {
		T e = null;
		try {
			e = poll(getPollTimeoutMs());
		}
		catch (InterruptedException ex) {
			interrupt();
			log.error("{} 类的poll线程被中断!id: {}", getClass().getSimpleName(), getId());
		}
		return e;
	}

	/**
	 * 数据处理时的异常回调
	 * @param ex 异常
	 * @param list 当时的数据
	 */
	public void errorHandle(Throwable ex, List<T> list) {
		log.error("{} 线程处理数据出现异常, 数据 {}", getClass().getSimpleName(), list, ex);
	}

	/**
	 * 执行过此方法紧接着就会卸载bean
	 * @param list 当前数据
	 */
	public void shutdownHandler(List<T> list) {
		try {
			log.error("{} 类 线程: {} 被关闭. 数据:{}", this.getClass().getSimpleName(), getId(), JSONUtil.toJsonStr(list));
		}
		catch (Throwable e) {
			log.error("{} 类 线程: {} 被关闭. 数据:{}", this.getClass().getSimpleName(), getId(), list);

		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setName(getClass().getSimpleName());
		this.start();
	}

	/**
	 * spring 销毁bean时 会发出一个应用关闭事件
	 * @see org.springframework.context.support.AbstractApplicationContext close
	 * @param contextClosedEvent
	 */
	@Override
	public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
		log.warn("{} 类的线程开始关闭! id: {} ", getClass().getSimpleName(), getId());
		this.interrupt();
	}

	/**
	 * 批次数量
	 * @return long
	 */
	public int getBatchSize() {
		return DEFAULT_BATCH_SIZE;
	}

	/**
	 * 批次最大等待时长
	 * @return 返回时长，单位毫秒
	 */
	public long getBatchTimeout() {
		return DEFAULT_BATCH_TIMEOUT_MS;
	}

	/**
	 * 获取数据的超时时间
	 * @return 返回时长，单位毫秒
	 */
	public long getPollTimeoutMs() {
		return POLL_TIMEOUT_MS;
	}

}
