package com.relaxed.extend.sms.executor.model.convert;



import com.relaxed.extend.sms.executor.model.dto.SendLogDTO;
import com.relaxed.extend.sms.executor.model.entity.SendLog;
import com.relaxed.extend.sms.executor.model.vo.SendLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 日志表 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-27T18:18:41.122
 */
@Mapper
public interface SendLogConverter {

    SendLogConverter INSTANCE= Mappers.getMapper(SendLogConverter.class);

    /**
     * po -> vo
     * @param sendLog {@link SendLog}
     * @return {@link SendLogVO}
     */
    SendLogVO poToVo(SendLog sendLog);

    /**
     * dto -> po
     * @param sendLogDTO {@link SendLogDTO}
     * @return {@link SendLog}
     */
    SendLog dtoToPo(SendLogDTO sendLogDTO);

    /**
     * po -> vos
     * @param sendLogProperties {@link List<SendLog>}
     * @return  {@link  List<SendLogVO>}
     */
     List<SendLogVO> poToVOs(List<SendLog> sendLogProperties);
            
}
