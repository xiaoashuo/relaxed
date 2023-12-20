package com.relaxed.common.log.biz.model;

import com.relaxed.common.log.biz.discover.LogRecordFuncDiscover;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.MethodExecutor;
import org.springframework.expression.TypedValue;

import java.lang.reflect.Method;

/**
 * @author Yakir
 * @Topic FuncMeta
 * @Description
 * @date 2023/12/15 10:01
 * @Version 1.0
 */
@Data
public class FuncMeta {

	/**
	 * 注册方法名
	 */
	private String regFuncName;

	/**
	 * 是否静态方法
	 */
	private boolean isStatic;

	/**
	 * 目标对象
	 */
	private Object target;

	/**
	 * 目标方法
	 */
	private Method method;

	/**
	 * 执行时机
	 */
	private String around;

	private MethodExecutor methodExecutor;

	private Class<?>[] parameterTypes;

	public FuncMeta(String regFuncName, boolean isStatic, Object target, Method method, String around) {
		this.regFuncName = regFuncName;
		this.isStatic = isStatic;
		this.target = target;
		this.method = method;
		this.around = around;
		this.parameterTypes = method.getParameterTypes();
		this.methodExecutor = initMethodExecutor(this);

	}

	public MethodExecutor getMethodExecutor() {
		return methodExecutor;
	}

	private MethodExecutor initMethodExecutor(FuncMeta funcMeta) {
		return new MyMethodExecutor(funcMeta);
	}

	/**
	 * 自定义 MethodExecutor
	 */
	@RequiredArgsConstructor
	class MyMethodExecutor implements MethodExecutor {

		public final FuncMeta funcMeta;

		@Override
		public TypedValue execute(EvaluationContext context, Object rootObj, Object... arguments)
				throws AccessException {
			// 在这里编写方法的实际逻辑
			if (arguments != null && arguments.length > 0) {
				String orgMethodName = funcMeta.getRegFuncName();
				try {
					Object result = LogRecordFuncDiscover.invokeFunc(orgMethodName, arguments);
					return new TypedValue(result);
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			throw new AccessException("Invalid arguments for myMethod");
		}

	}

}
