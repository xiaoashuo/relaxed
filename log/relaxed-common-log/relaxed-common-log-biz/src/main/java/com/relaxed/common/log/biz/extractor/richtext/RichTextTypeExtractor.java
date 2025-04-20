package com.relaxed.common.log.biz.extractor.richtext;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import com.relaxed.common.log.biz.annotation.LogDiffTag;
import com.relaxed.common.log.biz.extractor.DiffExtractor;
import difflib.Delta;
import difflib.DiffRow;
import difflib.DiffRowGenerator;
import difflib.DiffUtils;
import difflib.Patch;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 富文本类型差异提取器，用于处理富文本内容的差异比较。 该实现类通过比较富文本的旧值和新值，生成类似于 Git Diff 的差异结果。 主要功能包括： 1. 将 HTML
 * 内容转换为纯文本进行比较 2. 使用 DiffUtils 计算文本差异 3. 生成包含变更类型和内容的差异片段 4. 特殊处理图片标签
 *
 * @author Yakir
 * @since 1.0.0
 */
public class RichTextTypeExtractor implements DiffExtractor {

	/**
	 * 提取并转换富文本字段的差异值
	 * @param field 字段对象
	 * @param logDiffTag 差异标签注解
	 * @param oldFieldValue 字段的旧值
	 * @param newFieldValue 字段的新值
	 * @return 差异结果的 JSON 字符串表示
	 */
	@Override
	public String diffValue(Field field, LogDiffTag logDiffTag, Object oldFieldValue, Object newFieldValue) {
		String oldValueStr = StrUtil.toString(oldFieldValue);
		String newValueStr = StrUtil.toString(newFieldValue);
		String diffText = diffText(oldValueStr, newValueStr);
		return diffText;
	}

	/**
	 * 比较两个文本对象或 HTML 片段的不同，生成类似于 Git Diff 的差异结果
	 * @param oldText 旧文本内容
	 * @param newText 新文本内容
	 * @return 差异结果的 JSON 字符串表示，包含版本号和差异片段列表
	 */
	public static String diffText(String oldText, String newText) {
		// 将 HTML 转换为纯文本并按行分割
		List<String> oldStringList = Arrays.asList(Html2Text.simpleHtml(oldText).split("\n"));
		List<String> newStringList = Arrays.asList(Html2Text.simpleHtml(newText).split("\n"));

		// 计算文本差异
		Patch patch = DiffUtils.diff(oldStringList, newStringList);
		DiffRowGenerator.Builder builder = new DiffRowGenerator.Builder();
		builder.showInlineDiffs(false).columnWidth(Integer.MAX_VALUE);
		DiffRowGenerator generator = builder.build();

		// 存储差异行
		Map<Integer, List<DiffRow>> diffRowMap = new LinkedHashMap<>();

		// 处理每个差异块
		for (Object delta : patch.getDeltas()) {
			List<String> originalLines = (List<String>) ((Delta) delta).getOriginal().getLines();
			List<String> revisedLines = (List<String>) ((Delta) delta).getRevised().getLines();
			List<DiffRow> generateDiffRows = generator.generateDiffRows(originalLines, revisedLines);
			// 获取变化的位置
			int leftPos = ((Delta) delta).getOriginal().getPosition();
			int rightPos = ((Delta) delta).getRevised().getPosition();
			// 根据位置分类存储差异行
			for (DiffRow row : generateDiffRows) {
				List<DiffRow> diffRowList = diffRowMap.get(leftPos);
				if (diffRowList == null) {
					diffRowList = new ArrayList<DiffRow>();
				}
				diffRowList.add(row);
				diffRowMap.put(leftPos, diffRowList);
			}
		}

		// 构建差异结果
		Map<String, Object> textDiffMap = new HashMap<>();
		textDiffMap.put("version", "1.0.0");
		List<Fragment> diffFragmentList = new ArrayList<>();

		// 处理每个差异位置
		Set<Map.Entry<Integer, List<DiffRow>>> entrySet = diffRowMap.entrySet();
		for (Map.Entry<Integer, List<DiffRow>> entry : entrySet) {
			Integer pos = entry.getKey();
			List<DiffRow> diffRowList = entry.getValue();
			Fragment fragment = new Fragment(pos + 1);
			List<Part> partList = new ArrayList<>();
			for (DiffRow row : diffRowList) {
				DiffRow.Tag tag = row.getTag();
				if (tag == DiffRow.Tag.INSERT) {
					Part part = new Part(PartType.ADD, replaceImgTag(row.getNewLine()));
					partList.add(part);
				}
				else if (tag == DiffRow.Tag.CHANGE) {
					if (!row.getOldLine().trim().isEmpty()) {
						Part part = new Part(PartType.CHANGE_OLD, replaceImgTag(row.getOldLine()));
						partList.add(part);
					}
					if (!row.getNewLine().trim().isEmpty()) {
						Part part = new Part(PartType.CHANGE_NEW, replaceImgTag(row.getNewLine()));
						partList.add(part);
					}
				}
				else if (tag == DiffRow.Tag.DELETE) {
					Part part = new Part(PartType.DEL, replaceImgTag(row.getOldLine()));
					partList.add(part);
				}
			}
			fragment.setPartList(partList);
			diffFragmentList.add(fragment);
		}
		textDiffMap.put("content", diffFragmentList);
		return JSONUtil.toJsonStr(textDiffMap);
	}

	/**
	 * 替换图片标签占位符为实际的 HTML 标签
	 * @param s 包含图片占位符的字符串
	 * @return 替换后的字符串
	 */
	private static String replaceImgTag(String s) {
		return s.replaceAll(Constant.imgLeftPlaceholder, "<img ").replaceAll(Constant.imgRightPlaceholder, " >");
	}

}
