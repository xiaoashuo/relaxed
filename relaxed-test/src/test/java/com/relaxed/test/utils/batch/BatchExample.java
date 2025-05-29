package com.relaxed.test.utils.batch;

import com.relaxed.common.core.util.batch.BatchUtil;
import com.relaxed.common.core.util.batch.core.BatchConfig;
import com.relaxed.common.core.util.batch.core.BatchContext;
import com.relaxed.common.core.util.batch.core.BatchParam;
import com.relaxed.common.core.util.batch.core.BatchProgress;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

/**
 * BatchBak 使用示例
 */
@Slf4j
public class BatchExample {

	@Data
	public static class UserData {

		private Long id;

		private String name;

		private String email;

		private String phone;

		private Integer status;

	}

	public static void main(String[] args) {
		// 1. 创建线程池
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, // 核心线程数
				10, // 最大线程数
				60L, // 空闲线程存活时间
				TimeUnit.SECONDS, // 时间单位
				new LinkedBlockingQueue<>(1000), // 工作队列
				new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
		);

		// 3. 创建批量处理配置
		BatchConfig<UserData> config = BatchConfig.builder(UserData.class).taskName("用户数据处理").totalCount(1000) // 总数据量
				.batchSize(100) // 每批处理数量
				.async(true) // 异步处理
				.executor(executor) // 线程池
				.failFast(false) // 启用快速失败
				.provider(batchParam -> generateUserData(batchParam)) // 数据提供者
				.consumer(context -> processUserData(context)) // 数据处理者
				.maxExceptions(5).exceptionHandler(exceptions -> handleExceptions(exceptions)) // 异常处理器
				.exceptionRecordHandler(new BiConsumer<BatchContext<UserData>, Throwable>() {
					@Override
					public void accept(BatchContext<UserData> userDataBatchContext, Throwable throwable) {
						log.error("当前异常可以兜底:{}", throwable.getMessage(), throwable);
					}
				}).progressListener(progress -> logProgress(progress)) // 进度监听器
				.build();

		try {
			// 4. 执行批量处理
			BatchUtil.execute(config);
		}
		finally {
			// 5. 关闭线程池
			executor.shutdown();
			try {
				if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
					executor.shutdownNow();
				}
			}
			catch (InterruptedException e) {
				executor.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * 生成测试数据
	 */
	private static List<UserData> generateUserData(BatchParam batchParam) {
		List<UserData> dataList = new ArrayList<>();
		int startIndex = batchParam.getStartIndex();
		int size = batchParam.getSize();

		for (int i = 0; i < size; i++) {
			UserData user = new UserData();
			user.setId((long) (startIndex + i));
			user.setName("User" + (startIndex + i));
			user.setEmail("user" + (startIndex + i) + "@example.com");
			user.setPhone("138" + String.format("%08d", startIndex + i));
			user.setStatus(1);
			dataList.add(user);
		}

		return dataList;
	}

	/**
	 * 处理用户数据
	 */
	private static void processUserData(BatchContext<UserData> context) {
		UserData user = context.getData();

		// 模拟处理逻辑
		if (user.getId() % 100 == 0) {
			throw new RuntimeException("模拟处理失败: " + user.getId());
		}

		// 模拟处理耗时
		try {
			Thread.sleep(10);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("处理被中断", e);
		}

		log.debug("处理用户数据: {}", user.getId());
	}

	/**
	 * 处理异常
	 */
	private static void handleExceptions(List<Throwable> exceptions) {
		log.error("处理过程中发生异常，共{}个异常", exceptions.size());
		for (int i = 0; i < exceptions.size(); i++) {
			log.error("异常{}: {}", i + 1, exceptions.get(i).getMessage());
		}
	}

	/**
	 * 记录进度
	 */
	private static void logProgress(BatchProgress progress) {
		double percentage = (double) progress.getCurrent() / progress.getTotal() * 100;
		log.info("进度: {}/{} ({}%), 当前组: {}/{}", progress.getCurrent(), progress.getTotal(),
				String.format("%.2f", percentage), progress.getGroupNo(), progress.getGroupTotal());
	}

}
