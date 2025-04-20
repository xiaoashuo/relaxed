package com.relaxed.common.datetime.holidays.extension;

import com.relaxed.common.datetime.holidays.DateTimeException;
import com.relaxed.common.datetime.holidays.WorkdayCalculator;
import com.relaxed.common.datetime.holidays.storage.HolidayStorage;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class DefaultMultiWorkDayCalculator implements MultiWorkdayCalculator {

	private final MultiHolidayStorage multiHolidayStorage;

	@Override
	public boolean isHoliday(Integer source, LocalDate date) {
		return multiHolidayStorage.isHoliday(source, date);
	}

	@Override
	public LocalDate getSpecifiedWorkDay(Integer source, LocalDate date, int days, boolean future) {
		// 后指定天数的节假日
		LocalDate tmpDate = date;
		do {
			tmpDate = future ? tmpDate.plusDays(1) : tmpDate.minusDays(1);
			if (this.isHoliday(source, tmpDate)) {
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
	public int calculateDiffDays(Integer source, LocalDate startDate, LocalDate endDate) {
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
			if (tmpDate.compareTo(endDate) >= 0 || this.isHoliday(source, tmpDate)) {
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
