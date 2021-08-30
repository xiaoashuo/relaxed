package com.relaxed.extend.sms.executor.model.convert;



import com.relaxed.extend.sms.executor.model.dto.SignatureDTO;
import com.relaxed.extend.sms.executor.model.entity.Signature;
import com.relaxed.extend.sms.executor.model.vo.SignatureVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 签名表 转换器
 * </p>
 *
 * @author Yakir
 * @since 2021-08-27T18:18:41.224
 */
@Mapper
public interface SignatureConverter {

    SignatureConverter INSTANCE= Mappers.getMapper(SignatureConverter.class);

    /**
     * po -> vo
     * @param signature {@link Signature}
     * @return {@link SignatureVO}
     */
    SignatureVO poToVo(Signature signature);

    /**
     * dto -> po
     * @param signatureDTO {@link SignatureDTO}
     * @return {@link Signature}
     */
    Signature dtoToPo(SignatureDTO signatureDTO);

    /**
     * po -> vos
     * @param signatureProperties {@link List<Signature>}
     * @return  {@link  List<SignatureVO>}
     */
     List<SignatureVO> poToVOs(List<Signature> signatureProperties);
            
}
