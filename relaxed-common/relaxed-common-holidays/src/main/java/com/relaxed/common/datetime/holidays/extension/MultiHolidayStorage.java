package com.relaxed.common.datetime.holidays.extension;

import java.time.LocalDate;
import java.util.Collection;

/**
 * @author Yakir
 * @Topic HolidayStorage
 * @Description
 * @date 2023/8/10 9:54
 * @Version 1.0
 */
public interface MultiHolidayStorage {

	/**
	 * 是否包含指定日期
	 * @param source 数据源 1标准工作日 2.证券交易日3.期货交易日 等
	 * @param date
	 * @return
	 */
	boolean isHoliday(Integer source, LocalDate date);

	/**
	 * 获取节假日
	 * @param source 数据源 1标准工作日 2.证券交易日3.期货交易日 等
	 * @return
	 */
	Collection<String> getHolidays(Integer source);

	/**
	 * 获取需要工作的周末
	 * @param source 数据源 1标准工作日 2.证券交易日3.期货交易日 等
	 * @return
	 */
	Collection<String> getNeedWorkWeekends(Integer source);

}
