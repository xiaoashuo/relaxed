package com.relaxed.test.holidays;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.datetime.holidays.storage.BaseHolidayStorage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Yakir
 * @Topic LocalHolidayStorage
 * @Description
 * @date 2023/8/10 9:56
 * @Version 1.0
 */
public class LocalHolidayStorage extends BaseHolidayStorage {

	/**
	 * 节假日
	 */
	private Set<String> HOLIDAYS = new HashSet<>();

	/**
	 * 需要工作的周末
	 */
	private Set<String> NeedWorkWeekends = new HashSet<>();

	/**
	 * 原始数据
	 */
	private Set<String> ORIGIN_DATE = new HashSet<>();

	public void addOriginData(Collection<String> dates) {
		ORIGIN_DATE.addAll(dates);
		for (String date : dates) {
			if (date.startsWith("!")) {
				// 工作日转休息日
				NeedWorkWeekends.add(StrUtil.removePrefix(date, "!"));
			}
			else {
				HOLIDAYS.add(date);
			}
		}
	}

	public Set<String> getOriginData() {
		return ORIGIN_DATE;
	}

	@Override
	public Collection<String> getHolidays() {
		return HOLIDAYS;
	}

	@Override
	public Collection<String> getNeedWorkWeekends() {
		return NeedWorkWeekends;
	}

	public void delOriginData(Collection<String> dates) {
		for (String date : dates) {
			if (date.startsWith("!")) {
				// 工作日转休息日
				NeedWorkWeekends.remove(StrUtil.removePrefix(date, "!"));
			}
			else {
				HOLIDAYS.remove(date);
			}
			ORIGIN_DATE.remove(date);
		}
	}

}
