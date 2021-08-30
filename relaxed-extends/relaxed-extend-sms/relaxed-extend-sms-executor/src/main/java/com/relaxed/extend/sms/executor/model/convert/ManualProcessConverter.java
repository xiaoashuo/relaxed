package com.relaxed.extend.sms.executor.model.convert;



import com.relaxed.extend.sms.executor.model.dto.ManualProcessDTO;
import com.relaxed.extend.sms.executor.model.entity.ManualProcess;
import com.relaxed.extend.sms.executor.model.vo.ManualProcessVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 人工处理任务表 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-27T18:18:41.264
 */
@Mapper
public interface ManualProcessConverter {

    ManualProcessConverter INSTANCE= Mappers.getMapper(ManualProcessConverter.class);

    /**
     * po -> vo
     * @param manualProcess {@link ManualProcess}
     * @return {@link ManualProcessVO}
     */
    ManualProcessVO poToVo(ManualProcess manualProcess);

    /**
     * dto -> po
     * @param manualProcessDTO {@link ManualProcessDTO}
     * @return {@link ManualProcess}
     */
    ManualProcess dtoToPo(ManualProcessDTO manualProcessDTO);

    /**
     * po -> vos
     * @param manualProcessProperties {@link List<ManualProcess>}
     * @return  {@link  List<ManualProcessVO>}
     */
     List<ManualProcessVO> poToVOs(List<ManualProcess> manualProcessProperties);
            
}
