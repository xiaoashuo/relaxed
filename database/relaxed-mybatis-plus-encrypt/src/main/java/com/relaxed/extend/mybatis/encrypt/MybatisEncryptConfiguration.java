package com.relaxed.extend.mybatis.encrypt;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.relaxed.extend.mybatis.encrypt.sec.AesFieldEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author Yakir
 * @Topic MybatisEncryptConfiguration
 * @Description
 * @date 2024/10/18 13:41
 * @Version 1.0
 */
@EnableConfigurationProperties(FieldSecurityProperties.class)
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class MybatisEncryptConfiguration {

	private final FieldSecurityProperties fieldSecurityProperties;

	@Bean
	public FieldSecurityHolder securityHolder(@Nullable ObjectProvider<List<FieldEncryptor>> fieldSecProvider) {
		List<FieldEncryptor> fieldEncryptorList = fieldSecProvider.getIfAvailable();
		String defSec = fieldSecurityProperties.getDefSec();
		Assert.notBlank(defSec, "默认加密算法不能为空,请配置属性:{}", FieldSecurityProperties.PREFIX);
		FieldSecurityHolder instance = FieldSecurityHolder.INSTANCE;
		if (CollectionUtil.isNotEmpty(fieldEncryptorList)) {
			// 初始化默认算法
			initFieldSec(instance);
			// 加载三分算法
			for (FieldEncryptor fieldEncryptor : fieldEncryptorList) {
				instance.regByType(fieldEncryptor.secType(), fieldEncryptor);
			}
			FieldEncryptor defFieldEncryptor = instance.getByType(defSec);
			Assert.notNull(defFieldEncryptor, "加密算法:{},执行器不能为空", defSec);
		}
		else {
			// 初始化默认算法
			initFieldSec(instance);
			Assert.notNull(instance.getByType(defSec), "加密算法:{},执行器不能为空", defSec);

		}
		return instance;
	}

	private void initFieldSec(FieldSecurityHolder instance) {
		AesFieldEncryptor aesFieldEncryptor = new AesFieldEncryptor(fieldSecurityProperties.getAes());
		instance.regByType(FieldSecurityProperties.AES.SEC_FLAG, aesFieldEncryptor);
	}

}
