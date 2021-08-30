package com.relaxed.extend.sms.executor.model.convert;



import com.relaxed.extend.sms.executor.model.dto.ConfigDTO;
import com.relaxed.extend.sms.executor.model.entity.Config;
import com.relaxed.extend.sms.executor.model.vo.ConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 配置表 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-27T14:55:25.330
 */
@Mapper
public interface ConfigConverter {

    ConfigConverter INSTANCE= Mappers.getMapper(ConfigConverter.class);

    /**
     * po -> vo
     * @param config {@link Config}
     * @return {@link ConfigVO}
     */
    ConfigVO poToVo(Config config);

    /**
     * po -> dto
     * @param config
     * @return
     */
    ConfigDTO poToDto(Config config);
    /**
     * dto -> po
     * @param configDTO {@link ConfigDTO}
     * @return {@link Config}
     */
    Config dtoToPo(ConfigDTO configDTO);

    /**
     * po -> vos
     * @param configProperties {@link List<Config>}
     * @return  {@link  List<ConfigVO>}
     */
     List<ConfigVO> poToVOs(List<Config> configProperties);
            
}
