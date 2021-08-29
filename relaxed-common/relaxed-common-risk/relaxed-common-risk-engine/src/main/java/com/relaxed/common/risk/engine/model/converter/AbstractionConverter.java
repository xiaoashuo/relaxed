package com.relaxed.common.risk.engine.model.converter;

import com.relaxed.common.risk.engine.model.dto.AbstractionDTO;
import com.relaxed.common.risk.engine.model.entity.Abstraction;
import com.relaxed.common.risk.engine.model.vo.AbstractionVO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.507
 */
@Mapper
public interface AbstractionConverter {

	AbstractionConverter INSTANCE = Mappers.getMapper(AbstractionConverter.class);

	/**
	 * po -> vo
	 * @param abstraction {@link Abstraction}
	 * @return {@link AbstractionVO}
	 */
	AbstractionVO poToVo(Abstraction abstraction);

	/**
	 * dto -> po
	 * @param abstractionDTO {@link AbstractionDTO}
	 * @return {@link Abstraction}
	 */
	Abstraction dtoToPo(AbstractionDTO abstractionDTO);

	/**
	 * po -> vos
	 * @param abstractionProperties {@link List<Abstraction>}
	 * @return {@link List<AbstractionVO>}
	 */
	List<AbstractionVO> poToVOs(List<Abstraction> abstractionProperties);

}
