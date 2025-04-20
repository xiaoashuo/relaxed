package com.relaxed.common.core.thread;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽象队列线程类，提供了基于队列的线程处理框架。 该类实现了一个通用的队列处理模式，支持批量处理数据、优雅关闭以及异常处理机制。
 *
 * <p>
 * 主要特性：
 * <ul>
 * <li>支持批量处理数据，可配置批处理大小和超时时间</li>
 * <li>实现 Spring 的 {@link InitializingBean} 接口，在bean初始化完成后自动启动线程</li>
 * <li>实现 Spring 的 {@link ApplicationListener} 接口，在应用关闭时优雅停止线程</li>
 * <li>提供数据预处理、处理和错误处理的扩展点</li>
 * </ul>
 *
 * <p>
 * 使用示例: <pre>{@code
 * public class CustomQueueThread extends AbstractQueueThread<String> {
 *     &#64;Override
 *     public void put(String data) {
 *         // 实现数据入队逻辑
 *     }
 *

 *     &#64;Override
 *     public String poll(long time) {
 *         // 实现数据出队逻辑
 *     }
 *
 *     &#64;Override
 *     public void process(List<String> list) {
 *         // 实现批量数据处理逻辑
 *     }
 * }
 * }</pre>
 *
 * @param <T> 队列中元素的类型
 * @author Yakir
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractQueueThread<T> extends Thread
		implements InitializingBean, ApplicationListener<ContextClosedEvent> {

	/**
	 * 默认批处理数据量大小 当队列中的数据达到此数量时，会触发一次批处理
	 */
	private static final int DEFAULT_BATCH_SIZE = 500;

	/**
	 * 默认批处理等待超时时间，单位：毫秒 即使队列中的数据未达到批处理大小，超过此时间后也会触发一次批处理
	 */
	private static final long DEFAULT_BATCH_TIMEOUT_MS = 30 * 1000L;

	/**
	 * 默认从队列中获取数据的超时时间，单位：毫秒 当队列为空时，等待此时间后若仍未获取到数据则返回null
	 */
	private static final long POLL_TIMEOUT_MS = 5 * 1000L;

	/**
	 * 将数据项放入队列中。 该方法是队列写入操作的抽象，子类必须实现具体的入队逻辑。 实现时应考虑以下几点：
	 * <ul>
	 * <li>处理队列已满的情况（阻塞/非阻塞）</li>
	 * <li>确保线程安全</li>
	 * <li>处理中断异常</li>
	 * <li>实现适当的背压机制</li>
	 * </ul>
	 * @param e 要放入队列的数据项，不能为null
	 * @throws NullPointerException 如果参数为null
	 */
	public abstract void put(@NotNull T e);

	/**
	 * 从队列中获取数据项。 该方法是队列读取操作的抽象，子类必须实现具体的出队逻辑。 方法的行为应符合以下特征：
	 * <ul>
	 * <li>如果队列中有数据，立即返回</li>
	 * <li>如果队列为空，等待指定时间后：
	 * <ul>
	 * <li>如果等待期间有数据到达，返回该数据</li>
	 * <li>如果超时仍未有数据，返回null</li>
	 * </ul>
	 * </li>
	 * <li>如果等待过程中线程被中断，抛出中断异常</li>
	 * </ul>
	 * @param time 等待时间，单位：毫秒
	 * @return 队列中的数据项，如果超时返回null
	 * @throws InterruptedException 如果在等待过程中线程被中断
	 */
	@Nullable
	public abstract T poll(long time) throws InterruptedException;

	/**
	 * 检查线程是否正在运行
	 * @return true 表示线程正在运行，false 表示线程已被中断
	 */
	public boolean isRunning() {
		return !isInterrupted();
	}

	/**
	 * 线程启动时的初始化方法。 该方法在线程开始执行数据处理之前被调用，用于执行必要的初始化工作。 子类可以重写此方法以实现自定义的初始化逻辑，例如：
	 * <ul>
	 * <li>初始化资源（数据库连接、缓存等）</li>
	 * <li>加载配置</li>
	 * <li>建立外部系统连接</li>
	 * <li>预热缓存</li>
	 * </ul>
	 *
	 * <p>
	 * 注意：该方法在线程的 {@link #run()} 方法中被调用， 而不是在 Spring 容器初始化 Bean 时调用的
	 * {@link #afterPropertiesSet()} 方法中。
	 */
	public void init() {
	}

	/**
	 * 批处理数据前的预处理方法。 该方法在每次批处理循环开始时被调用，用于准备处理环境和数据。 子类可以重写此方法以实现自定义的预处理逻辑，例如：
	 * <ul>
	 * <li>重置计数器或状态标志</li>
	 * <li>清理临时资源</li>
	 * <li>检查系统状态</li>
	 * <li>准备批处理上下文</li>
	 * </ul>
	 *
	 * <p>
	 * 该方法的执行频率较高（每批次都会调用）， 因此建议避免在此方法中执行耗时操作。
	 */
	public void preProcess() {
	}

	/**
	 * 处理批量数据的核心方法。 该方法是整个批处理流程的核心，由子类实现具体的业务逻辑。 方法在以下场景下被调用：
	 * <ul>
	 * <li>当收集到足够的数据（达到 {@link #getBatchSize()}）时</li>
	 * <li>当等待时间超过限制（{@link #getBatchTimeout()}）且队列中有数据时</li>
	 * </ul>
	 *
	 * <p>
	 * 实现此方法时应注意：
	 * <ul>
	 * <li>方法应该是幂等的，因为在异常情况下可能会重试处理同一批数据</li>
	 * <li>需要合理处理异常，确保数据的一致性和完整性</li>
	 * <li>可以实现事务机制，保证批量操作的原子性</li>
	 * <li>建议添加监控和统计信息，便于问题排查</li>
	 * </ul>
	 *
	 * <p>
	 * 示例实现： <pre>{@code
	 * &#64;Override
	 * public void process(List<T> list) throws Exception {
	 *     try {
	 *         // 开启事务
	 *         transactionTemplate.execute(status -> {
	 *             // 批量处理数据
	 *             for (T item : list) {
	 *                 // 处理单个数据项
	 *                 processItem(item);
	 *             }
	 *             return null;
	 *         });
	 *         // 记录处理成功的统计信息
	 *         metrics.recordSuccess(list.size());
	 *     } catch (Exception e) {
	 *         // 记录处理失败的统计信息
	 *         metrics.recordFailure(list.size());
	 *         throw e;
	 *     }
	 * }
	 * }</pre>
	 * @param list 待处理的数据列表，保证非空且长度不超过 {@link #getBatchSize()}
	 * @throws Exception 处理过程中可能抛出的异常，会被 {@link #errorHandle(Throwable, List)} 方法捕获并处理
	 */
	public abstract void process(List<T> list) throws Exception;

	/**
	 * 线程的主要执行方法，实现了批量数据处理的核心逻辑。 该方法会循环执行以下步骤，直到线程被中断：
	 * <ol>
	 * <li>调用 {@link #init()} 进行初始化</li>
	 * <li>创建一个新的数据列表，用于存储待处理的数据</li>
	 * <li>调用 {@link #preProcess()} 进行数据预处理</li>
	 * <li>通过 {@link #fillList(List)} 填充数据列表</li>
	 * <li>如果线程仍在运行，则调用 {@link #process(List)} 处理数据</li>
	 * <li>如果线程已停止，则调用 {@link #shutdownHandler(List)} 处理剩余数据</li>
	 * </ol>
	 *
	 * <p>
	 * 异常处理：
	 * <ul>
	 * <li>对于可恢复的异常（Exception），调用 {@link #errorHandle(Throwable, List)} 进行处理</li>
	 * <li>对于不可恢复的异常（Error），记录错误日志并重新抛出</li>
	 * </ul>
	 */
	@Override
	public void run() {
		init();
		List<T> list;
		while (isRunning()) {
			list = new ArrayList<>(getBatchSize());
			try {
				preProcess();
				fillList(list);
				if (isRunning()) {
					process(list);
				}
				else {
					shutdownHandler(list);
				}
			}
			catch (Exception ex) {
				errorHandle(ex, list);
			}
			catch (Throwable ex) {
				log.error("线程队列运行异常!", ex);
				throw ex;
			}
		}
	}

	/**
	 * 填充待处理的数据列表。 该方法实现了批量数据收集的核心逻辑，遵循以下规则：
	 * <ol>
	 * <li>持续从队列中获取数据，直到达到以下任一条件：
	 * <ul>
	 * <li>收集的数据量达到批处理大小上限（{@link #getBatchSize()}）</li>
	 * <li>从收集第一个数据开始超过最大等待时间（{@link #getBatchTimeout()}）</li>
	 * <li>线程被中断（{@link #isRunning()} 返回 false）</li>
	 * </ul>
	 * </li>
	 * <li>对于每个非空的数据项：
	 * <ul>
	 * <li>如果是批次中的第一个数据，记录开始时间戳</li>
	 * <li>通过 {@link #processData(List, Object)} 处理数据项</li>
	 * </ul>
	 * </li>
	 * </ol>
	 *
	 * <p>
	 * 该方法通过平衡批处理大小和等待时间，实现了高效的数据批量处理：
	 * <ul>
	 * <li>当数据量充足时，优先保证批处理大小</li>
	 * <li>当数据量不足时，通过超时机制保证处理及时性</li>
	 * </ul>
	 * @param list 待填充的数据列表，方法执行过程中会往该列表中添加数据
	 */
	protected void fillList(List<T> list) {
		long timestamp = 0;
		int count = 0;

		while (count < getBatchSize()) {
			T e = get();

			if (e != null) {
				if (count++ == 0) {
					timestamp = System.currentTimeMillis();
				}
				processData(list, e);
			}

			boolean isBreak = !isRunning()
					|| (!CollectionUtils.isEmpty(list) && System.currentTimeMillis() - timestamp >= getBatchTimeout());
			if (isBreak) {
				break;
			}
		}
	}

	/**
	 * 处理单个数据项并添加到批处理列表中。 该方法提供了在数据项被添加到批处理列表之前进行预处理的机会。 默认实现是直接将数据项添加到列表中，子类可以重写此方法以实现：
	 * <ul>
	 * <li>数据转换或格式化</li>
	 * <li>数据过滤或验证</li>
	 * <li>添加额外的处理逻辑</li>
	 * </ul>
	 * @param list 当前批次的数据列表
	 * @param e 待处理的数据项，已经过非空检查
	 */
	private void processData(List<T> list, T e) {
		list.add(e);
	}

	/**
	 * 从队列中获取单个数据项。 该方法封装了从队列中安全获取数据的逻辑，包括：
	 * <ul>
	 * <li>使用配置的超时时间调用 {@link #poll(long)} 方法</li>
	 * <li>处理中断异常，确保线程状态的正确性</li>
	 * <li>记录中断事件的日志信息</li>
	 * </ul>
	 * @return 队列中的数据项，如果发生以下情况则返回null：
	 * <ul>
	 * <li>队列为空且等待超时</li>
	 * <li>线程被中断</li>
	 * </ul>
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
	 * 处理数据处理过程中发生的异常。 该方法提供了统一的异常处理机制，默认实现是记录错误日志。 子类可以重写此方法以实现自定义的异常处理策略，例如：
	 * <ul>
	 * <li>重试处理失败的数据</li>
	 * <li>将失败的数据保存到错误队列</li>
	 * <li>发送告警通知</li>
	 * <li>执行补偿操作</li>
	 * </ul>
	 * @param ex 处理过程中捕获的异常
	 * @param list 处理失败时的数据列表，可能包含已处理和未处理的数据
	 */
	public void errorHandle(Throwable ex, List<T> list) {
		log.error("{} 线程处理数据出现异常, 数据 {}", getClass().getSimpleName(), list, ex);
	}

	/**
	 * 线程关闭时的数据处理方法。 当线程被中断或应用程序关闭时，该方法会被调用以处理剩余的未处理数据。 默认实现是将未处理的数据记录到错误日志中。子类可以重写此方法以实现：
	 * <ul>
	 * <li>将未处理的数据保存到持久化存储</li>
	 * <li>将数据转移到其他处理队列</li>
	 * <li>执行必要的清理操作</li>
	 * </ul>
	 *
	 * <p>
	 * 注意：该方法的实现应当是幂等的，因为在某些情况下可能会被多次调用。 同时，由于是在线程关闭过程中调用，应当避免耗时的操作。
	 * @param list 未处理完的数据列表
	 */
	public void shutdownHandler(List<T> list) {
		try {
			log.error("{} 类 线程: {} 被关闭. 数据:{}", this.getClass().getSimpleName(), getId(), JSONUtil.toJsonStr(list));
		}
		catch (Throwable e) {
			log.error("{} 类 线程: {} 被关闭. 数据:{}", this.getClass().getSimpleName(), getId(), list);
		}
	}

	/**
	 * Spring Bean 初始化完成后的回调方法。 实现自 {@link InitializingBean} 接口，在 Spring 容器完成 Bean
	 * 的依赖注入后调用。 该方法执行以下操作：
	 * <ol>
	 * <li>设置线程名称为当前类的简单名称，便于调试和监控</li>
	 * <li>启动线程，开始执行数据处理任务</li>
	 * </ol>
	 * @throws Exception 如果初始化过程中发生错误
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		setName(getClass().getSimpleName());
		this.start();
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
		log.warn("{} 类的线程开始关闭! id: {} ", getClass().getSimpleName(), getId());
		this.interrupt();
	}

	/**
	 * 获取批处理的数据量大小 默认返回 {@link #DEFAULT_BATCH_SIZE}，子类可以重写此方法以自定义批处理大小
	 * @return 批处理数据量大小
	 */
	public int getBatchSize() {
		return DEFAULT_BATCH_SIZE;
	}

	/**
	 * 获取批处理的最大等待时间 默认返回 {@link #DEFAULT_BATCH_TIMEOUT_MS}，子类可以重写此方法以自定义等待时间
	 * @return 批处理最大等待时间，单位：毫秒
	 */
	public long getBatchTimeout() {
		return DEFAULT_BATCH_TIMEOUT_MS;
	}

	/**
	 * 获取从队列中获取数据的超时时间 默认返回 {@link #POLL_TIMEOUT_MS}，子类可以重写此方法以自定义超时时间
	 * @return 获取数据的超时时间，单位：毫秒
	 */
	public long getPollTimeoutMs() {
		return POLL_TIMEOUT_MS;
	}

}
