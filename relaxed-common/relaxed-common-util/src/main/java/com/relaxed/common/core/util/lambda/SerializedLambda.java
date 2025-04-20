package com.relaxed.common.core.util.lambda;

import java.io.*;

/**
 * Lambda表达式序列化类。 该类是 {@link java.lang.invoke.SerializedLambda} 的镜像实现，
 * 用于在反序列化过程中捕获Lambda表达式的元数据信息。
 *
 * 主要包含以下信息： 1. 捕获类（capturingClass） 2. 函数式接口信息（类名、方法名、方法签名） 3. 实现类信息（类名、方法名、方法签名、方法类型） 4.
 * 实例化方法类型 5. 捕获的参数
 *
 * @author Yakir
 * @since 1.0
 */
public class SerializedLambda implements Serializable {

	private static final long serialVersionUID = 8025925345765570181L;

	/**
	 * 定义Lambda表达式的类
	 */
	private Class<?> capturingClass;

	/**
	 * 函数式接口的完全限定名
	 */
	private String functionalInterfaceClass;

	/**
	 * 函数式接口中的方法名
	 */
	private String functionalInterfaceMethodName;

	/**
	 * 函数式接口方法的签名
	 */
	private String functionalInterfaceMethodSignature;

	/**
	 * Lambda表达式实现类的内部名称
	 */
	private String implClass;

	/**
	 * Lambda表达式实现方法的名称
	 */
	private String implMethodName;

	/**
	 * Lambda表达式实现方法的签名
	 */
	private String implMethodSignature;

	/**
	 * Lambda表达式实现方法的种类
	 */
	private int implMethodKind;

	/**
	 * 实例化方法的类型描述符
	 */
	private String instantiatedMethodType;

	/**
	 * Lambda表达式捕获的参数
	 */
	private Object[] capturedArgs;

	/**
	 * 从可序列化对象中提取Lambda表达式的元数据
	 * @param serializable 包含Lambda表达式的可序列化对象
	 * @return SerializedLambda实例
	 * @throws LambdaBusinessException 当序列化或反序列化过程出现异常时
	 */
	public static SerializedLambda extract(Serializable serializable) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(serializable);
			oos.flush();
			try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())) {
				@Override
				protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
					Class<?> clazz = super.resolveClass(desc);
					return clazz == java.lang.invoke.SerializedLambda.class ? SerializedLambda.class : clazz;
				}

			}) {
				return (SerializedLambda) ois.readObject();
			}
		}
		catch (IOException | ClassNotFoundException e) {
			throw new LambdaBusinessException(e);
		}
	}

	/**
	 * 获取实例化方法的类型描述符
	 * @return 类型描述符字符串
	 */
	public String getInstantiatedMethodType() {
		return instantiatedMethodType;
	}

	/**
	 * 获取定义Lambda表达式的类
	 * @return 捕获类的Class对象
	 */
	public Class<?> getCapturingClass() {
		return capturingClass;
	}

	/**
	 * 获取Lambda表达式实现方法的名称
	 * @return 实现方法名
	 */
	public String getImplMethodName() {
		return implMethodName;
	}

}
