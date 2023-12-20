package com.relaxed.common.log.test.operator;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.operation.discover.func.IParseFunc;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Yakir
 * @Topic CusIFunc
 * @Description
 * @date 2023/12/15 10:09
 * @Version 1.0
 */
@Component
public class CusIParseFunc implements IParseFunc {
    @Override
    public String namespace() {
        return "ifunc";
    }

    @Override
    public String name() {
        return "test";
    }

    @Override
    public String apply(Object[] args) {
        return "i func test success,params"+ StrUtil.join(",",args);
    }


    public String applyList(String param,String[] args) {
        return "i func test success,params"+ StrUtil.join(",",args,param);
    }
}
