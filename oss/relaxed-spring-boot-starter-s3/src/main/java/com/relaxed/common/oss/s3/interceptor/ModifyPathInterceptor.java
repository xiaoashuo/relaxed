package com.relaxed.common.oss.s3.interceptor;

import com.relaxed.common.oss.s3.modifier.PathModifier;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.interceptor.Context;
import software.amazon.awssdk.core.interceptor.ExecutionAttributes;
import software.amazon.awssdk.core.interceptor.ExecutionInterceptor;
import software.amazon.awssdk.core.interceptor.SdkExecutionAttribute;
import software.amazon.awssdk.http.SdkHttpRequest;

/**
 * OSS 请求路径修改拦截器。 用于修改 S3 请求的路径，主要功能包括： 1. 在虚拟主机模式下，保持源路径不变 2. 在路径模式下，移除路径前的 bucket 声明 3.
 * 支持自定义路径修改逻辑
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
public class ModifyPathInterceptor implements ExecutionInterceptor {

	private final String bucket;

	private final boolean useVirtualAddress;

	private final PathModifier pathModifier;

	/**
	 * 修改 HTTP 请求路径。 根据虚拟主机模式设置，决定是否修改请求路径。
	 * @param context 请求上下文
	 * @param executionAttributes 执行属性
	 * @return 修改后的 HTTP 请求
	 */
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
