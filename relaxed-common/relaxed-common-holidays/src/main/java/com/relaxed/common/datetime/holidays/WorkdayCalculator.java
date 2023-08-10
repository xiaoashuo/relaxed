package com.relaxed.common.datetime.holidays;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Yakir
 * @Topic WorkdayCalculator
 * @Description
 * @date 2023/8/10 9:40
 * @Version 1.0
 */
public interface WorkdayCalculator {

	/**
	 * 是否为节假日
	 * @param date
	 * @return true 是|false 不是
	 */
	boolean isHoliday(LocalDate date);

	/**
	 * 得到指定日期的前一个工作日
	 * @param date
	 * @return
	 */
	default LocalDate getPrevWorkday(LocalDate date) {
		return getSpecifiedWorkDay(date, 1, false);
	}

	/**
	 * 得到指定日期的下一个工作日
	 * @param date
	 * @return
	 */
	default LocalDate getNextWorkday(LocalDate date) {
		return getSpecifiedWorkDay(date, 1, true);
	}

	/**
	 * 获取指定工作日
	 * @param date 当前指定日期
	 * @param days 向后推几个工作日
	 * @param future true 未来日期| false 前几个工作日
	 * @return 计算后工作日
	 */
	LocalDate getSpecifiedWorkDay(LocalDate date, int days, boolean future);

	/**
	 * 计算两个日期的工作日之差 不算头不算尾
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return 相差天数
	 */
	int calculateDiffDays(LocalDate startDate, LocalDate endDate);

}
