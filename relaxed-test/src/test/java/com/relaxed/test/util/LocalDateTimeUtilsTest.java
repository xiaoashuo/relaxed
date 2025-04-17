package com.relaxed.test.util;

import com.relaxed.common.core.util.time.LocalDateTimeUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author Yakir
 * @Topic LocalDateTimeUtilsTest
 * @Description
 * @date 2023/6/4 15:43
 * @Version 1.0
 */
public class LocalDateTimeUtilsTest {

	public static void main(String[] args) {
		LocalDateTime now = LocalDateTime.parse("2016-12-01 12:13:30",
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		LocalDateTime oneYearLater = now.plusYears(1);
		String formatDateTime = LocalDateTimeUtils.format(now, "yyyy-MM-dd HH:mm:ss");
		System.out.println("格式化时间:" + formatDateTime);
		LocalDateTime parseDateTime = LocalDateTimeUtils.parse(formatDateTime, "yyyy-MM-dd HH:mm:ss");
		System.out.println("还原后时间:" + parseDateTime);
		Long millsTimestamp = LocalDateTimeUtils.toMillsTimestamp(now);
		System.out.println("毫秒级时间戳:" + millsTimestamp);
		LocalDateTime millsStampRecover = LocalDateTimeUtils.parseMills(millsTimestamp);
		System.out.println("恢复毫秒级时间戳后时间:" + millsStampRecover);
		long betweenDay = ChronoUnit.DAYS.between(now, oneYearLater);
		System.out.println("ChronoUnit 相差天数" + betweenDay);
		Duration duration = Duration.between(now, oneYearLater);
		System.out.println("duration相差天数:" + duration.toDays());
		// Period.between只能算月份（年）之内的数据
		// 比如：
		// 计算2022-04-16距离2022-02-17间隔的天数，那么用Period.between来计算getDays就是只有一天
		// 计算2021-06-22距离2022-07-22间隔的月份，那么用Period.between来计算getMonths就是只有一个月
		Period period = Period.between(now.toLocalDate(), oneYearLater.toLocalDate());
		System.out.println("period相差天数:" + period.getDays());

		LocalDateTime t1 = LocalDateTime.now();
		LocalDateTime t2 = LocalDateTime.now().minusMonths(3);
		long dateTimeDiff = ChronoUnit.MONTHS.between(t2, t1);
		System.out.println("diff dateTime :" + dateTimeDiff); // diff dateTime : 2

	}

}
