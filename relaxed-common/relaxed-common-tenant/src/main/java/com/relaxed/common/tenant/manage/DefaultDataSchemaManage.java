package com.relaxed.common.tenant.manage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Yakir
 * @Topic DefaultDataSchemaManage
 * @Description
 * @date 2021/7/26 16:53
 * @Version 1.0
 */
public class DefaultDataSchemaManage implements DataSchemaManage {

	List<String> schemas = new ArrayList<String>() {
		{
			set(0, "db1");
			set(1, "db2");
		}
	};

	@Override
	public List<String> getAllSchemas() {
		return schemas;
	}

	@Override
	public List<String> getIgnoreSchemas() {
		return null;
	}

	@Override
	public boolean ignore(String schemaName) {
		if (schemaName == null || "".equals(schemaName)) {
			return true;
		}
		return getIgnoreSchemas().contains(schemaName);
	}

	@Override
	public boolean ignoreMethod(String mapperId) {
		return false;
	}

	@Override
	public boolean validSchema(String schemaName) {
		return getAllSchemas().contains(schemaName);
	}

}
