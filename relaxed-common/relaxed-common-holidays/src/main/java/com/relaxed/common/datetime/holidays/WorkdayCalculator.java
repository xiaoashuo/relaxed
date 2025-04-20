package com.relaxed.common.datetime.holidays;

import java.time.LocalDate;
import java.util.List;

/**
 * 工作日计算器接口 提供工作日相关的计算功能，包括：
 * <ul>
 * <li>判断指定日期是否为节假日</li>
 * <li>获取指定日期的前一个工作日</li>
 * <li>获取指定日期的下一个工作日</li>
 * <li>获取指定日期前后N个工作日</li>
 * <li>计算两个日期之间的工作日数量</li>
 * </ul>
 *
 * @author Yakir
 * @since 1.0.0
 */
public interface WorkdayCalculator {

	/**
	 * 判断指定日期是否为节假日 节假日包括：
	 * <ul>
	 * <li>法定节假日</li>
	 * <li>周末（周六和周日，但不包括需要补班的周末）</li>
	 * </ul>
	 * @param date 需要判断的日期
	 * @return true 表示是节假日，false 表示是工作日
	 */
	boolean isHoliday(LocalDate date);

	/**
	 * 获取指定日期的前一个工作日
	 * <p>
	 * 从指定日期向前查找，直到找到第一个工作日。 如果指定日期本身是工作日，则会从前一天开始查找。
	 * </p>
	 * @param date 指定日期
	 * @return 前一个工作日的日期
	 */
	default LocalDate getPrevWorkday(LocalDate date) {
		return getSpecifiedWorkDay(date, 1, false);
	}

	/**
	 * 获取指定日期的下一个工作日
	 * <p>
	 * 从指定日期向后查找，直到找到第一个工作日。 如果指定日期本身是工作日，则会从后一天开始查找。
	 * </p>
	 * @param date 指定日期
	 * @return 下一个工作日的日期
	 */
	default LocalDate getNextWorkday(LocalDate date) {
		return getSpecifiedWorkDay(date, 1, true);
	}

	/**
	 * 获取指定工作日
	 * <p>
	 * 根据给定的参数，计算指定日期前后的第N个工作日。 计算时会跳过所有节假日，只计算工作日。
	 * </p>
	 * @param date 当前指定日期
	 * @param days 需要计算的工作日数量
	 * @param future true表示向后计算，false表示向前计算
	 * @return 计算后的工作日日期
	 */
	LocalDate getSpecifiedWorkDay(LocalDate date, int days, boolean future);

	/**
	 * 计算两个日期之间的工作日数量 计算规则：
	 * <ul>
	 * <li>不计算开始日期和结束日期</li>
	 * <li>只计算区间内的工作日数量</li>
	 * <li>如果开始日期大于结束日期，将抛出异常</li>
	 * <li>如果开始日期等于结束日期，返回0</li>
	 * </ul>
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return 工作日数量
	 * @throws DateTimeException 当开始日期大于结束日期时抛出此异常
	 */
	int calculateDiffDays(LocalDate startDate, LocalDate endDate);

}
