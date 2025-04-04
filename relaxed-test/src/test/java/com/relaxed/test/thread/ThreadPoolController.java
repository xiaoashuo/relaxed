package com.relaxed.test.thread;

import com.relaxed.pool.monitor.annotation.ThreadPoolMonitor;
import com.relaxed.pool.monitor.monitor.ThreadPoolStats;
import com.relaxed.pool.monitor.monitor.ThreadPoolTaskMonitor;
import com.relaxed.pool.monitor.monitor.ThreadPoolTrend;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic ThreadPoolController
 * @Description
 * @date 2025/4/3 17:32
 * @Version 1.0
 */

@RequestMapping("/api/thread-pools")
public class ThreadPoolController {

	private final ThreadPoolTaskMonitor monitor;

	public ThreadPoolController(ThreadPoolTaskMonitor monitor) {
		this.monitor = monitor;
	}

	@GetMapping("/stats")
	public List<ThreadPoolStats> getAllStats() {
		return monitor.getAllPoolStats();
	}

	@GetMapping("/trend")
	public Map<String, ThreadPoolTrend> getAllTrends() {
		Map<String, ThreadPoolTrend> trends = new HashMap<>();
		monitor.getAllPoolStats().forEach(stats -> {
			trends.put(stats.getPoolName(), monitor.getTrend(stats.getPoolName()));
		});
		return trends;
	}

}
