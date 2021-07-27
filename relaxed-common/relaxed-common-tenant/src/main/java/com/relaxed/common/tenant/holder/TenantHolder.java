package com.relaxed.common.tenant.holder;

import com.relaxed.common.tenant.handler.Tenant;
import com.relaxed.common.tenant.handler.table.DataScope;
import org.springframework.core.NamedThreadLocal;

import java.util.List;

/**
 * Tenant 持有者。 方便解析 SQL 时的参数透传
 *
 * @author yakir
 * @date 2021/7/27 10:49
 */
public final class TenantHolder {

	private TenantHolder() {
	}

	private static final ThreadLocal<Tenant> TENANT_HOLDER = new NamedThreadLocal<>(TenantHolder.class.getName());

	/**
	 * get tenant
	 * @return tenant
	 */
	public static Tenant get() {
		return TENANT_HOLDER.get();
	}

	/**
	 * 添加 tenant
	 */
	public static void set(Tenant tenant) {
		TENANT_HOLDER.set(tenant);
	}

	/**
	 * 删除 dataScope
	 */
	public static void remove() {
		TENANT_HOLDER.remove();
	}

}
