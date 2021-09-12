package com.relaxed.common.risk.model.converter;

import com.relaxed.common.risk.model.dto.DataListsDTO;
import com.relaxed.common.risk.model.entity.DataLists;
import com.relaxed.common.risk.model.vo.DataListsVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.389
 */
@Mapper
public interface DataListsConverter {

	DataListsConverter INSTANCE = Mappers.getMapper(DataListsConverter.class);

	/**
	 * po -> vo
	 * @param dataLists {@link DataLists}
	 * @return {@link DataListsVO}
	 */
	DataListsVO poToVo(DataLists dataLists);

	/**
	 * dto -> po
	 * @param dataListsDTO {@link DataListsDTO}
	 * @return {@link DataLists}
	 */
	DataLists dtoToPo(DataListsDTO dataListsDTO);

	/**
	 * po -> vos
	 * @param dataListsProperties {@link List<DataLists>}
	 * @return {@link List<DataListsVO>}
	 */
	List<DataListsVO> poToVOs(List<DataLists> dataListsProperties);

}
