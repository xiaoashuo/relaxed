package com.relaxed.common.datetime.holidays.extension;

import java.time.LocalDate;
import java.time.DateTimeException;

/**
 * 多数据源工作日计算器接口 扩展了基础的工作日计算功能，支持多个不同数据源的工作日计算。 不同的数据源可以是不同的业务场景，如：
 * <ul>
 * <li>标准工作日历（source=1）</li>
 * <li>证券交易日历（source=2）</li>
 * <li>期货交易日历（source=3）</li>
 * <li>其他自定义日历</li>
 * </ul>
 * 每个数据源都可以独立计算自己的工作日。
 *
 * @author Yakir
 * @since 1.0.0
 */
public interface MultiWorkdayCalculator {

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
	 * 获取指定数据源中指定日期的前一个工作日
	 * <p>
	 * 从指定日期向前查找，直到找到第一个工作日。 如果指定日期本身是工作日，则会从前一天开始查找。 工作日的判断会基于指定的数据源。
	 * </p>
	 * @param source 数据源标识：1-标准工作日历，2-证券交易日历，3-期货交易日历，等
	 * @param date 指定日期
	 * @return 前一个工作日的日期
	 */
	default LocalDate getPrevWorkday(Integer source, LocalDate date) {
		return getSpecifiedWorkDay(source, date, 1, false);
	}

	/**
	 * 获取指定数据源中指定日期的下一个工作日
	 * <p>
	 * 从指定日期向后查找，直到找到第一个工作日。 如果指定日期本身是工作日，则会从后一天开始查找。 工作日的判断会基于指定的数据源。
	 * </p>
	 * @param source 数据源标识：1-标准工作日历，2-证券交易日历，3-期货交易日历，等
	 * @param date 指定日期
	 * @return 下一个工作日的日期
	 */
	default LocalDate getNextWorkday(Integer source, LocalDate date) {
		return getSpecifiedWorkDay(source, date, 1, true);
	}

	/**
	 * 获取指定数据源中的指定工作日
	 * <p>
	 * 根据给定的参数，计算指定日期前后的第N个工作日。 计算时会跳过所有节假日，只计算工作日。 工作日的判断会基于指定的数据源。
	 * </p>
	 * @param source 数据源标识：1-标准工作日历，2-证券交易日历，3-期货交易日历，等
	 * @param date 当前指定日期
	 * @param days 需要计算的工作日数量
	 * @param future true表示向后计算，false表示向前计算
	 * @return 计算后的工作日日期
	 */
	LocalDate getSpecifiedWorkDay(Integer source, LocalDate date, int days, boolean future);

	/**
	 * 计算指定数据源中两个日期之间的工作日数量 计算规则：
	 * <ul>
	 * <li>不计算开始日期和结束日期</li>
	 * <li>只计算区间内的工作日数量</li>
	 * <li>如果开始日期大于结束日期，将抛出异常</li>
	 * <li>如果开始日期等于结束日期，返回0</li>
	 * <li>工作日的判断会基于指定的数据源</li>
	 * </ul>
	 * @param source 数据源标识：1-标准工作日历，2-证券交易日历，3-期货交易日历，等
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return 工作日数量
	 * @throws DateTimeException 当开始日期大于结束日期时抛出此异常
	 */
	int calculateDiffDays(Integer source, LocalDate startDate, LocalDate endDate);

}
