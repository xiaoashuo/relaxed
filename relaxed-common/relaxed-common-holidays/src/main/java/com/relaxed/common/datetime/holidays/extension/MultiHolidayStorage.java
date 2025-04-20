package com.relaxed.common.datetime.holidays.extension;

import java.time.LocalDate;
import java.util.Collection;

/**
 * 多数据源节假日存储接口 扩展了基础的节假日存储功能，支持多个不同数据源的节假日信息管理。 不同的数据源可以是不同的业务场景，如：
 * <ul>
 * <li>标准工作日历（source=1）</li>
 * <li>证券交易日历（source=2）</li>
 * <li>期货交易日历（source=3）</li>
 * <li>其他自定义日历</li>
 * </ul>
 * 每个数据源都可以独立维护自己的节假日和补班周末信息。
 *
 * @author Yakir
 * @since 1.0.0
 */
public interface MultiHolidayStorage {

	/**
	 * 判断指定日期在指定数据源中是否为节假日 判断逻辑：
	 * <ul>
	 * <li>如果是周末（周六或周日）且不在该数据源需要补班的周末列表中，则为节假日</li>
	 * <li>如果在该数据源的节假日列表中，则为节假日</li>
	 * <li>其他情况均为工作日</li>
	 * </ul>
	 * @param source 数据源标识：1-标准工作日历，2-证券交易日历，3-期货交易日历，等
	 * @param date 需要判断的日期
	 * @return true 表示是节假日，false 表示是工作日
	 */
	boolean isHoliday(Integer source, LocalDate date);

	/**
	 * 获取指定数据源的所有节假日列表
	 * <p>
	 * 返回指定数据源中配置的所有法定节假日。日期格式为 "yyyy-MM-dd"。 不包含普通的周末（周六和周日）。
	 * </p>
	 * @param source 数据源标识：1-标准工作日历，2-证券交易日历，3-期货交易日历，等
	 * @return 节假日集合，每个元素为日期字符串，格式：yyyy-MM-dd
	 */
	Collection<String> getHolidays(Integer source);

	/**
	 * 获取指定数据源需要补班的周末列表
	 * <p>
	 * 返回指定数据源中配置的所有需要补班的周末。这些日期虽然是周末，但需要正常上班。 日期格式为 "yyyy-MM-dd"。
	 * </p>
	 * @param source 数据源标识：1-标准工作日历，2-证券交易日历，3-期货交易日历，等
	 * @return 需要补班的周末集合，每个元素为日期字符串，格式：yyyy-MM-dd
	 */
	Collection<String> getNeedWorkWeekends(Integer source);

}
