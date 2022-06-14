package com.relaxed.common.core.util.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author Yakir
 * @Topic SFunction
 * @Description 支持序列化的 Function
 * @date 2022/6/14 16:43
 * @Version 1.0
 */
public interface SFunction<T, R> extends Function<T, R>, Serializable {

}
