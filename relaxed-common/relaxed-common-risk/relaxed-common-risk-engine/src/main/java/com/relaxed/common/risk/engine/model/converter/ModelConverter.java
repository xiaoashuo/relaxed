package com.relaxed.common.risk.engine.model.converter;

import com.relaxed.common.risk.engine.model.dto.ModelDTO;
import com.relaxed.common.risk.engine.model.entity.Model;
import com.relaxed.common.risk.engine.model.vo.ModelVO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T09:04:20.388
 */
@Mapper
public interface ModelConverter {

	ModelConverter INSTANCE = Mappers.getMapper(ModelConverter.class);

	/**
	 * po -> vo
	 * @param model {@link Model}
	 * @return {@link ModelVO}
	 */
	ModelVO poToVo(Model model);

	/**
	 * dto -> po
	 * @param modelDTO {@link ModelDTO}
	 * @return {@link Model}
	 */
	Model dtoToPo(ModelDTO modelDTO);

	/**
	 * po -> vos
	 * @param modelProperties {@link List<Model>}
	 * @return {@link List<ModelVO>}
	 */
	List<ModelVO> poToVOs(List<Model> modelProperties);

}
