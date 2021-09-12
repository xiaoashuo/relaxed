package com.relaxed.common.risk.model.converter;

import com.relaxed.common.risk.model.dto.ModelConfParamDTO;
import com.relaxed.common.risk.model.entity.ModelConfParam;
import com.relaxed.common.risk.model.vo.ModelConfParamVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-31T09:58:08.892
 */
@Mapper
public interface ModelConfParamConverter {

	ModelConfParamConverter INSTANCE = Mappers.getMapper(ModelConfParamConverter.class);

	/**
	 * po -> vo
	 * @param modelConfParam {@link ModelConfParam}
	 * @return {@link ModelConfParamVO}
	 */
	ModelConfParamVO poToVo(ModelConfParam modelConfParam);

	/**
	 * dto -> po
	 * @param modelConfParamDTO {@link ModelConfParamDTO}
	 * @return {@link ModelConfParam}
	 */
	ModelConfParam dtoToPo(ModelConfParamDTO modelConfParamDTO);

	/**
	 * po -> vos
	 * @param modelConfParamProperties {@link List<ModelConfParam>}
	 * @return {@link List<ModelConfParamVO>}
	 */
	List<ModelConfParamVO> poToVOs(List<ModelConfParam> modelConfParamProperties);

}
