package com.relaxed.common.core.util.time;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author Yakir
 * @Topic LocalDateTimeUtils
 * @Description
 * @date 2023/6/4 14:35
 * @Version 1.0
 */
public class LocalDateTimeUtils {

	/**
	 * 默认为东八区
	 */
	public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+8");

	public static final ZoneId DEFAULT_ZONE_ID = DEFAULT_ZONE_OFFSET.normalized();

	/**
	 * 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss
	 */
	public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 还原时间字符串 默认时间模式 yyyy-MM-dd HH:mm:ss
	 * @param dateTimeStr 时间字符串
	 * @return java.time.LocalDateTime
	 */
	public static LocalDateTime parse(String dateTimeStr) {
		return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN));
	}

	/**
	 * 还原时间字符串
	 * @param dateTimeStr 时间字符串
	 * @param pattern 时间模式
	 * @return java.time.LocalDateTime
	 */
	public static LocalDateTime parse(String dateTimeStr, String pattern) {
		return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 还原时间字符串
	 * @param dateTimeStr 时间字符串
	 * @param dateTimeFormatter 格式化器
	 * @return java.time.LocalDateTime
	 */
	public static LocalDateTime parse(String dateTimeStr, DateTimeFormatter dateTimeFormatter) {
		return LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
	}

	/**
	 * 还原时间戳(毫秒级) 默认时区
	 * @param timestamp
	 * @return java.time.LocalDateTime
	 */
	public static LocalDateTime parseMills(Long timestamp) {
		return parseMills(timestamp, DEFAULT_ZONE_ID);
	}

	/**
	 * 还原时间戳(毫秒级)
	 * @date 2023/6/4 15:43
	 * @param zoneId 区域id
	 * @return java.time.LocalDateTime
	 */
	public static LocalDateTime parseMills(Long timestamp, ZoneId zoneId) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zoneId);
	}

	/**
	 * 格式化时间为字符串 默认时间格式 yyyy-MM-dd HH:mm:ss
	 * @param datetime 时间
	 * @return java.lang.String
	 */
	public static String format(LocalDateTime datetime) {
		return format(datetime, DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN));
	}

	/**
	 * 格式化时间为字符串
	 * @param datetime 时间
	 * @param pattern 模式
	 * @return java.lang.String
	 */
	public static String format(LocalDateTime datetime, String pattern) {
		return format(datetime, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 格式化时间为字符串
	 * @param datetime 时间
	 * @param dateTimeFormatter 格式化器
	 * @return java.lang.String
	 */
	public static String format(LocalDateTime datetime, DateTimeFormatter dateTimeFormatter) {
		return datetime.format(dateTimeFormatter);
	}

	/**
	 * 转换为毫秒时间戳 默认时区
	 * @param datetime 时间
	 * @return java.lang.Long
	 */
	public static Long toMillsTimestamp(LocalDateTime datetime) {
		return toMillsTimestamp(datetime, DEFAULT_ZONE_OFFSET);
	}

	/**
	 * 转换为毫秒时间戳
	 * @param datetime 时间
	 * @param offset 时区偏移量
	 * @return java.lang.Long
	 */
	public static Long toMillsTimestamp(LocalDateTime datetime, ZoneOffset offset) {
		return datetime.toInstant(offset).toEpochMilli();
	}

}
