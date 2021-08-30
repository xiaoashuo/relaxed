package com.relaxed.extend.sms.executor.model.qo;

import java.io.Serializable;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 签名表 查询对象
 *
 * @author Yakir
 * @since 2021-08-27T18:18:41.224
 */
@ApiModel(value = "签名表")
@Data
@Accessors(chain = true)
public class SignatureQO implements Serializable {

   
         /**
      * 主键
      */
     @ApiModelProperty(value = "主键")
     private String id;
       
       
       
       
       
       
       
       
       
    
}

