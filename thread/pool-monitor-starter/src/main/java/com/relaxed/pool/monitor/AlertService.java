package com.relaxed.pool.monitor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Yakir
 * @Topic AlertService
 * @Description
 * @date 2025/4/3 16:58
 * @Version 1.0
 */

public interface AlertService {

	void sendAlert(String finalMsg, String[] channels);

}
