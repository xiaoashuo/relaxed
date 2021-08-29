package com.relaxed.common.risk.engine.model.converter;

import com.relaxed.common.risk.engine.model.dto.FieldDTO;
import com.relaxed.common.risk.engine.model.entity.Field;
import com.relaxed.common.risk.engine.model.vo.FieldVO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T12:14:38.328
 */
@Mapper
public interface FieldConverter {

	FieldConverter INSTANCE = Mappers.getMapper(FieldConverter.class);

	/**
	 * po -> vo
	 * @param field {@link Field}
	 * @return {@link FieldVO}
	 */
	FieldVO poToVo(Field field);

	/**
	 * dto -> po
	 * @param fieldDTO {@link FieldDTO}
	 * @return {@link Field}
	 */
	Field dtoToPo(FieldDTO fieldDTO);

	/**
	 * po -> vos
	 * @param fieldProperties {@link List<Field>}
	 * @return {@link List<FieldVO>}
	 */
	List<FieldVO> poToVOs(List<Field> fieldProperties);

}
