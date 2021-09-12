package com.relaxed.common.risk.model.converter;

import com.relaxed.common.risk.model.dto.ActivationDTO;
import com.relaxed.common.risk.model.entity.Activation;
import com.relaxed.common.risk.model.vo.ActivationVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-29T18:48:19.435
 */
@Mapper
public interface ActivationConverter {

	ActivationConverter INSTANCE = Mappers.getMapper(ActivationConverter.class);

	/**
	 * po -> vo
	 * @param activation {@link Activation}
	 * @return {@link ActivationVO}
	 */
	ActivationVO poToVo(Activation activation);

	/**
	 * dto -> po
	 * @param activationDTO {@link ActivationDTO}
	 * @return {@link Activation}
	 */
	Activation dtoToPo(ActivationDTO activationDTO);

	/**
	 * po -> vos
	 * @param activationProperties {@link List<Activation>}
	 * @return {@link List<ActivationVO>}
	 */
	List<ActivationVO> poToVOs(List<Activation> activationProperties);

}
