package com.relaxed.common.http.core.notify;

import com.relaxed.common.http.event.ReqReceiveEvent;

/**
 * @author Yakir
 * @Topic RequestResultNotifier
 * @Description
 * @date 2022/5/23 11:03
 * @Version 1.0
 */
public interface RequestResultNotifier {

	void notify(ReqReceiveEvent reqReceiveEvent);

}
