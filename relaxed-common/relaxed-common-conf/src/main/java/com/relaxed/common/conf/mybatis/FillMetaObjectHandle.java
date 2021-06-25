package com.relaxed.common.conf.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.relaxed.common.core.constants.GlobalConstants;
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

	@Override
	public void insertFill(MetaObject metaObject) {
		// 创建时间
		this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// 修改时间
		this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
	}

}
