package com.relaxed.common.risk.engine.model.converter;

import com.relaxed.common.risk.engine.model.dto.ModelConfDTO;
import com.relaxed.common.risk.engine.model.entity.ModelConf;
import com.relaxed.common.risk.engine.model.vo.ModelConfVO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-31T09:58:08.656
 */
@Mapper
public interface ModelConfConverter {

	ModelConfConverter INSTANCE = Mappers.getMapper(ModelConfConverter.class);

	/**
	 * po -> vo
	 * @param modelConf {@link ModelConf}
	 * @return {@link ModelConfVO}
	 */
	ModelConfVO poToVo(ModelConf modelConf);

	/**
	 * dto -> po
	 * @param modelConfDTO {@link ModelConfDTO}
	 * @return {@link ModelConf}
	 */
	ModelConf dtoToPo(ModelConfDTO modelConfDTO);

	/**
	 * po -> vos
	 * @param modelConfProperties {@link List<ModelConf>}
	 * @return {@link List<ModelConfVO>}
	 */
	List<ModelConfVO> poToVOs(List<ModelConf> modelConfProperties);

}
