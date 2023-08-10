package com.relaxed.common.datetime.holidays.storage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * @author Yakir
 * @Topic HolidayStorage
 * @Description 日期格式统一为yyyy-MM-dd
 * @date 2023/8/10 9:54
 * @Version 1.0
 */
public interface HolidayStorage {

	/**
	 * 添加节假日日期
	 * @param date 日期
	 */
	void add(String date);

	/**
	 * 添加节假日日期
	 * @param dates 日期列表
	 */
	void addAll(Collection<String> dates);

	/**
	 * 节假日列表
	 * @return
	 */
	Collection<String> list();

	/**
	 * 是否包含指定日期
	 * @param date
	 * @return
	 */
	boolean contains(String date);

	/**
	 * 删除节假日
	 * @param date
	 */
	void del(String date);

	/**
	 * 删除指定节假日对象
	 * @param dates
	 */
	void delAll(Collection<String> dates);

	/**
	 * 清空所有
	 */
	void clear();

}
