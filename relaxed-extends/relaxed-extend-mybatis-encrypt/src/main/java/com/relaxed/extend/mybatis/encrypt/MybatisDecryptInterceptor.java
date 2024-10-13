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
 * @author Yakir
 * @Topic DecryptInterceptor
 * @Description
 * @date 2024/10/9 17:12
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = Statement.class) })
@Component
public class MybatisDecryptInterceptor implements Interceptor {

	private final FieldEncryptHelper fieldEncryptHelper;

	@SuppressWarnings("rawtypes")
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// MetaObject metaObject = SystemMetaObject.forObject(invocation.getTarget());
		// MappedStatement mappedStatement = (MappedStatement)
		// metaObject.getValue("mappedStatement");
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
