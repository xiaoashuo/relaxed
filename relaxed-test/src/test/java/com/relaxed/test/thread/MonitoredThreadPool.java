package com.relaxed.test.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic MonitoredThreadPool
 * @Description
 *    监控包装的线程池
 *    拦截执行和拒绝事件
 * @date 2025/4/3 16:55
 * @Version 1.0
 */
@Slf4j
public class MonitoredThreadPool extends ThreadPoolExecutor {
    private final String name;
    private final ThreadPoolExecutor originalExecutor;
    private long rejectedCount = 0;


    /**
     * 最后一次参数更新时间
     */
    private long lastUpdateTimeMills;

    public MonitoredThreadPool(String name, ThreadPoolExecutor executor) {
        super(
                executor.getCorePoolSize(),
                executor.getMaximumPoolSize(),
                executor.getKeepAliveTime(TimeUnit.NANOSECONDS),
                TimeUnit.NANOSECONDS,
                executor.getQueue(),
                executor.getThreadFactory(),
                executor.getRejectedExecutionHandler()
        );

        this.name = name;
        this.originalExecutor = executor;
        this.lastUpdateTimeMills=System.currentTimeMillis();
    }

    public ThreadPoolExecutor getOriginalExecutor() {
        return originalExecutor;
    }

    public long getRejectedCount() {
        return rejectedCount;
    }

    public long getLastUpdateTimeMills() {
        return lastUpdateTimeMills;
    }

    public void setLastUpdateTimeMills(long lastUpdateTimeMills) {
        this.lastUpdateTimeMills = lastUpdateTimeMills;
    }

    /**
     * 拒绝策略包装器
     */
    private class RejectedHandler implements RejectedExecutionHandler {
        private final RejectedExecutionHandler original;

        public RejectedHandler(RejectedExecutionHandler original) {
            this.original = original;
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            rejectedCount++;
            log.warn("线程池 [{}] 拒绝任务，当前拒绝总数: {}", name, rejectedCount);
            original.rejectedExecution(r, executor);
        }
    }
}
