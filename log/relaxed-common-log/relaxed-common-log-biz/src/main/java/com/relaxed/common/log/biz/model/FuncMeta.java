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
 * 函数元数据类，用于封装日志记录中使用的自定义函数信息 该类提供了函数注册、执行和参数处理的功能，支持静态和非静态方法的调用
 *
 * @author Yakir
 */
@Data
public class FuncMeta {

	/**
	 * 注册的函数名称，用于在日志表达式中引用该函数
	 */
	private String regFuncName;

	/**
	 * 标识函数是否为静态方法 true 表示静态方法，false 表示实例方法
	 */
	private boolean isStatic;

	/**
	 * 目标对象，函数所属的实例对象 对于静态方法，此字段可能为 null
	 */
	private Object target;

	/**
	 * 目标方法，表示要执行的 Java 方法
	 */
	private Method method;

	/**
	 * 函数执行时机，用于指定函数在日志记录过程中的调用时机 例如：before（执行前）、after（执行后）等
	 */
	private String around;

	/**
	 * 方法执行器，用于实际执行注册的函数
	 */
	private MethodExecutor methodExecutor;

	/**
	 * 方法参数类型数组，用于方法调用时的参数类型匹配
	 */
	private Class<?>[] parameterTypes;

	/**
	 * 构造函数，初始化函数元数据
	 * @param regFuncName 注册的函数名称
	 * @param isStatic 是否为静态方法
	 * @param target 目标对象
	 * @param method 目标方法
	 * @param around 执行时机
	 */
	public FuncMeta(String regFuncName, boolean isStatic, Object target, Method method, String around) {
		this.regFuncName = regFuncName;
		this.isStatic = isStatic;
		this.target = target;
		this.method = method;
		this.around = around;
		this.parameterTypes = method.getParameterTypes();
		this.methodExecutor = initMethodExecutor(this);
	}

	/**
	 * 获取方法执行器
	 * @return MethodExecutor 实例
	 */
	public MethodExecutor getMethodExecutor() {
		return methodExecutor;
	}

	/**
	 * 初始化方法执行器
	 * @param funcMeta 函数元数据
	 * @return 初始化后的方法执行器
	 */
	private MethodExecutor initMethodExecutor(FuncMeta funcMeta) {
		return new MyMethodExecutor(funcMeta);
	}

	/**
	 * 自定义方法执行器实现类 负责实际执行注册的函数，并处理执行结果
	 */
	@RequiredArgsConstructor
	class MyMethodExecutor implements MethodExecutor {

		/**
		 * 函数元数据引用
		 */
		public final FuncMeta funcMeta;

		/**
		 * 执行方法
		 * @param context 评估上下文
		 * @param rootObj 根对象
		 * @param arguments 方法参数
		 * @return 方法执行结果
		 * @throws AccessException 当访问方法失败时抛出
		 */
		@Override
		public TypedValue execute(EvaluationContext context, Object rootObj, Object... arguments)
				throws AccessException {
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
