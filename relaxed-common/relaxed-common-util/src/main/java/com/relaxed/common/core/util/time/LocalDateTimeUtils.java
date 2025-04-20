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
 * LocalDateTime 工具类，提供日期时间的解析、格式化和转换功能。 主要功能包括： 1. 字符串与 LocalDateTime 的相互转换 2. 时间戳与
 * LocalDateTime 的相互转换 3. 支持自定义日期时间格式 4. 支持时区转换
 *
 * @author Yakir
 * @since 1.0
 */
public class LocalDateTimeUtils {

	/**
	 * 默认时区偏移量，设置为东八区（UTC+8）
	 */
	public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+8");

	/**
	 * 默认时区ID，基于默认时区偏移量
	 */
	public static final ZoneId DEFAULT_ZONE_ID = DEFAULT_ZONE_OFFSET.normalized();

	/**
	 * 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss
	 */
	public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 将时间字符串解析为 LocalDateTime 对象 使用默认时间格式：yyyy-MM-dd HH:mm:ss
	 * @param dateTimeStr 时间字符串
	 * @return 解析后的 LocalDateTime 对象
	 */
	public static LocalDateTime parse(String dateTimeStr) {
		return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN));
	}

	/**
	 * 将时间字符串解析为 LocalDateTime 对象
	 * @param dateTimeStr 时间字符串
	 * @param pattern 时间格式模式
	 * @return 解析后的 LocalDateTime 对象
	 */
	public static LocalDateTime parse(String dateTimeStr, String pattern) {
		return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 将时间字符串解析为 LocalDateTime 对象
	 * @param dateTimeStr 时间字符串
	 * @param dateTimeFormatter 自定义的日期时间格式化器
	 * @return 解析后的 LocalDateTime 对象
	 */
	public static LocalDateTime parse(String dateTimeStr, DateTimeFormatter dateTimeFormatter) {
		return LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
	}

	/**
	 * 将毫秒级时间戳转换为 LocalDateTime 对象 使用默认时区（东八区）
	 * @param timestamp 毫秒级时间戳
	 * @return 转换后的 LocalDateTime 对象
	 */
	public static LocalDateTime parseMills(Long timestamp) {
		return parseMills(timestamp, DEFAULT_ZONE_ID);
	}

	/**
	 * 将毫秒级时间戳转换为 LocalDateTime 对象
	 * @param timestamp 毫秒级时间戳
	 * @param zoneId 时区ID
	 * @return 转换后的 LocalDateTime 对象
	 */
	public static LocalDateTime parseMills(Long timestamp, ZoneId zoneId) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zoneId);
	}

	/**
	 * 将 LocalDateTime 对象格式化为字符串 使用默认时间格式：yyyy-MM-dd HH:mm:ss
	 * @param datetime 要格式化的 LocalDateTime 对象
	 * @return 格式化后的时间字符串
	 */
	public static String format(LocalDateTime datetime) {
		return format(datetime, DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN));
	}

	/**
	 * 将 LocalDateTime 对象格式化为字符串
	 * @param datetime 要格式化的 LocalDateTime 对象
	 * @param pattern 时间格式模式
	 * @return 格式化后的时间字符串
	 */
	public static String format(LocalDateTime datetime, String pattern) {
		return format(datetime, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 将 LocalDateTime 对象格式化为字符串
	 * @param datetime 要格式化的 LocalDateTime 对象
	 * @param dateTimeFormatter 自定义的日期时间格式化器
	 * @return 格式化后的时间字符串
	 */
	public static String format(LocalDateTime datetime, DateTimeFormatter dateTimeFormatter) {
		return datetime.format(dateTimeFormatter);
	}

	/**
	 * 将 LocalDateTime 对象转换为毫秒级时间戳 使用默认时区（东八区）
	 * @param datetime 要转换的 LocalDateTime 对象
	 * @return 毫秒级时间戳
	 */
	public static Long toMillsTimestamp(LocalDateTime datetime) {
		return toMillsTimestamp(datetime, DEFAULT_ZONE_OFFSET);
	}

	/**
	 * 将 LocalDateTime 对象转换为毫秒级时间戳
	 * @param datetime 要转换的 LocalDateTime 对象
	 * @param offset 时区偏移量
	 * @return 毫秒级时间戳
	 */
	public static Long toMillsTimestamp(LocalDateTime datetime, ZoneOffset offset) {
		return datetime.toInstant(offset).toEpochMilli();
	}

}
