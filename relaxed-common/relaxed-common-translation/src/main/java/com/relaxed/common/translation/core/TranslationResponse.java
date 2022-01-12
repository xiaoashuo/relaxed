package com.relaxed.common.translation.core;

import lombok.Data;

/**
 * @author Yakir
 * @Topic TranslationResponse1
 * @Description
 * @date 2022/1/12 14:34
 * @Version 1.0
 */
@Data
public class TranslationResponse<T>{

    public static final Integer SUCCESS_CODE = 200;

    private Integer code;

    private String msg;

    private T data;

    public static <T> TranslationResponse ok(T data){
        TranslationResponse<T> response = new TranslationResponse<>();
        response.setCode(SUCCESS_CODE);
        response.setMsg("success");
        response.setData(data);
        return response;

    }
    public boolean success() {
        return SUCCESS_CODE.equals(this.code);
    }

}
