package com.relaxed.common.core.util.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 可序列化的函数式接口，扩展自 {@link Function} 接口。
 *
 * 该接口主要用于需要序列化 Lambda 表达式的场景，例如： - 在分布式系统中传输 Lambda 表达式 - 在需要持久化 Lambda 表达式的场景 -
 * 在反射获取方法引用信息的场景
 *
 * @param <T> 输入参数类型
 * @param <R> 返回值类型
 * @author Yakir
 * @since 1.0
 */
public interface SFunction<T, R> extends Function<T, R>, Serializable {

}
