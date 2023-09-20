package com.relaxed.common.datetime.holidays.extension;

import java.time.LocalDate;

/**
 * @author Yakir
 * @Topic WorkdayCalculator
 * @Description
 * @date 2023/8/10 9:40
 * @Version 1.0
 */
public interface MultiWorkdayCalculator {

	/**
	 * 是否为节假日
	 * @param source 数据源 1标准工作日 2.证券交易日3.期货交易日 等
	 * @param date
	 * @return true 是|false 不是
	 */
	boolean isHoliday(Integer source, LocalDate date);

	/**
	 * 得到指定日期的前一个工作日
	 * @param source 数据源 1标准工作日 2.证券交易日3.期货交易日 等
	 * @param date
	 * @return
	 */
	default LocalDate getPrevWorkday(Integer source, LocalDate date) {
		return getSpecifiedWorkDay(source, date, 1, false);
	}

	/**
	 * 得到指定日期的下一个工作日
	 * @param source 数据源 1标准工作日 2.证券交易日3.期货交易日 等
	 * @param date
	 * @return
	 */
	default LocalDate getNextWorkday(Integer source, LocalDate date) {
		return getSpecifiedWorkDay(source, date, 1, true);
	}

	/**
	 * 获取指定工作日
	 * @param source 数据源 1标准工作日 2.证券交易日3.期货交易日 等
	 * @param date 当前指定日期
	 * @param days 向后推几个工作日
	 * @param future true 未来日期| false 前几个工作日
	 * @return 计算后工作日
	 */
	LocalDate getSpecifiedWorkDay(Integer source, LocalDate date, int days, boolean future);

	/**
	 * 计算两个日期的工作日之差 不算头不算尾
	 * @param source 数据源 1标准工作日 2.证券交易日3.期货交易日 等
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return 相差天数
	 */
	int calculateDiffDays(Integer source, LocalDate startDate, LocalDate endDate);

}
