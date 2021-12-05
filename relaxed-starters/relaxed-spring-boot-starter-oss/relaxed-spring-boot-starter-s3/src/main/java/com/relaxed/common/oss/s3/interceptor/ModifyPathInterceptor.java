package com.relaxed.common.oss.s3.interceptor;

import com.relaxed.common.oss.s3.modifier.PathModifier;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.interceptor.Context;
import software.amazon.awssdk.core.interceptor.ExecutionAttributes;
import software.amazon.awssdk.core.interceptor.ExecutionInterceptor;
import software.amazon.awssdk.core.interceptor.SdkExecutionAttribute;
import software.amazon.awssdk.http.SdkHttpRequest;

/**
 * <p>
 * oss 拦截器
 * </p>
 * <p>
 * 修改发出请求的路径, 如果路径前携带了 bucket , 则移除
 * </p>
 *
 * @author lingting 2021/5/12 18:46
 */
@RequiredArgsConstructor
public class ModifyPathInterceptor implements ExecutionInterceptor {

	private final String bucket;

	private final boolean useVirtualAddress;

	private final PathModifier pathModifier;

	@Override
	public SdkHttpRequest modifyHttpRequest(Context.ModifyHttpRequest context,
			ExecutionAttributes executionAttributes) {

		SdkHttpRequest request = context.httpRequest();

		SdkHttpRequest.Builder builder = request.toBuilder();

		// 若使用虚拟主机模式则 保持源路径
		// 若使用路径模式 则需要 移除 path 前的 bucket 声明
		String sourcePath = request.encodedPath();

		if (useVirtualAddress) {
			String proxyPath = pathModifier.modifyRequestPath(bucket,
					executionAttributes.getAttribute(SdkExecutionAttribute.OPERATION_NAME), sourcePath);
			builder.encodedPath(proxyPath);
		}

		return builder.build();
	}

}
