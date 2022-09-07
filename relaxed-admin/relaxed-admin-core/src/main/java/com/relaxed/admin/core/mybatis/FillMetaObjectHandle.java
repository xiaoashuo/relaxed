package com.relaxed.admin.core.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/7/26 14:41
 */
@Slf4j
public class FillMetaObjectHandle implements MetaObjectHandler {

	private final static String CREATE_TIME = "createTime";

	private final static String CREATED_TIME = "createdTime";

	private final static String UPDATE_TIME = "updateTime";

	private final static String UPDATED_TIME = "updatedTime";

	@Override
	public void insertFill(MetaObject metaObject) {
		// 创建时间
		this.strictInsertFill(metaObject, CREATE_TIME, LocalDateTime.class, LocalDateTime.now());
		this.strictInsertFill(metaObject, CREATED_TIME, LocalDateTime.class, LocalDateTime.now());
		this.strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
		this.strictInsertFill(metaObject, UPDATED_TIME, LocalDateTime.class, LocalDateTime.now());
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// 修改时间
		this.strictUpdateFill(metaObject, UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
		this.strictUpdateFill(metaObject, UPDATED_TIME, LocalDateTime.class, LocalDateTime.now());
	}

}
