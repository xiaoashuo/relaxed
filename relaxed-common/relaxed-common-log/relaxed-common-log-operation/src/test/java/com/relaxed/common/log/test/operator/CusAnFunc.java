package com.relaxed.common.log.test.operator;

import com.relaxed.common.log.operation.annotation.LogFunc;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic CusAnFunc
 * @Description
 * @date 2023/12/15 10:10
 * @Version 1.0
 */
@Component
@LogFunc
public class CusAnFunc {

    @LogFunc
    public static String testAnnotation(Integer arg){
        return "test annotation method success"+arg;
    }

//    @LogFunc
    public  String testAnnotationNoStatic(){
        return "test annotation non static method success";
    }
}
