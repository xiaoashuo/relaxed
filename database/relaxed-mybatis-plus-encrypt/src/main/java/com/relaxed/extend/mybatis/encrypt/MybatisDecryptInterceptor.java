package com.relaxed.extend.mybatis.encrypt;

import cn.hutool.core.util.ClassUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.Collection;

/**
 * MyBatis 字段解密拦截器 用于拦截查询结果集，对标记了加密注解的字段进行解密处理 支持单个对象和集合类型的解密
 *
 * @author Yakir
 */
@RequiredArgsConstructor
@Slf4j
@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = Statement.class) })
@Component
public class MybatisDecryptInterceptor implements Interceptor {

	/**
	 * 字段加密助手，用于处理字段解密逻辑
	 */
	private final FieldEncryptHelper fieldEncryptHelper;

	/**
	 * 拦截结果集处理过程，对需要解密的字段进行解密处理
	 * @param invocation 拦截器调用信息
	 * @return 解密后的结果
	 * @throws Throwable 处理过程中可能抛出的异常
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object result = invocation.proceed();

		// 解密处理
		// 经过测试发现，无论是返回单个对象还是集合，result都是ArrayList类型
		if (ClassUtil.isAssignable(Collection.class, result.getClass())) {
			fieldEncryptHelper.decrypt((Collection) result);
		}
		else {
			fieldEncryptHelper.decrypt(result);
		}

		return result;
	}

}
