package com.relaxed.extend.sms.executor.model.convert;



import com.relaxed.extend.sms.executor.model.dto.TemplateDTO;
import com.relaxed.extend.sms.executor.model.entity.Template;
import com.relaxed.extend.sms.executor.model.vo.TemplateVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 模板表 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-27T18:18:41.302
 */
@Mapper
public interface TemplateConverter {

    TemplateConverter INSTANCE= Mappers.getMapper(TemplateConverter.class);

    /**
     * po -> vo
     * @param template {@link Template}
     * @return {@link TemplateVO}
     */
    TemplateVO poToVo(Template template);

    /**
     * dto -> po
     * @param templateDTO {@link TemplateDTO}
     * @return {@link Template}
     */
    Template dtoToPo(TemplateDTO templateDTO);

    /**
     * po -> vos
     * @param templateProperties {@link List<Template>}
     * @return  {@link  List<TemplateVO>}
     */
     List<TemplateVO> poToVOs(List<Template> templateProperties);
            
}
