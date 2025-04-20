package com.relaxed.common.datetime.holidays;

import com.relaxed.common.datetime.holidays.extension.MultiWorkdayCalculator;
import com.relaxed.common.datetime.holidays.storage.HolidayStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 节假日自动配置类 提供节假日相关功能的自动配置，包括：
 * <ul>
 * <li>自动配置工作日计算器 {@link WorkdayCalculator}</li>
 * <li>当没有自定义实现时，使用默认的工作日计算器 {@link DefaultWorkDayCalculator}</li>
 * <li>支持多数据源工作日计算器的配置</li>
 * </ul>
 * 使用此配置类需要提供一个 {@link HolidayStorage} 的实现类作为Bean。
 *
 * @author Yakir
 * @since 1.0.0
 * @see WorkdayCalculator
 * @see DefaultWorkDayCalculator
 * @see MultiWorkdayCalculator
 * @see HolidayStorage
 */
@Configuration
public class HolidayAutoConfiguration {

	/**
	 * 配置默认的工作日计算器
	 * <p>
	 * 当容器中没有 {@link WorkdayCalculator} 和 {@link MultiWorkdayCalculator} 的实现时，
	 * 创建一个默认的工作日计算器实例。
	 * </p>
	 * @param holidayStorage 节假日存储接口的实现
	 * @return 工作日计算器实例
	 */
	@Bean
	@ConditionalOnMissingBean(value = { WorkdayCalculator.class, MultiWorkdayCalculator.class })
	public WorkdayCalculator workDayCalculator(HolidayStorage holidayStorage) {
		return new DefaultWorkDayCalculator(holidayStorage);
	}

}
