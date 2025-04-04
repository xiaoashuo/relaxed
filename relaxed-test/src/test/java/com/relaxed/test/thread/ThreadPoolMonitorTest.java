package com.relaxed.test.thread;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.thread.ThreadUtil;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic ThreadPoolMonitorTest
 * @Description
 * 监控面板展示的关键指标：
 * <pre>
 *     指标名称	  健康值	   警告值	   危险值	    当前值
 *     活跃线程数	  <70%	   70%-85%	   >85%	        56%
 *     队列使用率	  <60%	   60%-80%	   >80%	        23%
 *     任务完成率	  >95%	   85%-95%	   <85%	        99.8%
 *     拒绝任务数	   0	   <10	       ≥10	        0
 *     平均执行时间 <300ms   300ms-800ms >800ms	    78ms
 * </pre>
 * @date 2025/4/3 17:03
 * @Version 1.0
 */
public class ThreadPoolMonitorTest {

    public static void main(String[] args) {
        monitorTest();
    }
    public static void monitorTest(){
        AlertService alertService = new AlertService();

        // 创建线程池
        ThreadPoolExecutor  orderProcessExecutor = new ThreadPoolExecutor(
                3,                 // 核心线程数
                5,                 // 最大线程数
                60, TimeUnit.SECONDS, // 空闲线程存活时间
                new LinkedBlockingQueue<>(200), // 工作队列
                new NamedThreadFactory("order-process-",false),
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
        );
        // 注册到监控系统
        ThreadPoolMonitorProperties monitorProperties = new ThreadPoolMonitorProperties();
        monitorProperties.setMonitorEnabled(true);
        monitorProperties.setAlertThreshold(80);
        monitorProperties.setAlertChannels("email,wechat");
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(alertService, monitorProperties);



        orderProcessExecutor = threadPoolMonitor.register("订单线程池", orderProcessExecutor);

        for (int i = 0; i < 1000; i++) {
            PoolOrder poolOrder = new PoolOrder();
            poolOrder.setUsername("username"+i);
            processOrderAsync(poolOrder,orderProcessExecutor);
        }
        ThreadUtil.safeSleep(80000);
    }

    // 业务方法
    public static CompletableFuture<String> processOrderAsync(PoolOrder order, ThreadPoolExecutor  orderProcessExecutor) {
        return CompletableFuture.supplyAsync(() -> {
            // 处理订单逻辑
            return doProcessOrder(order);
        }, orderProcessExecutor);
    }

    private  static String doProcessOrder(PoolOrder order) {
        System.out.println("当前处理到order"+Thread.currentThread().getName());
        ThreadUtil.safeSleep(3000);
        return "success";
    }
    // 添加自动伸缩能力
//    @Bean
//    public ThreadPoolAutoScaler threadPoolAutoScaler(ThreadPoolMonitor monitor) {
//        return new ThreadPoolAutoScaler(monitor)
//                .addRule("订单处理线程池", pool -> {
//                    // 根据每小时订单量动态调整线程池大小
//                    int hourOfDay = LocalDateTime.now().getHour();
//                    int baseSize = 10;
//
//                    // 根据历史数据，12-18点是高峰期
//                    if (hourOfDay >= 12 && hourOfDay <= 18) {
//                        return baseSize * 3;  // 高峰期扩容3倍
//                    } else if ((hourOfDay >= 9 && hourOfDay < 12) ||
//                            (hourOfDay > 18 && hourOfDay <= 20)) {
//                        return baseSize * 2;  // 次高峰期扩容2倍
//                    } else {
//                        return baseSize;      // 普通时段保持基础大小
//                    }
//                });
//    }


}
