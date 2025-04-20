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
 * 字段加密助手类 提供字段加密和解密的核心功能，支持对象和集合类型的加解密处理 支持通过注解标记需要加密的字段，自动进行加解密操作
 *
 * @author Yakir
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class FieldEncryptHelper {

	/**
	 * 字段安全持有者，用于管理不同类型的加密器
	 */
	private static FieldSecurityHolder FILED_SEC_HOLDER = FieldSecurityHolder.INSTANCE;

	/**
	 * 字段安全配置属性
	 */
	private final FieldSecurityProperties fieldSecurityProperties;

	/**
	 * 获取默认的字段加密器
	 * @return 字段加密器实例
	 */
	public FieldEncryptor getFieldEncryptor() {
		String defSec = fieldSecurityProperties.getDefSec();
		FieldEncryptor encryptor = FILED_SEC_HOLDER.getByType(defSec);
		Assert.notNull(encryptor, "加密算法:{},不能为空", defSec);
		return encryptor;
	}

	/**
	 * 对对象中标记了加密注解的字段进行加密处理
	 * @param obj 需要加密的对象
	 */
	public void encrypt(Object obj) {
		if (ClassUtil.isPrimitiveWrapper(obj.getClass())) {
			return;
		}
		encryptOrDecrypt(obj, true);
	}

	/**
	 * 对对象中标记了加密注解的字段进行解密处理
	 * @param obj 需要解密的对象
	 */
	public void decrypt(Object obj) {
		encryptOrDecrypt(obj, false);
	}

	/**
	 * 对集合中所有对象进行解密处理
	 * @param list 需要解密的集合
	 */
	public void decrypt(Collection list) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		list.forEach(this::decrypt);
	}

	/**
	 * 对对象进行加密或解密处理
	 * @param obj 需要处理的对象
	 * @param encrypt true表示加密，false表示解密
	 */
	private void encryptOrDecrypt(Object obj, boolean encrypt) {
		if (Objects.isNull(obj)) {
			return;
		}
		handleObject(obj, encrypt);
	}

	/**
	 * 克隆对象
	 * @param source 源对象
	 * @return 克隆后的新对象
	 */
	public static Object clone(Object source) {
		Object result = BeanUtils.instantiateClass(source.getClass());
		BeanUtils.copyProperties(source, result);
		return result;
	}

	/**
	 * 处理对象的加密或解密
	 * @param obj 需要处理的对象
	 * @param encrypt true表示加密，false表示解密
	 */
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
	 * 对查询条件中的加密字段进行加密处理
	 * @param entityClass 实体类类型
	 * @param mpMap 字段名到MPGENVALX的映射关系
	 * @param paramNameValuePairs MPGENVALX到实际值的映射关系
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
