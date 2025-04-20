package com.relaxed.common.datascope.holder;

import com.relaxed.common.datascope.handler.DataPermissionRule;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 数据权限规则持有者
 * <p>
 * 使用栈结构存储调用链中的数据权限规则，支持方法嵌套调用时使用不同的数据权限控制。 与 @DataPermission 注解不同，DataPermissionRule
 * 是编程式数据权限控制的方式，优先级高于注解。
 */
public final class DataPermissionRuleHolder {

	private DataPermissionRuleHolder() {
	}

	/**
	 * 使用栈存储 DataPermissionRule，便于在方法嵌套调用时使用不同的数据权限控制。
	 */
	private static final ThreadLocal<Deque<DataPermissionRule>> DATA_PERMISSION_RULES = ThreadLocal
			.withInitial(ArrayDeque::new);

	/**
	 * 获取当前栈顶的数据权限规则
	 * <p>
	 * 返回当前线程中栈顶的数据权限规则，如果栈为空则返回 null。
	 * @return 当前的数据权限规则
	 */
	public static DataPermissionRule peek() {
		Deque<DataPermissionRule> deque = DATA_PERMISSION_RULES.get();
		return deque == null ? null : deque.peek();
	}

	/**
	 * 将数据权限规则压入栈顶
	 * <p>
	 * 将指定的数据权限规则压入当前线程的栈中。
	 * @param dataPermissionRule 要压入的数据权限规则
	 * @return 压入的数据权限规则
	 */
	public static DataPermissionRule push(DataPermissionRule dataPermissionRule) {
		Deque<DataPermissionRule> deque = DATA_PERMISSION_RULES.get();
		if (deque == null) {
			deque = new ArrayDeque<>();
		}
		deque.push(dataPermissionRule);
		return dataPermissionRule;
	}

	/**
	 * 弹出栈顶的数据权限规则
	 * <p>
	 * 移除并返回当前线程栈顶的数据权限规则。 如果栈为空，则清除 ThreadLocal。
	 */
	public static void poll() {
		Deque<DataPermissionRule> deque = DATA_PERMISSION_RULES.get();
		deque.poll();
		// 当没有元素时，清空 ThreadLocal
		if (deque.isEmpty()) {
			clear();
		}
	}

	/**
	 * 清除当前线程的数据权限规则
	 * <p>
	 * 移除当前线程中存储的所有数据权限规则。
	 */
	public static void clear() {
		DATA_PERMISSION_RULES.remove();
	}

}
