package com.relaxed.autoconfigure.web.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充处理器 用于自动填充实体类中的创建时间和更新时间字段 支持多种字段命名方式：createTime/createdTime 和
 * updateTime/updatedTime
 *
 * @author Hccake
 * @since 1.0
 */
@Slf4j
public class FillMetaObjectHandle implements MetaObjectHandler {

	/**
	 * 创建时间字段名
	 */
	private final static String CREATE_TIME = "createTime";

	/**
	 * 创建时间字段名（备用）
	 */
	private final static String CREATED_TIME = "createdTime";

	/**
	 * 更新时间字段名
	 */
	private final static String UPDATE_TIME = "updateTime";

	/**
	 * 更新时间字段名（备用）
	 */
	private final static String UPDATED_TIME = "updatedTime";

	/**
	 * 插入时自动填充 自动填充创建时间和更新时间
	 * @param metaObject 元对象
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		// 创建时间
		this.strictInsertFill(metaObject, CREATE_TIME, LocalDateTime.class, LocalDateTime.now());
		this.strictInsertFill(metaObject, CREATED_TIME, LocalDateTime.class, LocalDateTime.now());
		this.strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
		this.strictInsertFill(metaObject, UPDATED_TIME, LocalDateTime.class, LocalDateTime.now());
	}

	/**
	 * 更新时自动填充 自动填充更新时间
	 * @param metaObject 元对象
	 */
	@Override
	public void updateFill(MetaObject metaObject) {
		// 修改时间
		this.strictUpdateFill(metaObject, UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
		this.strictUpdateFill(metaObject, UPDATED_TIME, LocalDateTime.class, LocalDateTime.now());
	}

}
