package com.relaxed.common.log.biz.context;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.relaxed.common.log.biz.constant.LogRecordConstants;
import com.relaxed.common.log.biz.model.DiffMeta;
import org.springframework.core.NamedThreadLocal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志记录上下文，用于在方法执行过程中存储和管理日志相关的上下文信息。 使用 ThreadLocal 和 Deque 实现了一个栈结构，支持多层级的方法调用。 主要功能： 1.
 * 存储方法执行过程中的临时变量 2. 管理差异比对对象 3. 支持嵌套方法调用的上下文隔离
 *
 * @author Yakir
 * @since 1.0.0
 */
public class LogRecordContext {

	/**
	 * 使用 ThreadLocal 存储上下文栈，每个线程独立维护自己的上下文
	 */
	private static final ThreadLocal<Deque<Map<String, Object>>> variableMapStack = new NamedThreadLocal<Deque<Map<String, Object>>>(
			"log-operator") {
		@Override
		protected Deque<Map<String, Object>> initialValue() {
			return new ArrayDeque<>();
		}
	};

	/**
	 * 获取当前栈顶的上下文信息
	 * @return 当前栈顶的上下文 Map
	 */
	public static Map<String, Object> peek() {
		return variableMapStack.get().peek();
	}

	/**
	 * 压入一个新的空上下文到栈顶 用于开始一个新的方法调用层级
	 */
	public static void putEmptySpan() {
		Deque<Map<String, Object>> deque = variableMapStack.get();
		deque.push(new HashMap<>(8));
	}

	/**
	 * 向当前栈顶的上下文中添加键值对
	 * @param key 键
	 * @param val 值
	 */
	public static void push(String key, Object val) {
		Map<String, Object> peek = peek();
		peek.put(key, val);
	}

	/**
	 * 弹出并移除栈顶的上下文 如果栈为空，则清理 ThreadLocal
	 */
	public static void poll() {
		Deque<Map<String, Object>> deque = variableMapStack.get();
		deque.poll();
		if (deque.isEmpty()) {
			variableMapStack.remove();
		}
	}

	/**
	 * 添加需要差异比对的对象
	 * @param diffKey 差异标识
	 * @param source 源对象
	 * @param target 目标对象
	 */
	public static void putDiff(String diffKey, Object source, Object target) {
		Map<String, Object> peek = peek();
		List<DiffMeta> diffMetaList = (List<DiffMeta>) peek.get(LogRecordConstants.DIFF_KEY);
		if (CollectionUtil.isEmpty(diffMetaList)) {
			diffMetaList = new ArrayList<>();
			peek.put(LogRecordConstants.DIFF_KEY, diffMetaList);
		}
		diffMetaList.add(new DiffMeta(diffKey, source, target));
	}

	/**
	 * 添加需要差异比对的对象，使用源对象的类名作为差异标识
	 * @param source 源对象
	 * @param target 目标对象
	 */
	public static void putDiff(Object source, Object target) {
		putDiff(source.getClass().getSimpleName(), source, target);
	}

	/**
	 * 清理当前线程的所有上下文信息
	 */
	public static void clear() {
		variableMapStack.remove();
	}

	/**
	 * 测试方法，用于演示上下文栈的使用
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		LogRecordContext.putEmptySpan();
		LogRecordContext.push("user1", "a");
		LogRecordContext.push("user1-1", "a");
		System.out.println(LogRecordContext.peek());
		LogRecordContext.putEmptySpan();
		LogRecordContext.push("user2", "b");
		System.out.println(LogRecordContext.peek());

		// 创建一个使用 LinkedList 实现的 Deque
		ThreadLocal<Deque<Map<String, Object>>> variableMapStack = new NamedThreadLocal<Deque<Map<String, Object>>>(
				"xx") {
			@Override
			protected Deque<Map<String, Object>> initialValue() {
				return new ArrayDeque<>();
			}
		};
		Map<String, Object> map1 = MapUtil.newHashMap();
		variableMapStack.get().push(map1);
		Map<String, Object> peekMap1 = variableMapStack.get().peek();
		peekMap1.put("user1", "a");
		peekMap1.put("user1-1", "a");
		Map<String, Object> map2 = MapUtil.newHashMap();
		variableMapStack.get().push(map2);
		Map<String, Object> peekMap2 = variableMapStack.get().peek();
		peekMap2.put("user2", "b");
		peekMap2.put("user2-2", "b");
		Map<String, Object> map3 = MapUtil.newHashMap();
		variableMapStack.get().push(map3);
		Map<String, Object> peekMap3 = variableMapStack.get().peek();
		peekMap3.put("user3", "c");

		// 使用 peek 查看栈顶元素，但不弹出
		Map<String, Object> peekedMap = variableMapStack.get().peek();
		System.out.println("Peeked Map: " + peekedMap);
		variableMapStack.get().poll();
		Map<String, Object> peekedMap2 = variableMapStack.get().peek();
		System.out.println("Peeked Map: " + peekedMap2);
	}

}
