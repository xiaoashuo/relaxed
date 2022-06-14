package com.relaxed.common.core.util.lambda;

import java.io.*;

/**
 * @author Yakir
 * @Topic SerializedLambda
 * @Description 当前类是 {@link java.lang.invoke.SerializedLambda } 的一个镜像
 * @date 2022/6/14 16:50
 * @Version 1.0
 */
public class SerializedLambda implements Serializable {

	private static final long serialVersionUID = 8025925345765570181L;

	private Class<?> capturingClass;

	private String functionalInterfaceClass;

	private String functionalInterfaceMethodName;

	private String functionalInterfaceMethodSignature;

	private String implClass;

	private String implMethodName;

	private String implMethodSignature;

	private int implMethodKind;

	private String instantiatedMethodType;

	private Object[] capturedArgs;

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

	public String getInstantiatedMethodType() {
		return instantiatedMethodType;
	}

	public Class<?> getCapturingClass() {
		return capturingClass;
	}

	public String getImplMethodName() {
		return implMethodName;
	}

}
