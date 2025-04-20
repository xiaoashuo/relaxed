package com.relaxed.common.datetime.holidays.storage;

import java.time.LocalDate;
import java.util.Collection;

/**
 * 节假日存储接口 定义了节假日数据的存储和访问方法。实现类可以从不同的数据源（如数据库、配置文件等） 获取节假日信息。节假日信息包括：
 * <ul>
 * <li>法定节假日</li>
 * <li>需要补班的周末</li>
 * </ul>
 *
 * @author Yakir
 * @since 1.0.0
 */
public interface HolidayStorage {

	/**
	 * 判断指定日期是否为节假日 判断逻辑：
	 * <ul>
	 * <li>如果是周末（周六或周日）且不在需要补班的周末列表中，则为节假日</li>
	 * <li>如果在节假日列表中，则为节假日</li>
	 * <li>其他情况均为工作日</li>
	 * </ul>
	 * @param date 需要判断的日期
	 * @return true 表示是节假日，false 表示是工作日
	 */
	boolean isHoliday(LocalDate date);

	/**
	 * 获取所有节假日列表
	 * <p>
	 * 返回系统中配置的所有法定节假日。日期格式为 "yyyy-MM-dd"。 不包含普通的周末（周六和周日）。
	 * </p>
	 * @return 节假日集合，每个元素为日期字符串，格式：yyyy-MM-dd
	 */
	Collection<String> getHolidays();

	/**
	 * 获取需要补班的周末列表
	 * <p>
	 * 返回系统中配置的所有需要补班的周末。这些日期虽然是周末，但需要正常上班。 日期格式为 "yyyy-MM-dd"。
	 * </p>
	 * @return 需要补班的周末集合，每个元素为日期字符串，格式：yyyy-MM-dd
	 */
	Collection<String> getNeedWorkWeekends();

}
