package com.relaxed.common.risk.model.converter;

import com.relaxed.common.risk.model.dto.PluginDTO;
import com.relaxed.common.risk.model.entity.Plugin;
import com.relaxed.common.risk.model.vo.PluginVO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-09-28T13:43:11.834
 */
@Mapper
public interface PluginConverter {

	PluginConverter INSTANCE = Mappers.getMapper(PluginConverter.class);

	/**
	 * po -> vo
	 * @param plugin {@link Plugin}
	 * @return {@link PluginVO}
	 */
	PluginVO poToVo(Plugin plugin);

	/**
	 * dto -> po
	 * @param pluginDTO {@link PluginDTO}
	 * @return {@link Plugin}
	 */
	Plugin dtoToPo(PluginDTO pluginDTO);

	/**
	 * po -> vos
	 * @param pluginProperties {@link List<Plugin>}
	 * @return {@link List<PluginVO>}
	 */
	List<PluginVO> poToVOs(List<Plugin> pluginProperties);

}
