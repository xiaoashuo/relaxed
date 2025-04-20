package com.relaxed.extend.mybatis.encrypt;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.segments.NormalSegmentList;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * MyBatis 字段加密拦截器 用于拦截 SQL 参数设置过程，对标记了加密注解的字段进行加密处理 支持实体类字段加密、Wrapper 条件加密和参数加密
 *
 * @author Yakir
 */
@RequiredArgsConstructor
@Intercepts({ @Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class), })
@Component
@Slf4j
public class MybatisEncryptInterceptor implements Interceptor {

	/**
	 * 字段加密助手，用于处理字段加密逻辑
	 */
	private final FieldEncryptHelper fieldEncryptHelper;

	/**
	 * 拦截参数设置过程，对需要加密的字段进行加密处理
	 * @param invocation 拦截器调用信息
	 * @return 处理后的结果
	 * @throws Throwable 处理过程中可能抛出的异常
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (invocation.getTarget() instanceof ParameterHandler) {
			// 获取参数
			ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
			// 获取参数字段
			Object parameterObject = parameterHandler.getParameterObject();
			if (Objects.isNull(parameterObject)) {
				return invocation.proceed();
			}
			Class<?> parameterClass = parameterObject.getClass();
			boolean isBasicType = ClassUtil.isBasicType(parameterClass)
					|| String.class.isAssignableFrom(parameterClass);
			if (!isBasicType) {
				if (parameterObject instanceof MapperMethod.ParamMap) {
					MapperMethod.ParamMap<Object> paramMap = (MapperMethod.ParamMap<Object>) parameterObject;
					if (paramMap.containsKey(Constants.WRAPPER) && Objects.nonNull(paramMap.get(Constants.WRAPPER))) {
						AbstractWrapper<Object, ?, ?> wrapper = (AbstractWrapper<Object, ?, ?>) paramMap
								.get(Constants.WRAPPER);
						// MPGENVAL->值映射
						Map<String, Object> paramNameValuePairs = wrapper.getParamNameValuePairs();

						//// 解决· mybatis plus 分页查询 二阶段查询 导致第一次参数加密 第二次执行 再次加密问题
						if (paramMap.containsKey("page")) {
							if (!paramMap.containsKey("x-old-pvPairs")) {
								// 首次进入
								paramMap.put("x-old-pvPairs", new HashMap<>(paramNameValuePairs));
							}
							else {
								Map<String, Object> oldParamNameValuePairs = (Map<String, Object>) paramMap
										.get("x-old-pvPairs");
								wrapper.getParamNameValuePairs().putAll(oldParamNameValuePairs);
							}
						}
						String simpleName = wrapper.getClass().getSimpleName();
						if (!"EmptyWrapper".equals(simpleName)) {
							wrapperEwEncrypt(wrapper);
						}
					}
					else if (paramMap.containsKey(Constants.ENTITY)
							&& Objects.nonNull(paramMap.get(Constants.ENTITY))) {
						Object clone = FieldEncryptHelper.clone(paramMap.get(Constants.ENTITY));
						paramMap.put(Constants.ENTITY, clone);
						paramMap.put("param1", clone);
						Object handleObj = paramMap.get(Constants.ENTITY);
						fieldEncryptHelper.encrypt(handleObj);
					}
					else {
						List<String> encryptArgs = searchParamAnnotation(parameterHandler);
						// 对普通字段进行加密
						if (!CollectionUtils.isEmpty(encryptArgs)) {

							for (String encryptArg : encryptArgs) {
								String value = (String) ((MapperMethod.ParamMap<?>) parameterObject).get(encryptArg);
								String encryptValue = fieldEncryptHelper.getFieldEncryptor().encrypt(value);
								((MapperMethod.ParamMap<String>) parameterObject).put(encryptArg, encryptValue);
							}
						}
					}

				}
				else {
					ReflectUtil.setFieldValue(parameterHandler, "parameterObject",
							FieldEncryptHelper.clone(parameterObject));
					fieldEncryptHelper.encrypt(parameterHandler.getParameterObject());
				}
			}
			else {
				// 基础类型
				if (String.class.isAssignableFrom(parameterClass)) {
					MetaObject metaObject = SystemMetaObject.forObject(parameterHandler);
					MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");
					String methodName = mappedStatement.getId();
					Class<?> mapperClass = Class.forName(methodName.substring(0, methodName.lastIndexOf('.')));
					methodName = methodName.substring(methodName.lastIndexOf('.') + 1);
					Method method = ReflectUtil.getMethod(mapperClass, methodName,
							parameterHandler.getParameterObject().getClass());
					Assert.notNull(method, "当前方法未查找到, mapperId:{}", mappedStatement.getId());
					Annotation[][] pa = method.getParameterAnnotations();
					for (int i = 0; i < pa.length; i++) {
						for (Annotation annotation : pa[i]) {
							if (annotation instanceof ParamEncrypt) {
								String encrypt = fieldEncryptHelper.getFieldEncryptor()
										.encrypt((String) parameterObject);
								ReflectUtil.setFieldValue(parameterHandler, "parameterObject", encrypt);
								break;
							}
						}
					}
				}
			}
		}
		return invocation.proceed();
	}

	/**
	 * 处理 MyBatis-Plus Wrapper 中的加密字段
	 * @param wrapper 查询条件包装器
	 */
	private void wrapperEwEncrypt(AbstractWrapper<Object, ?, ?> wrapper) {
		Map<String, Object> paramNameValuePairs = wrapper.getParamNameValuePairs();
		// 实体类型检测
		Class<Object> entityClass = wrapper.getEntityClass();
		Assert.notNull(entityClass, "当前实体类型信息未找到,无法寻找加密注解");
		// 获取where条件片段 提出属性映射
		MergeSegments expression = wrapper.getExpression();
		NormalSegmentList normalSegmentList = expression.getNormal();
		// (c_list_id = #{ew.paramNameValuePairs.MPGENVAL1} AND c_custtype
		// LIKE #{ew.paramNameValuePairs.MPGENVAL2})
		String normalSqlSegment = normalSegmentList.getSqlSegment();
		// 解析 where条件sql 获取多值map 字段名->[随机条件名]
		MultiValueMap<String, String> mpMap = MpJsqlParserExt.parseSql(normalSqlSegment);

		if (wrapper instanceof Update) {
			// set语句
			List<String> updateFieldValueList = (List<String>) ReflectUtil.getFieldValue(wrapper, "sqlSet");

			if (CollectionUtil.isNotEmpty(updateFieldValueList)) {
				for (String val : updateFieldValueList) {
					String[] pair = val.replace("#{ew.paramNameValuePairs.", "").replace("}", "").split("=");
					mpMap.add(pair[0], pair[1]);
				}
			}
		}
		// 总MPGENVAL数量
		long count = mpMap.values().stream().mapToInt(List::size).sum();

		Assert.isTrue(paramNameValuePairs.size() == count, "字段值MPGENVAL数量不匹配,参数数量:{},sql提取出数量:{}",
				paramNameValuePairs.size(), count);
		fieldEncryptHelper.encrypt(entityClass, mpMap, paramNameValuePairs);
	}

