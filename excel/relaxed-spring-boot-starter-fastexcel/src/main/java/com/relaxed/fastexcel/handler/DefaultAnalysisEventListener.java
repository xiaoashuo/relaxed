package com.relaxed.fastexcel.handler;

import cn.idev.excel.context.AnalysisContext;
import com.relaxed.fastexcel.kit.Validators;
import com.relaxed.fastexcel.vo.ErrorMessage;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认Excel解析事件监听器 用于处理Excel数据解析过程中的事件 主要功能: 1. 支持数据校验(JSR-303) 2. 支持错误信息收集 3. 支持行号记录 4.
 * 支持有效数据收集
 *
 * @author lengleng
 * @author L.cm
 * @since 1.0.0
 */
@Slf4j
public class DefaultAnalysisEventListener extends ListAnalysisEventListener<Object> {

	/**
	 * 存储解析成功的数据对象列表
	 */
	private final List<Object> list = new ArrayList<>();

	/**
	 * 存储数据校验错误信息列表
	 */
	private final List<ErrorMessage> errorMessageList = new ArrayList<>();

	/**
	 * 当前处理的行号(从1开始)
	 */
	private Long lineNum = 1L;

	/**
	 * 处理每一行解析的结果 处理流程: 1. 更新行号 2. 执行数据校验 3. 处理校验结果 - 校验失败: 收集错误信息 - 校验成功: 添加到有效数据列表
	 * @param o 解析的数据对象
	 * @param analysisContext 解析上下文
	 */
	@Override
	public void invoke(Object o, AnalysisContext analysisContext) {
		lineNum++;

		Set<ConstraintViolation<Object>> violations = Validators.validate(o);
		if (!violations.isEmpty()) {
			Set<String> messageSet = violations.stream().map(ConstraintViolation::getMessage)
					.collect(Collectors.toSet());
			errorMessageList.add(new ErrorMessage(lineNum, messageSet));
		}
		else {
			list.add(o);
		}
	}

	/**
	 * 所有数据解析完成后的回调 用于处理解析完成后的清理工作
	 * @param analysisContext 解析上下文
	 */
	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		log.debug("Excel read analysed");
	}

	/**
	 * 获取所有解析成功的数据对象
	 * @return 数据对象列表
	 */
	@Override
	public List<Object> getList() {
		return list;
	}

	/**
	 * 获取所有校验失败的错误信息
	 * @return 错误信息列表
	 */
	@Override
	public List<ErrorMessage> getErrors() {
		return errorMessageList;
	}

}
