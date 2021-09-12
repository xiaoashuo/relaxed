package com.relaxed.common.risk.model.converter;

import com.relaxed.common.risk.model.dto.DataListMetaDTO;
import com.relaxed.common.risk.model.entity.DataListMeta;
import com.relaxed.common.risk.model.vo.DataListMetaVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.341
 */
@Mapper
public interface DataListMetaConverter {

	DataListMetaConverter INSTANCE = Mappers.getMapper(DataListMetaConverter.class);

	/**
	 * po -> vo
	 * @param dataListMeta {@link DataListMeta}
	 * @return {@link DataListMetaVO}
	 */
	DataListMetaVO poToVo(DataListMeta dataListMeta);

	/**
	 * dto -> po
	 * @param dataListMetaDTO {@link DataListMetaDTO}
	 * @return {@link DataListMeta}
	 */
	DataListMeta dtoToPo(DataListMetaDTO dataListMetaDTO);

	/**
	 * po -> vos
	 * @param dataListMetaProperties {@link List<DataListMeta>}
	 * @return {@link List<DataListMetaVO>}
	 */
	List<DataListMetaVO> poToVOs(List<DataListMeta> dataListMetaProperties);

}
