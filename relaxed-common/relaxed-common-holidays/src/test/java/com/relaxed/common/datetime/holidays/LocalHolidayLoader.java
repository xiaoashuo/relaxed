package com.relaxed.common.datetime.holidays;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.datetime.holidays.storage.HolidayStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * @author Yakir
 * @Topic HolidaysLoader
 * @Description 节假日加载器
 * @date 2023/8/10 9:53
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LocalHolidayLoader {

	@Value("${holiday.localPath}")
	private String localPath;

	private final HolidayStorage holidayStorage;

	private static WatchMonitor eventModify;

	@PostConstruct
	void init() {
		Assert.hasText(localPath, "文件路径不存在");
		File file = FileUtil.file(localPath);
		String result = FileUtil.readString(file, Charset.forName("UTF-8"));
		if (StrUtil.isNotEmpty(result)) {
			List<String> dates = StrUtil.split(result, ",");
			holidayStorage.addAll(dates);
		}
		eventModify = WatchMonitor.createAll(file, new SimpleWatcher() {
			@Override
			public void onModify(WatchEvent<?> event, Path currentPath) {
				Console.log("当前时间:" + LocalDateTime.now() + " 文件已修改");
				String result = FileUtil.readString(file, Charset.forName("UTF-8"));
				if (StrUtil.isNotEmpty(result)) {
					// 此处仅支持新增,若删除或修改，则需要比较修改后与修改前的差值,且将修改后全量加入，在删除差值数据，不允许直接clear在添加
					// 会导致瞬时数据为空
					Collection<String> list = holidayStorage.list();
					List<String> dates = StrUtil.split(result, ",");
					Collection<String> colList = CollUtil.subtract(list, dates);
					holidayStorage.delAll(colList);
					holidayStorage.addAll(dates);
				}
			}
		});
		eventModify.start();
		log.info("节假日数据监听器开启");
	}

	@PreDestroy
	void preDestroy() {
		eventModify.close();
		log.info("节假日数据监听器关闭");
	}

	public static void main(String[] args) {

		File file = new File("D:\\idea\\source\\company\\doc\\滴滴abc\\holidays.txt");
		String result = FileUtil.readString(file, Charset.forName("UTF-8"));
		System.out.println(result);
		List<String> dates = StrUtil.split(result, ",");
		System.out.println(dates);
		WatchMonitor eventModify = WatchMonitor.createAll(file, new SimpleWatcher() {
			@Override
			public void onModify(WatchEvent<?> event, Path currentPath) {
				Console.log("EVENT modify");
			}
		});
		eventModify.start();
		try {
			Thread.sleep(1000);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		eventModify.close();
		System.out.println("1203");
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

}
