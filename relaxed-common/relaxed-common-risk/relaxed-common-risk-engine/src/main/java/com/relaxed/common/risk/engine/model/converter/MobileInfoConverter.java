package com.relaxed.common.risk.engine.model.converter;

import com.relaxed.common.risk.engine.model.dto.MobileInfoDTO;
import com.relaxed.common.risk.engine.model.entity.MobileInfo;
import com.relaxed.common.risk.engine.model.vo.MobileInfoVO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-09-01T13:49:40.174
 */
@Mapper
public interface MobileInfoConverter {

	MobileInfoConverter INSTANCE = Mappers.getMapper(MobileInfoConverter.class);

	/**
	 * po -> vo
	 * @param mobileInfo {@link MobileInfo}
	 * @return {@link MobileInfoVO}
	 */
	MobileInfoVO poToVo(MobileInfo mobileInfo);

	/**
	 * PO -> VO
	 * @param mobileInfo
	 * @return
	 */
	MobileInfo voToPo(MobileInfoVO mobileInfo);

	/**
	 * dto -> po
	 * @param mobileInfoDTO {@link MobileInfoDTO}
	 * @return {@link MobileInfo}
	 */
	MobileInfo dtoToPo(MobileInfoDTO mobileInfoDTO);

	/**
	 * po -> vos
	 * @param mobileInfoProperties {@link List< MobileInfo >}
	 * @return {@link List< MobileInfoVO >}
	 */
	List<MobileInfoVO> poToVOs(List<MobileInfo> mobileInfoProperties);

}
