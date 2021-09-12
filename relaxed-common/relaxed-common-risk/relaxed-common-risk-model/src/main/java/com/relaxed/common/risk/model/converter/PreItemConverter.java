package com.relaxed.common.risk.model.converter;

import com.relaxed.common.risk.model.dto.PreItemDTO;
import com.relaxed.common.risk.model.entity.PreItem;
import com.relaxed.common.risk.model.vo.PreItemVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T13:57:50.664
 */
@Mapper
public interface PreItemConverter {

	PreItemConverter INSTANCE = Mappers.getMapper(PreItemConverter.class);

	/**
	 * po -> vo
	 * @param preItem {@link PreItem}
	 * @return {@link PreItemVO}
	 */
	PreItemVO poToVo(PreItem preItem);

	/**
	 * dto -> po
	 * @param preItemDTO {@link PreItemDTO}
	 * @return {@link PreItem}
	 */
	PreItem dtoToPo(PreItemDTO preItemDTO);

	/**
	 * po -> vos
	 * @param preItemProperties {@link List<PreItem>}
	 * @return {@link List<PreItemVO>}
	 */
	List<PreItemVO> poToVOs(List<PreItem> preItemProperties);

}
