package com.relaxed.common.datetime.holidays;

import com.relaxed.common.datetime.holidays.extension.MultiWorkdayCalculator;
import com.relaxed.common.datetime.holidays.storage.HolidayStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yakir
 * @Topic HolidayAutoConfiguration
 * @Description
 * @date 2023/8/10 11:03
 * @Version 1.0
 */
@Configuration
public class HolidayAutoConfiguration {

	/**
	 * 节假日计算
	 * @param holidayStorage
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(value = { WorkdayCalculator.class, MultiWorkdayCalculator.class })
	public WorkdayCalculator workDayCalculator(HolidayStorage holidayStorage) {
		return new DefaultWorkDayCalculator(holidayStorage);
	}

}
