package com.relaxed.extend.mybatis.handler;

import com.relaxed.extend.mybatis.annotation.MapResult;
import com.relaxed.extend.mybatis.decorator.ResultSetDecorator;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Yakir
 * @Topic ResultMapHandler
 * @Description
 * @date 2021/10/22 17:56
 * @Version 1.0
 */
public interface MapResultHandler {

	/**
	 * 结果转为->MAP
	 * @author yakir
	 * @date 2021/10/22 17:57
	 * @param decorator
	 * @param method
	 * @param mapResultAnnotation
	 * @return java.util.Map
	 * @throws SQLException
	 */
	Map resultToMap(ResultSetDecorator decorator, Method method, MapResult mapResultAnnotation) throws SQLException;

}
