package com.relaxed.test.idempotent;

import com.relaxed.common.idempotent.IdempotentAspect;
import com.relaxed.common.idempotent.key.IdempotentKeyStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author hccake
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan
public class IdempotentTestConfiguration {

	@Bean
	public IdempotentAspect idempotentAspect(IdempotentKeyStore idempotentKeyStore) {
		return new IdempotentAspect(idempotentKeyStore);
	}

	@Bean
	public IdempotentMethods idempotentMethods() {
		return new IdempotentMethods();
	}

}
