package com.relaxed.common.datetime.holidays;

import cn.hutool.core.date.DatePattern;
import com.relaxed.common.datetime.holidays.storage.HolidayStorage;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Yakir
 * @Topic DefaultWorkDayCalculator
 * @Description
 * @date 2023/8/10 9:50
 * @Version 1.0
 */
@RequiredArgsConstructor
public class DefaultWorkDayCalculator implements WorkdayCalculator {

	private final HolidayStorage holidayStorage;

	@Override
	public boolean isHoliday(LocalDate date) {
		return holidayStorage.contains(formatNormalDateStr(date));
	}

	private static String formatNormalDateStr(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
	}

	@Override
	public LocalDate getSpecifiedWorkDay(LocalDate date, int days, boolean future) {
		// 后指定天数的节假日
		LocalDate tmpDate = date;
		do {
			tmpDate = future ? tmpDate.plusDays(1) : tmpDate.minusDays(1);
			if (this.isHoliday(tmpDate)) {
				// 若为节假日 跳过
				continue;
			}
			// 若为有效日期 则递减一次
			days--;
		}
		while (days != 0);
		return tmpDate;
	}

	@Override
	public int calculateDiffDays(LocalDate startDate, LocalDate endDate) {
		int diffDays = 0;
		// 基础参数检查
		if (startDate.compareTo(endDate) > 0) {
			// 开始日期 大于结束日期 抛出异常
			throw new DateTimeException("开始日期不能大于结束日期");
		}
		if (startDate.compareTo(endDate) == 0) {
			// 开始日期 等于结束日期 直接返回0天间隔
			return diffDays;
		}
		// 若递增后日期 大于等于结束日期 则结束循环
		LocalDate tmpDate = startDate;
		do {
			tmpDate = tmpDate.plusDays(1);
			if (tmpDate.compareTo(endDate) >= 0 || this.isHoliday(tmpDate)) {
				// 临时日期 是否超出结束日期，超出跳过 若为节假日 跳过
				continue;
			}
			// 若为有效日期 则递增一次
			diffDays++;
		}
		while (tmpDate.compareTo(endDate) < 0);
		return diffDays;
	}

}
