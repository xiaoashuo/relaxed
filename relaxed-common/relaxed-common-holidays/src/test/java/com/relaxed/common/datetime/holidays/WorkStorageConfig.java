package com.relaxed.common.datetime.holidays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yakir
 * @Topic WorkStorageConfig
 * @Description
 * @date 2023/8/14 10:17
 * @Version 1.0
 */
@Configuration
public class WorkStorageConfig {

	@Bean
	public LocalHolidayStorage holidayStorage() {
		return new LocalHolidayStorage();
	}

}
