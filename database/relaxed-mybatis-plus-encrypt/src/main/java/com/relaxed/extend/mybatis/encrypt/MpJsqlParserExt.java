package com.relaxed.extend.mybatis.encrypt;

import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.stream.Collectors;

/**
 * @author Yakir
 * @Topic MpJsqlParserExt
 * @Description
 * @date 2024/10/12 17:51
 * @Version 1.0
 */
@Slf4j
public class MpJsqlParserExt {

	public static void main(String[] args) {

		String sqlWhere = " (c_list_id = #{ew.paramNameValuePairs.MPGENVAL1} AND c_custtype LIKE #{ew.paramNameValuePairs.MPGENVAL2} "
				+ "OR c_isdelete = #{ew.paramNameValuePairs.MPGENVAL3} AND created_time > #{ew.paramNameValuePairs.MPGENVAL4} "
				+ "OR (c_custtype > #{ew.paramNameValuePairs.MPGENVAL5} AND c_identitytype = #{ew.paramNameValuePairs.MPGENVAL6}) "
				+ "AND id BETWEEN #{ew.paramNameValuePairs.MPGENVAL7} AND #{ew.paramNameValuePairs.MPGENVAL8} "
				+ "AND md5 IN (#{ew.paramNameValuePairs.MPGENVAL9}, #{ew.paramNameValuePairs.MPGENVAL10}) "
				+ "AND id >= #{ew.paramNameValuePairs.MPGENVAL11} AND id NOT IN (#{ew.paramNameValuePairs.MPGENVAL12}))";

		MultiValueMap<String, String> multiValueMap = parseSql(sqlWhere);
		Integer mpTotalCount = multiValueMap.values().stream().collect(Collectors.summingInt(e -> e.size()));
		log.info("当前MPGENVAL总数:{}", mpTotalCount);
		log.info("当前MPGENVAL缓存:{}", multiValueMap);

	}

	@SneakyThrows
	public static MultiValueMap<String, String> parseSql(String sqlWhere) {
		MultiValueMap<String, String> multiMap = new LinkedMultiValueMap<>();
		sqlWhere = sqlWhere.replace("#{ew.paramNameValuePairs.", ":").replace("}", "");
		Expression expression = CCJSqlParserUtil.parseCondExpression(sqlWhere);
		sqlHandle(multiMap, expression);
		return multiMap;
	}

	private static void sqlHandle(MultiValueMap<String, String> param, Expression expression) {

		if (expression instanceof Parenthesis) {
			Parenthesis parenthesis = (Parenthesis) expression;
			sqlHandle(param, parenthesis.getExpression());
		}
		else if (expression instanceof OrExpression) {
			OrExpression orExpression = (OrExpression) expression;
			sqlHandle(param, orExpression.getLeftExpression());
			sqlHandle(param, orExpression.getRightExpression());

		}
		else if (expression instanceof AndExpression) {
			AndExpression andExpression = (AndExpression) expression;
			sqlHandle(param, andExpression.getLeftExpression());
			sqlHandle(param, andExpression.getRightExpression());
		}
		else if (expression instanceof EqualsTo) {
			EqualsTo equalsTo = (EqualsTo) expression;
			Expression leftExpression = equalsTo.getLeftExpression();
			String colName = leftExpression.toString();
			String mapName = equalsTo.getRightExpression().toString();
			param.add(colName, removePrefix(mapName));
			// param.put(removePrefix(mapName),colName);
		}
		else if (expression instanceof LikeExpression) {
			LikeExpression likeExpression = (LikeExpression) expression;
			Expression leftExpression = likeExpression.getLeftExpression();
			String colName = leftExpression.toString();
			String mapName = likeExpression.getRightExpression().toString();
			// param.put(removePrefix(mapName),colName);
			param.add(colName, removePrefix(mapName));

		}
		else if (expression instanceof GreaterThan) {
			GreaterThan greaterThan = (GreaterThan) expression;
			Expression leftExpression = greaterThan.getLeftExpression();
			String colName = leftExpression.toString();
			String mapName = greaterThan.getRightExpression().toString();
			param.add(colName, removePrefix(mapName));

			// param.put(removePrefix(mapName),colName);
		}
		else if (expression instanceof GreaterThanEquals) {
			GreaterThanEquals greaterThanEquals = (GreaterThanEquals) expression;
			Expression leftExpression = greaterThanEquals.getLeftExpression();
			String colName = leftExpression.toString();
			String mapName = greaterThanEquals.getRightExpression().toString();
			// param.put(removePrefix(mapName),colName);
			param.add(colName, removePrefix(mapName));

		}
		else if (expression instanceof MinorThan) {
			MinorThan minorThan = (MinorThan) expression;
			Expression leftExpression = minorThan.getLeftExpression();
			String colName = leftExpression.toString();
			String mapName = minorThan.getRightExpression().toString();
			param.add(colName, removePrefix(mapName));

			// param.put(removePrefix(mapName),colName);
		}
		else if (expression instanceof MinorThanEquals) {
			MinorThanEquals minorThanEquals = (MinorThanEquals) expression;
			Expression leftExpression = minorThanEquals.getLeftExpression();
			String colName = leftExpression.toString();
			String mapName = minorThanEquals.getRightExpression().toString();
			// param.put(removePrefix(mapName),colName);
			param.add(colName, removePrefix(mapName));

		}
		else if (expression instanceof InExpression) {
			InExpression inExpression = (InExpression) expression;
			// 是否为notin 根据一个not属性判断
			Expression leftExpression = inExpression.getLeftExpression();
			String colName = leftExpression.toString();
			ExpressionList rightItemsList = (ExpressionList) inExpression.getRightItemsList();
			for (Expression rightItemsListExpression : rightItemsList.getExpressions()) {
				String mapName = rightItemsListExpression.toString();
				param.add(colName, removePrefix(mapName));
				// param.put(removePrefix(mapName),colName);
			}
		}
		else if (expression instanceof Between) {
			Between between = (Between) expression;
			Expression leftExpression = between.getLeftExpression();
			String colName = leftExpression.toString();
			Expression colStart = between.getBetweenExpressionStart();
			Expression colEnd = between.getBetweenExpressionEnd();
			// param.put(removePrefix(colStart.toString()),colName);
			// param.put(removePrefix(colEnd.toString()),colName);
			param.add(colName, removePrefix(colStart.toString()));
			param.add(colName, removePrefix(colEnd.toString()));
		}

	}

	public static String removePrefix(String val) {
		return StrUtil.removePrefix(val, ":");
	}

}
