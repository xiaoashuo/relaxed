package com.relaxed.common.risk.engine.model.converter;

import com.relaxed.common.risk.engine.model.dto.RuleHistoryDTO;
import com.relaxed.common.risk.engine.model.entity.RuleHistory;
import com.relaxed.common.risk.engine.model.vo.RuleHistoryVO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-31T11:30:23.317
 */
@Mapper
public interface RuleHistoryConverter {

	RuleHistoryConverter INSTANCE = Mappers.getMapper(RuleHistoryConverter.class);

	/**
	 * po -> vo
	 * @param ruleHistory {@link RuleHistory}
	 * @return {@link RuleHistoryVO}
	 */
	RuleHistoryVO poToVo(RuleHistory ruleHistory);

	/**
	 * dto -> po
	 * @param ruleHistoryDTO {@link RuleHistoryDTO}
	 * @return {@link RuleHistory}
	 */
	RuleHistory dtoToPo(RuleHistoryDTO ruleHistoryDTO);

	/**
	 * po -> vos
	 * @param ruleHistoryProperties {@link List<RuleHistory>}
	 * @return {@link List<RuleHistoryVO>}
	 */
	List<RuleHistoryVO> poToVOs(List<RuleHistory> ruleHistoryProperties);

}
