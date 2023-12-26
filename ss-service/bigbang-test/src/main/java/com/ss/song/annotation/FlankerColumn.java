package com.ss.song.annotation;

import com.ss.song.enums.None;
import com.ss.song.enums.SqlType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FlankerColumn
{
	/**
	 * 字段名.默认取类属性名
	 *
	 * @return
	 */
	String name() default "";

	/**
	 * 该字段是否可更新
	 *
	 * @return
	 */
	boolean updatable() default true;

	/**
	 * 是否可以为空
	 *
	 * @return
	 */
	boolean nullable() default true;

	/**
	 * 最大长度(字符串时有效)
	 *
	 * @return
	 */
	int length() default 0;

	/**
	 * DECIMAL精度
	 *
	 * @return
	 */
	int precision() default 12;

	/**
	 * DECIMAL精度
	 *
	 * @return
	 */
	int scale() default 2;

	/**
	 * 对应SQL字段类型
	 *
	 * @return
	 */
	SqlType sqlType() default SqlType.VARCHAR;

	/**
	 * 枚举内容类型<br>
	 * 属性为EnumSet时有效<br>
	 *
	 * @return
	 */
	Class<? extends Enum<?>> enumType() default None.class;

}