	/**
	 * 搜索方法参数中的加密注解
	 * @param parameterHandler 参数处理器
	 * @return 需要加密的参数名称列表
	 * @throws ClassNotFoundException 当找不到 Mapper 类时抛出
	 */
	private List<String> searchParamAnnotation(ParameterHandler parameterHandler) throws ClassNotFoundException {
		MetaObject metaObject = SystemMetaObject.forObject(parameterHandler);
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");
		String methodName = mappedStatement.getId();
		Class<?> mapperClass = Class.forName(methodName.substring(0, methodName.lastIndexOf('.')));
		methodName = methodName.substring(methodName.lastIndexOf('.') + 1);
		// 此处要求 mapper不允许出现同名方法 不然mapper id相同会有问题
		Method[] methods = mapperClass.getDeclaredMethods();
		Method method = null;
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				method = m;
				break;
			}
		}
		Assert.notNull(method, "当前方法未查找到, mapperId:{}", mappedStatement.getId());
		List<String> paramNames = new ArrayList<>();

		if (method != null) {
			Parameter[] parameters = method.getParameters();

			int index = 1;
			for (Parameter parameter : parameters) {
				String key = "param" + index;
				ParamEncrypt parameterAnnotation = parameter.getAnnotation(ParamEncrypt.class);
				if (parameterAnnotation != null) {
					Param annotation = parameter.getAnnotation(Param.class);
					if (String.class.isAssignableFrom(parameter.getType())) {
						// 只有string类型进行加密
						String fieldKey = annotation.value();
						paramNames.add(fieldKey);
						paramNames.add(key);
					}
					else {
						log.warn("当前字段类型非String,不支持加密,id:{},字段名{}", methodName, annotation.value());
					}
				}
				index++;
			}

		}
		return paramNames;
	}

}
