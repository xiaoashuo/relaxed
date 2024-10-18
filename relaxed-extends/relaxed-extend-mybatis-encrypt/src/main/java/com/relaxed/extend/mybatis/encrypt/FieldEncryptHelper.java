package com.relaxed.extend.mybatis.encrypt;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Yakir
 * @Topic FieldEncryptUtil
 * @Description
 * @date 2024/10/9 17:08
 * @Version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class FieldEncryptHelper {

	private static FieldSecurityHolder FILED_SEC_HOLDER = FieldSecurityHolder.INSTANCE;

	public FieldEncryptor getFieldEncryptor() {
		FieldEncryptor encryptor = FILED_SEC_HOLDER.getByType(FieldSecurityProperties.AES.SEC_FLAG);
		Assert.notNull(encryptor, "加密算法:{},不能为空", FieldSecurityProperties.AES.SEC_FLAG);
		return encryptor;
	}

	/** 对EncryptField注解进行加密处理 */
	public void encrypt(Object obj) {
		if (ClassUtil.isPrimitiveWrapper(obj.getClass())) {
			return;
		}
		encryptOrDecrypt(obj, true);
	}

	/** 对EncryptField注解进行解密处理 */
	public void decrypt(Object obj) {
		encryptOrDecrypt(obj, false);
	}

	/** 对EncryptField注解进行解密处理 */
	public void decrypt(Collection list) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		list.forEach(this::decrypt);
	}

	/** 对EncryptField注解进行加解密处理 */
	private void encryptOrDecrypt(Object obj, boolean encrypt) {
		if (Objects.isNull(obj)) {
			return;
		}
		handleObject(obj, encrypt);
	}

	public static Object clone(Object source) {
		Object result = BeanUtils.instantiateClass(source.getClass());
		BeanUtils.copyProperties(source, result);
		return result;
	}

	private void handleObject(Object obj, boolean encrypt) {
		// 判断类上面是否有加密注解
		FieldEncrypt fieldEncrypt = AnnotationUtils.findAnnotation(obj.getClass(), FieldEncrypt.class);
		if (ObjectUtil.isNull(fieldEncrypt)) {
			return;
		}
		// 获取所有带加密注解的字段
		List<Field> encryptFields = FieldUtils.getFieldsListWithAnnotation(obj.getClass(), FieldEncrypt.class);
		// 没有字段需要加密，则跳过
		if (CollectionUtils.isEmpty(encryptFields)) {
			return;
		}
		for (Field field : encryptFields) {
			// 不为字符串类型 则跳过
			if (!ClassUtil.isAssignable(String.class, field.getType())) {
				continue;
			}
			//
			String oldValue = (String) ReflectUtil.getFieldValue(obj, field);
			if (StringUtils.isBlank(oldValue)) {
				continue;
			}
			//
			String logText = null, newValue = null;
			if (encrypt) {
				logText = "encrypt";
				newValue = getFieldEncryptor().encrypt(oldValue);
			}
			else {
				logText = "decrypt";
				newValue = getFieldEncryptor().decrypt(oldValue);
			}

			log.info("{} success[{}=>{}]. before:{}, after:{}", logText, field.getDeclaringClass().getName(),
					field.getName(), oldValue, newValue);
			ReflectUtil.setFieldValue(obj, field, newValue);

		}
	}

	/**
	 * 编码加密字段查询
	 * @param entityClass 实体类
	 * @param mpMap 字段->MPGENVALX 映射 多值MAP
	 * @param paramNameValuePairs MPGENVALX->值映射
	 */
	public void encrypt(Class<?> entityClass, MultiValueMap<String, String> mpMap,
			Map<String, Object> paramNameValuePairs) {
		// 判断类上面是否有加密注解
		FieldEncrypt fieldEncrypt = AnnotationUtils.findAnnotation(entityClass, FieldEncrypt.class);
		if (ObjectUtil.isNull(fieldEncrypt)) {
			return;
		}
		// 获取所有带加密注解的字段
		List<Field> encryptFields = FieldUtils.getFieldsListWithAnnotation(entityClass, FieldEncrypt.class);
		// 没有字段需要加密，则跳过
		if (CollectionUtils.isEmpty(encryptFields)) {
			return;
		}
		for (Field field : encryptFields) {
			// 不为字符串类型 则跳过
			if (!ClassUtil.isAssignable(String.class, field.getType())) {
				continue;
			}
			//
			String underlineCaseName = StrUtil.toUnderlineCase(field.getName());
			List<String> list = mpMap.get(underlineCaseName);
			for (String mapKey : list) {
				String oldValue = (String) paramNameValuePairs.get(mapKey);
				if (StringUtils.isBlank(oldValue)) {
					continue;
				}
				String newValue = getFieldEncryptor().encrypt(oldValue);
				log.info("{} success[{}=>{}]. before:{}, after:{}", "encrypt", field.getDeclaringClass().getName(),
						field.getName(), oldValue, newValue);
				paramNameValuePairs.put(mapKey, newValue);
			}

		}
	}

}
