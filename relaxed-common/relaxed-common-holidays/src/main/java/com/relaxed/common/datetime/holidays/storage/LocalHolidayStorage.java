package com.relaxed.common.datetime.holidays.storage;

import cn.hutool.core.date.DatePattern;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Yakir
 * @Topic LocalHolidayStorage
 * @Description
 * @date 2023/8/10 9:56
 * @Version 1.0
 */
public class LocalHolidayStorage implements HolidayStorage {

	private Set<String> HOLIDAYS = new HashSet<>();

	@Override
	public void add(String date) {
		HOLIDAYS.add(date);
	}

	@Override
	public void addAll(Collection<String> dates) {
		HOLIDAYS.addAll(dates);
	}

	@Override
	public Collection<String> list() {
		return HOLIDAYS;
	}

	@Override
	public boolean contains(String date) {
		return HOLIDAYS.contains(date);
	}

	@Override
	public void del(String date) {
		HOLIDAYS.remove(date);
	}

	@Override
	public void delAll(Collection<String> dates) {
		HOLIDAYS.removeAll(dates);
	}

	@Override
	public void clear() {
		HOLIDAYS.clear();
	}

}
