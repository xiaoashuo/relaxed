package com.relaxed.extend.mybatis.plus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.relaxed.common.model.domain.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 扩展的 Mapper 接口
 * <p>
 * 继承自 MyBatis-Plus 的 BaseMapper 接口，提供分页和批量插入功能。 所有 Mapper 接口都应该继承此接口以使用这些扩展功能。
 *
 * @param <T> 实体类型
 */
public interface ExtendMapper<T> extends BaseMapper<T> {

	/**
	 * 根据分页参数生成 MyBatis-Plus 分页对象
	 * <p>
	 * 支持动态排序，可以根据分页参数中的排序信息进行排序。
	 * @param pageParam 分页参数
	 * @return MyBatis-Plus 分页对象
	 */
	IPage<T> prodPage(PageParam pageParam);

	/**
	 * 批量插入指定字段
	 * <p>
	 * 将集合中的数据批量插入到数据库中，只插入指定的字段。 具体实现参考 InsertBatchSomeColumn。
	 * @param list 实体集合
	 * @return 影响行数
	 */
	int insertBatchSomeColumn(Collection<T> list);

	/**
	 * 批量插入所有数据
	 * <p>
	 * 将集合中的所有数据批量插入到数据库中。
	 * @param list 实体集合
	 * @return 影响行数
	 */
	int insertBatch(Collection<T> list);

}