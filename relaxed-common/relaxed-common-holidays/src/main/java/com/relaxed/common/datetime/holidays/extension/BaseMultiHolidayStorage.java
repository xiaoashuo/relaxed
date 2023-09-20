package com.relaxed.common.datetime.holidays.extension;

import cn.hutool.core.date.DatePattern;
import com.relaxed.common.datetime.holidays.storage.HolidayStorage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Yakir
 * @Topic BaseHolidayStorage
 * @Description
 * @date 2023/8/14 10:22
 * @Version 1.0
 */
public abstract class BaseMultiHolidayStorage implements MultiHolidayStorage {

	@Override
	public boolean isHoliday(Integer source, LocalDate date) {
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		String dateStr = formatNormalDateStr(date);
		// 判断是否为周六或周日
		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			// 需要工作的周末 不算节假日
			return !getNeedWorkWeekends(source).contains(dateStr);
		}
		// 判断节假日是否包含
		return getHolidays(source).contains(dateStr);
	}

	private static String formatNormalDateStr(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
	}

}
