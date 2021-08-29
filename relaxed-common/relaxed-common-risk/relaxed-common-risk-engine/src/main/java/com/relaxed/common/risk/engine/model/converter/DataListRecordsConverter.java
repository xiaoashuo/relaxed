package com.relaxed.common.risk.engine.model.converter;

import com.relaxed.common.risk.engine.model.dto.DataListRecordsDTO;
import com.relaxed.common.risk.engine.model.entity.DataListRecords;
import com.relaxed.common.risk.engine.model.vo.DataListRecordsVO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.131
 */
@Mapper
public interface DataListRecordsConverter {

	DataListRecordsConverter INSTANCE = Mappers.getMapper(DataListRecordsConverter.class);

	/**
	 * po -> vo
	 * @param dataListRecords {@link DataListRecords}
	 * @return {@link DataListRecordsVO}
	 */
	DataListRecordsVO poToVo(DataListRecords dataListRecords);

	/**
	 * dto -> po
	 * @param dataListRecordsDTO {@link DataListRecordsDTO}
	 * @return {@link DataListRecords}
	 */
	DataListRecords dtoToPo(DataListRecordsDTO dataListRecordsDTO);

	/**
	 * po -> vos
	 * @param dataListRecordsProperties {@link List<DataListRecords>}
	 * @return {@link List<DataListRecordsVO>}
	 */
	List<DataListRecordsVO> poToVOs(List<DataListRecords> dataListRecordsProperties);

}
