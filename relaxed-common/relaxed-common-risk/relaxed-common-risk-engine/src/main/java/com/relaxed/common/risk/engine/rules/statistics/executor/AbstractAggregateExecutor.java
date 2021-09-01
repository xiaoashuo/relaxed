package com.relaxed.common.risk.engine.rules.statistics.executor;

import com.relaxed.common.risk.engine.manage.ModelEventManageService;
import com.relaxed.common.risk.engine.rules.statistics.AggregateExecutor;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Yakir
 * @Topic AbstractAggregateExecutor
 * @Description
 * @date 2021/8/30 16:38
 * @Version 1.0
 */
public abstract class AbstractAggregateExecutor<T extends AggregateParam, R> implements AggregateExecutor<T, R> {

}
