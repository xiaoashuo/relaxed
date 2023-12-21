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
 * @author Yakir
 * @Topic LogOperatorContext
 * @Description
 * @date 2023/12/14 11:26
 * @Version 1.0
 */
public class LogRecordContext {

	private static final ThreadLocal<Deque<Map<String, Object>>> variableMapStack = new NamedThreadLocal<Deque<Map<String, Object>>>(
			"log-operator") {
		@Override
		protected Deque<Map<String, Object>> initialValue() {
			return new ArrayDeque<>();
		}
	};

	public static Map<String, Object> peek() {
		return variableMapStack.get().peek();
	}

	public static void putEmptySpan() {
		Deque<Map<String, Object>> deque = variableMapStack.get();
		deque.push(new HashMap<>(8));
	}

	public static void push(String key, Object val) {
		Map<String, Object> peek = peek();
		peek.put(key, val);
	}

	public static void poll() {
		Deque<Map<String, Object>> deque = variableMapStack.get();
		deque.poll();
		if (deque.isEmpty()) {
			variableMapStack.remove();
		}

	}

	/**
	 * 放入需要差异比对对象
	 * @param diffKey
	 * @param source
	 * @param target
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
	 * @param source
	 * @param target
	 */
	public static void putDiff(Object source, Object target) {
		putDiff(source.getClass().getSimpleName(), source, target);
	}

	public static void clear() {
		variableMapStack.remove();
	}

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
