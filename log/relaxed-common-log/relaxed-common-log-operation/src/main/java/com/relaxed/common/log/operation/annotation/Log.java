package com.relaxed.common.log.operation.annotation;

import java.lang.annotation.*;

/**
 * An annotation for marking methods that require operation logging. This annotation can
 * be used to automatically record method execution details, including parameters, return
 * values, execution time, and custom messages. It supports grouping logs and specifying
 * operation types for better organization and filtering of log entries.
 *
 * @author Yakir
 * @since 1.0.0
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

	/**
	 * Specifies the log group name for categorizing log entries. Logs with the same group
	 * name will be grouped together for easier management and querying.
	 * @return the group name, defaults to "default" if not specified
	 */
	String group() default "default";

	/**
	 * Defines a custom message to be recorded with the log entry. This message can
	 * contain descriptive information about the operation being performed.
	 * @return the custom message to be logged
	 */
	String msg() default "";

	/**
	 * Specifies the type of operation being performed. This can be used to categorize
	 * different types of operations (e.g., CREATE, UPDATE, DELETE). The type should be
	 * defined as an integer constant in a separate enumeration or constants class.
	 * @return the operation type code
	 */
	int type();

	/**
	 * Controls whether method parameters should be recorded in the log entry. When
	 * enabled, the values of all method parameters will be serialized and stored. This
	 * can be useful for debugging and audit purposes.
	 * @return true if parameters should be recorded, false otherwise
	 */
	boolean recordParams() default true;

	/**
	 * Controls whether the method's return value should be recorded in the log entry.
	 * When enabled, the return value will be serialized and stored. This can be useful
	 * for tracking operation results and debugging.
	 * @return true if the return value should be recorded, false otherwise
	 */
	boolean recordResult() default true;

}
