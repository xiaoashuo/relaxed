package com.relaxed.extend.mybatis.interceptor;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.extend.mybatis.annotation.MapResult;
import com.relaxed.extend.mybatis.decorator.ResultSetDecorator;
import com.relaxed.extend.mybatis.handler.MapResultHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic MapF2FInterceptor
 * @Description
 * @date 2021/10/21 10:58
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
@Intercepts(@Signature(method = "handleResultSets", type = ResultSetHandler.class, args = { Statement.class }))
public class MapResultInterceptor implements Interceptor {

	private final MapResultHandler mapResultHandler;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object target = invocation.getTarget();
		MetaObject metaObject = SystemMetaObject.forObject(target);
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");

		String methodId = mappedStatement.getId();
		String classname = StrUtil.subBefore(methodId, StrPool.DOT, true);
		String methodName = StrUtil.subAfter(methodId, StrPool.DOT, true);
		Class<?> callClazz = Class.forName(classname);
		Method method = ReflectUtil.getMethod(callClazz, methodName);
		if (method == null || method.getAnnotation(MapResult.class) == null) {
			return invocation.proceed();
		}
		MapResult mapResultAnnotation = method.getAnnotation(MapResult.class);
		Statement statement = (Statement) invocation.getArgs()[0];
		// 结果集 包装器
		ResultSetDecorator resultSetDecorator = new ResultSetDecorator(statement.getResultSet(),
				mappedStatement.getConfiguration());
		// 默认只使用单个结果集处理map结果 若多个需要更改此处List
		List<Map> list = Collections
				.singletonList(mapResultHandler.resultToMap(resultSetDecorator, method, mapResultAnnotation));
		return list;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

}
