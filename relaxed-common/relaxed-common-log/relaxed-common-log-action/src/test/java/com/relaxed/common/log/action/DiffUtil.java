// package com.relaxed.common.log.action;
//
// import cn.hutool.core.util.StrUtil;
// import cn.hutool.json.JSONUtil;
// import com.google.common.collect.MapDifference;
// import com.google.common.collect.Maps;
// import com.relaxed.common.log.action.enums.AttrOptionEnum;
// import com.relaxed.common.log.action.model.AttributeChange;
//
// import com.relaxed.common.log.action.utils.FlatMapUtil;
// import org.springframework.util.StringUtils;
//
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
//
/// **
// * @author Yakir
// * @Topic DiffUtil
// * @Description
// * @date 2021/12/17 14:25
// * @Version 1.0
// */
//
// public class DiffUtil {
//
// public static MapDifference<String, Object> difference(Map<String, Object> source,
// Map<String, Object> target) {
// // 铺平数据源
// Map<String, Object> flattenSource = FlatMapUtil.flatten(source);
// Map<String, Object> flattenTarget = FlatMapUtil.flatten(target);
// // 比对数据差异
// MapDifference<String, Object> difference = Maps.difference(flattenSource,
// flattenTarget);
// return difference;
// }
//
// public static List<AttributeChange> diffJson(String source, String target) {
// Map sourceMap = JSONUtil.toBean(source, Map.class);
// Map targetMap = JSONUtil.toBean(target, Map.class);
//
// MapDifference difference = difference(sourceMap, targetMap);
// // 是否有差异，返回boolean
// boolean areEqual = difference.areEqual();
// if (areEqual) {
// return new ArrayList<>();
// }
// // 建只存在于左边映射项 即为删除操作
// Map<String, Object> onlyLeftMap = difference.entriesOnlyOnLeft();
// // 建只存在于右边
// Map<String, Object> onlyRightMap = difference.entriesOnlyOnRight();
// // 两个map的交集
// // Map<String, Object> entriesInCommon = difference.entriesInCommon();
// // 键相同但是值不同值映射项。返回的Map的值类型为MapDifference.ValueDifference，以表示左右两个不同的值
// Map<String, MapDifference.ValueDifference<Object>> entriesDiffering =
// difference.entriesDiffering();
// List<AttributeChange> attributeChangeList = new ArrayList<>();
// // 处理添加
// for (Map.Entry<String, Object> entry : onlyRightMap.entrySet()) {
// String path = entry.getKey();
// Object value = entry.getValue();
// AttributeChange attributeChange = buildAttributeChange(path, AttrOptionEnum.ADD, "",
// StrUtil.toString(value));
// attributeChangeList.add(attributeChange);
// }
// // 处理删除
// for (Map.Entry<String, Object> entry : onlyLeftMap.entrySet()) {
// String path = entry.getKey();
// Object value = entry.getValue();
//
// AttributeChange attributeChange = buildAttributeChange(path, AttrOptionEnum.REMOVE,
// StrUtil.toString(value),
// "");
// attributeChangeList.add(attributeChange);
// }
//
// for (Map.Entry<String, MapDifference.ValueDifference<Object>> entry :
// entriesDiffering.entrySet()) {
// String path = entry.getKey();
// MapDifference.ValueDifference<Object> entryValue = entry.getValue();
// Object leftVal = entryValue.leftValue();
// Object rightVal = entryValue.rightValue();
//
// AttributeChange attributeChange = buildAttributeChange(path, AttrOptionEnum.REPLACE,
// StrUtil.toString(leftVal), StrUtil.toString(rightVal));
// attributeChangeList.add(attributeChange);
// }
// return attributeChangeList;
// }
//
// private static AttributeChange buildAttributeChange(String path, AttrOptionEnum remove,
// String leftValue,
// String rightValue) {
// AttributeChange attributeChange = new AttributeChange();
// attributeChange.setOp(remove.toString());
// String property = "";
// if (StringUtils.hasText(path)) {
// String[] pathArray = path.split("/");
// property = pathArray[pathArray.length - 1];
// }
// attributeChange.setProperty(property);
// attributeChange.setPath(path);
// attributeChange.setLeftValue(leftValue);
// attributeChange.setRightValue(rightValue);
// return attributeChange;
// }
//
// }
