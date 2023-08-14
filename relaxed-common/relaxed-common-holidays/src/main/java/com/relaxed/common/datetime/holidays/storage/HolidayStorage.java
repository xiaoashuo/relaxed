package com.relaxed.common.datetime.holidays.storage;

import java.time.LocalDate;
import java.util.Collection;

/**
 * @author Yakir
 * @Topic HolidayStorage
 * @Description
 * @date 2023/8/10 9:54
 * @Version 1.0
 */
public interface HolidayStorage {

	/**
	 * 是否包含指定日期
	 * @param date
	 * @return
	 */
	boolean isHoliday(LocalDate date);

	/**
	 * 获取节假日
	 * @return
	 */
	Collection<String> getHolidays();

	/**
	 * 获取需要工作的周末
	 * @return
	 */
	Collection<String> getNeedWorkWeekends();

}
