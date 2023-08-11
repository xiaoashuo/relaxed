package com.relaxed.common.datetime.holidays;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.thread.ThreadUtil;
import com.relaxed.common.datetime.holidays.storage.HolidayStorage;
import com.relaxed.common.datetime.holidays.storage.LocalHolidayStorage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yakir
 * @Topic WorkdayCalculatorTest
 * @Description
 * @date 2023/8/10 9:58
 * @Version 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
		classes = { HolidayAutoConfiguration.class, LocalHolidayLoader.class })
@Slf4j
class WorkdayCalculatorTest {

	@Autowired
	WorkdayCalculator workdayCalculator;

	@BeforeEach
	void init() {
		//节假日参考https://timor.tech/api/holiday/year/2023
		//https://www.gov.cn/zhengce/zhengceku/2022-12/08/content_5730844.htm
		//https://www.cnblogs.com/dzlishen/p/16365742.html
		// HolidayStorage storage=new LocalHolidayStorage();
		// storage.add("2023-05-05");
		// storage.add("2023-06-28");
		// storage.add("2023-07-01");
		// storage.add("2023-08-01");
		// storage.add("2023-08-05");
		// workdayCalculator=new DefaultWorkDayCalculator(storage);
	}

	@Test
	void isHoliday() {
		LocalDate date = LocalDate.parse("2023-06-30", DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
		boolean isHoliday = workdayCalculator.isHoliday(date);
		log.info("当前日期{},是否为节假日{}", date, isHoliday);
		ThreadUtil.sleep(5000);
		// 修改文件内工作日内容，再次进行判断
		date = LocalDate.parse("2024-01-03", DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
		isHoliday = workdayCalculator.isHoliday(date);
		log.info("当前日期{},是否为节假日{}", date, isHoliday);
		ThreadUtil.sleep(2000000);

	}

	@Test
	void getNextWorkday() {
		LocalDate date = LocalDate.parse("2023-06-30", DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
		LocalDate nextDate = workdayCalculator.getNextWorkday(date);
		log.info("当前日期{},下一个工作日{}", date, nextDate);
	}

	@Test
	void getPrevWorkday() {
		LocalDate date = LocalDate.parse("2023-06-23", DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
		LocalDate nextDate = workdayCalculator.getPrevWorkday(date);
		log.info("当前日期{},上一个工作日{}", date, nextDate);
	}

	@Test
	void getSpecifiedWorkDay() {
		int days = 2;
		LocalDate date = LocalDate.parse("2023-06-30", DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
		LocalDate nextDate = workdayCalculator.getSpecifiedWorkDay(date, days, false);
		log.info("当前日期{},下{}个工作日{}", date, days, nextDate);
	}

	@Test
	void calculateDays() {
		LocalDate startDate = LocalDate.parse("2023-06-30", DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
		LocalDate endDate = LocalDate.parse("2023-08-07", DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
		int diffDay = workdayCalculator.calculateDiffDays(startDate, endDate);
		log.info("开始日期{},结束日期{},相差天数{}", startDate, endDate, diffDay);
	}

}
