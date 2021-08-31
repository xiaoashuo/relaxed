package com.relaxed.common.risk.engine.model.converter;

import com.relaxed.common.risk.engine.model.dto.RuleDTO;
import com.relaxed.common.risk.engine.model.entity.Rule;
import com.relaxed.common.risk.engine.model.vo.RuleVO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-31T11:30:23.273
 */
@Mapper
public interface RuleConverter {

	RuleConverter INSTANCE = Mappers.getMapper(RuleConverter.class);

	/**
	 * po -> vo
	 * @param rule {@link Rule}
	 * @return {@link RuleVO}
	 */
	RuleVO poToVo(Rule rule);

	/**
	 * dto -> po
	 * @param ruleDTO {@link RuleDTO}
	 * @return {@link Rule}
	 */
	Rule dtoToPo(RuleDTO ruleDTO);

	/**
	 * po -> vos
	 * @param ruleProperties {@link List<Rule>}
	 * @return {@link List<RuleVO>}
	 */
	List<RuleVO> poToVOs(List<Rule> ruleProperties);

}
